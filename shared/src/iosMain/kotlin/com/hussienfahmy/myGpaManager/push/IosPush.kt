package com.hussienfahmy.myGpaManager.push

import com.hussienfahmy.core.domain.user_data.use_cases.UpdateFCMToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

/**
 * Called from iosApp/iosApp/AppDelegate.swift's MessagingDelegate every time Firebase issues or
 * refreshes the FCM registration token - the iOS analog of :app's
 * GPAFirebaseMessagingService.onRegistered().
 *
 * Writes the token onto the signed-in user's Firestore doc via the same shared use case Android
 * uses. Safe to call while signed out: UserDataRepository resolves no user doc and the write is a
 * no-op. Not "init"-prefixed - see doInitApp()'s note on Swift's Objective-C bridge.
 */
fun registerFcmToken(token: String) {
    val koin = KoinPlatform.getKoin()
    val updateFcmToken = koin.get<UpdateFCMToken>()
    val scope = koin.get<CoroutineScope>()
    scope.launch {
        updateFcmToken(token)
    }
}
