import SwiftUI
import Shared

@main
struct iOSApp: App {
    // Startup (Firebase/Koin init) and FCM/APNs wiring live in AppDelegate - it needs the UIKit
    // app-delegate callbacks, and running doInitApp() from didFinishLaunchingWithOptions gives a
    // deterministic "before any Compose/Koin use" ordering.
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea(edges: .all)
        }
    }
}
