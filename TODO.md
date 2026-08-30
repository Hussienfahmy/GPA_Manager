## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.

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
