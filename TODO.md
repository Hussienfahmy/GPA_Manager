## iOS parity gaps

- Fill in storeRatingUrl() for iOS once the app is on the App Store (paste the numeric Apple ID
  from App Store Connect into `StoreRatingUrl.ios.kt`).

## Rate app

- Native in-app review per platform, falling back to the store URL when it can't run:
  - iOS: StoreKit `AppStore.requestReview(in:)` (iOS 16+) / `SKStoreReviewController.requestReview()`.
  - Android: Play In-App Review API (`ReviewManager`, `com.google.android.play:review`).
  - Fall back to opening `storeRatingUrl()` when the native flow is unavailable: rate limit hit
    (iOS caps at ~3 prompts/user/year and silently no-ops), Play Services missing, or the
    request fails / returns without showing anything.
  - Needs an expect/actual `RateApp` launcher replacing the current `storeRatingUrl()` +
    `UrlOpener` call site in `AppMoreScreen`. Keep the URL path reachable - native review can't
    be reliably triggered in dev or TestFlight.

## Before Publishing iOS
- Verify release signing: archive with destination "Any iOS Device (arm64)" and confirm
  Distribute App completes. This is the real signing check (the dev-profile "no devices"
  warning in Signing & Capabilities is expected and irrelevant to distribution). Also confirm
  the Sign in with Apple capability row + `iosApp/iosApp.entitlements` are present.
- Push: `iosApp.entitlements` keeps `aps-environment = development` on purpose — Xcode rewrites
  it to `production` at archive time. After Distribute, confirm the exported build's embedded
  entitlements show `aps-environment = production`, and send a real push to a TestFlight build
  to confirm production APNs works end to end.

## Before Publishing Android
- App profiler: precompile in semester_marks before publishing.
