# iosApp

Minimal Xcode project shell for the iOS target, wired to `:shared`'s Kotlin/Native framework
export. Written blind in a sandbox with no macOS/Xcode/simulator anywhere in the pipeline -
**treat everything here as a first draft to open and fix in Xcode, not a working project.**

## What's here

- `iosApp.xcodeproj/project.pbxproj` - single-target SwiftUI app. **This is the single riskiest
  file in the whole migration.** `.pbxproj` is a hand-editable-but-fragile format (UUID
  cross-references, exact schema) that Xcode normally generates, not something meant to be
  authored by hand without Xcode itself to validate it. If Xcode refuses to open it or shows a
  "project file is corrupted" error, the fastest fix is likely **not** debugging this file byte by
  byte - create a fresh project via Xcode's File > New > Project > iOS > App (SwiftUI, Swift)
  template, then copy `iOSApp.swift`, `ComposeView.swift`, `Info.plist`, and `Assets.xcassets`
  into it and redo the two build-phase changes below.
- `iosApp/iOSApp.swift` - SwiftUI `@main App`, calls `KoinIosKt.doInitKoin()` before showing any UI.
- `iosApp/ComposeView.swift` - `UIViewControllerRepresentable` wrapping
  `MainViewControllerKt.MainViewController()` (`shared/src/iosMain/.../MainViewController.kt`).
- `iosApp/Info.plist`, `iosApp/Assets.xcassets/` - standard boilerplate, no real app icon bundled
  yet (empty AppIcon slot).

## Required manual steps (can't be done from this sandbox)

1. **Firebase config**: no `GoogleService-Info.plist` is bundled - fabricating one would just be
   fake credentials. Download the real one from the Firebase console for this project's iOS app
   entry (create one there first if it doesn't exist yet) and drag it into the `iosApp/iosApp/`
   Xcode group.
2. **Open in Xcode** and let it resolve/repair the project settings if needed - team/signing,
   bundle ID conflicts, deployment target, etc.
3. **Build once via Xcode** (not just `xcodebuild` from a script) so the "Embed Shared.framework"
   run-script build phase (`./gradlew :shared:embedAndSignAppleFrameworkForXcode`) actually runs
   and the framework gets linked. If linking fails, check `FRAMEWORK_SEARCH_PATHS` and
   `OTHER_LDFLAGS` in the target's build settings against whatever path that Gradle task actually
   produces on your machine/Kotlin version - the exact output directory has shifted across
   Kotlin/Compose Multiplatform versions and this was written from general knowledge, not observed
   output.
4. Add an app icon to `Assets.xcassets/AppIcon.appiconset/`.

## Known gaps (see individual file comments for detail)

- Sign in with Apple's Xcode capability ("Sign in with Apple" under Signing & Capabilities) isn't
  enabled in this project file - add it, and enable the capability in the Apple Developer portal
  for this app ID.
- `PdfReportPrinter`, `ImageThumbnailer`, `ReportBrandingProvider`'s iOS actuals, and Coil3's iOS
  network engine (`core-ui/src/iosMain/.../CoilImageLoader.ios.kt`) are all best-effort, unverified
  native interop - see their file comments in `core/src/iosMain/` and `core-ui/src/iosMain/`.
