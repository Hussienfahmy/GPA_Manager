import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        // The iOS analog of :app's GPAManagerApplication.onCreate() - must run before any
        // Compose UI is shown, since every screen resolves its dependencies through Koin.
        KoinIosKt.doInitApp()
    }

    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea(edges: .all)
        }
    }
}
