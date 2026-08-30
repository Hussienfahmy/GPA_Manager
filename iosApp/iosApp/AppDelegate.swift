import UIKit
import FirebaseInstallations
import FirebaseMessaging
import UserNotifications
import Shared

/// Owns the parts of app startup that need a UIKit app-delegate hook: Firebase/Koin init and the
/// FCM ⇄ APNs plumbing. The iOS analog of :app's GPAManagerApplication + GPAFirebaseMessagingService.
final class AppDelegate: NSObject, UIApplicationDelegate, MessagingDelegate, UNUserNotificationCenterDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        // Configures Firebase (FIRApp.configure), Koin, Coil and the BGTaskScheduler handler.
        // Must run before any Firebase/Koin use and before the app finishes launching, hence here
        // rather than in iOSApp.init().
        KoinIosKt.doInitApp()

        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self
        // Gets an APNs token (no permission prompt); FCM mints its registration token off it. The
        // user-facing alert permission is requested separately from Compose via Calf.
        application.registerForRemoteNotifications()

        logInstallationIDForInAppMessagingTests()

        return true
    }

    /// Prints the Firebase Installation ID (FID) so it can be pasted into the Firebase console's
    /// In-App Messaging "Test on your device" flow. Debug builds only.
    private func logInstallationIDForInAppMessagingTests() {
        #if DEBUG
        Installations.installations().installationID { id, error in
            if let id {
                NSLog("[FIAM] Firebase Installation ID (FID) for test devices: \(id)")
            } else {
                NSLog("[FIAM] Could not fetch Installation ID: \(error?.localizedDescription ?? "unknown")")
            }
        }
        #endif
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().apnsToken = deviceToken
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        NSLog("APNs registration failed: \(error.localizedDescription)")
    }

    // MARK: - MessagingDelegate

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        guard let fcmToken else { return }
        IosPushKt.registerFcmToken(token: fcmToken)
    }

    // MARK: - UNUserNotificationCenterDelegate

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        // Show the notification while the app is foregrounded, matching Android's heads-up default.
        completionHandler([.banner, .list, .sound, .badge])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        Messaging.messaging().appDidReceiveMessage(response.notification.request.content.userInfo)
        completionHandler()
    }
}
