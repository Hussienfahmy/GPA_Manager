package com.hussienfahmy.core.domain.auth.service

import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import com.hussienfahmy.core.domain.crash.CrashReporter
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.OAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.AuthenticationServices.ASPresentationAnchor
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlin.coroutines.resume

/**
 * iOS's sign-in flow: Sign in with Apple (AuthenticationServices) rather than porting Google
 * Sign-In's iOS SDK - the more platform-idiomatic choice, per the migration plan. Adapted from
 * the real, working approach used by mirzemehdi/KMPAuth (an open-source Kotlin Multiplatform
 * auth library, MIT licensed) - swapped from that library's CocoaPods-based FIROAuthProvider
 * interop to this project's own GitLive Firebase-Kotlin-SDK OAuthProvider, since this codebase
 * already uses GitLive for every other Firebase product (Auth/Firestore/Storage/Analytics).
 */
class AppleSignIn(
    private val crashReporter: CrashReporter
) {
    // Held strongly while a request is in flight: ASAuthorizationController's delegate and
    // presentationContextProvider properties are weak references in UIKit, so this whole request
    // would otherwise be eligible for Kotlin/Native GC before its delegate callback ever fires.
    private var activeController: ASAuthorizationController? = null
    private var activeDelegate: NSObject? = null
    private var activePresentationContextProvider: NSObject? = null

    suspend fun signIn(): AuthResult? = suspendCancellableCoroutine { continuation ->
        val request = ASAuthorizationAppleIDProvider().createRequest().apply {
            requestedScopes = listOf(ASAuthorizationScopeFullName, ASAuthorizationScopeEmail)
        }

        val rawNonce = randomNonceString()
        request.nonce = sha256(rawNonce)

        val scope = CoroutineScope(Dispatchers.Main)
        val delegate = Delegate(rawNonce, scope, crashReporter) { result ->
            activeController = null
            activeDelegate = null
            activePresentationContextProvider = null
            if (continuation.isActive) continuation.resume(result)
        }
        val presentationContextProvider = PresentationContextProvider()

        val controller = ASAuthorizationController(listOf(request))
        controller.delegate = delegate
        controller.presentationContextProvider = presentationContextProvider

        activeController = controller
        activeDelegate = delegate
        activePresentationContextProvider = presentationContextProvider

        controller.performRequests()
    }

    private class PresentationContextProvider :
        NSObject(), ASAuthorizationControllerPresentationContextProvidingProtocol {
        override fun presentationAnchorForAuthorizationController(
            controller: ASAuthorizationController,
        ): ASPresentationAnchor {
            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            return rootViewController?.view?.window
        }
    }

    private class Delegate(
        private val rawNonce: String,
        private val scope: CoroutineScope,
        private val crashReporter: CrashReporter,
        private val onResult: (AuthResult?) -> Unit,
    ) : NSObject(), ASAuthorizationControllerDelegateProtocol {

        @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
        override fun authorizationController(
            controller: ASAuthorizationController,
            didCompleteWithAuthorization: ASAuthorization,
        ) {
            val credential =
                didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
            val idTokenData = credential?.identityToken
            val idToken = idTokenData?.let { NSString.create(it, NSUTF8StringEncoding)?.toString() }
            if (credential == null || idToken == null) {
                onResult(AuthResult.Error("Unable to read Apple identity token"))
                return
            }

            scope.launch {
                try {
                    val firebaseCredential = OAuthProvider.credential(
                        providerId = "apple.com",
                        idToken = idToken,
                        rawNonce = rawNonce,
                    )
                    val user = Firebase.auth.signInWithCredential(firebaseCredential).user
                    if (user == null) {
                        onResult(AuthResult.Error("Firebase sign-in returned no user"))
                    } else {
                        // Apple only ever sends fullName on the very first authorization for a
                        // given app - it's absent on every subsequent sign-in, hence the fallback
                        // to whatever Firebase already has on file for this user.
                        val appleFullName = credential.fullName?.let {
                            listOfNotNull(it.givenName, it.familyName).joinToString(" ")
                        }?.takeIf { it.isNotBlank() }

                        onResult(
                            AuthResult.Success(
                                AuthUserData(
                                    id = user.uid,
                                    name = appleFullName ?: (user.displayName ?: ""),
                                    photoUrl = user.photoURL ?: "",
                                    email = user.email ?: (credential.email ?: ""),
                                )
                            )
                        )
                    }
                } catch (e: Exception) {
                    crashReporter.recordException(e, mapOf("operation" to "appleSignIn"))
                    onResult(AuthResult.Error(e.message ?: "Unknown error"))
                }
            }
        }

        override fun authorizationController(
            controller: ASAuthorizationController,
            didCompleteWithError: NSError,
        ) {
            onResult(AuthResult.Error(didCompleteWithError.localizedDescription))
        }
    }

    private fun randomNonceString(length: Int = 32): String {
        val randomBytes = secureRandomBytes(length)
        val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._"
        return randomBytes.map { byte -> charset[(byte.toInt() and 0xFF) % charset.length] }
            .joinToString("")
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun secureRandomBytes(length: Int): ByteArray = memScoped {
        val buffer = allocArray<UByteVar>(length)
        val status = SecRandomCopyBytes(kSecRandomDefault, length.convert(), buffer)
        check(status == errSecSuccess) { "SecRandomCopyBytes failed with OSStatus $status" }
        buffer.readBytes(length)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun sha256(input: String): String {
        val digest = UByteArray(CC_SHA256_DIGEST_LENGTH)
        val inputBytes = input.encodeToByteArray()
        inputBytes.usePinned {
            CC_SHA256(it.addressOf(0), inputBytes.size.convert(), digest.refTo(0))
        }
        return digest.toByteArray().toHexString(HexFormat.Default)
    }
}
