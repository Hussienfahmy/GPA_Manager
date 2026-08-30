## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.

## Before Publishing iOS
- Verify release signing: archive with destination "Any iOS Device (arm64)" and confirm
  Distribute App completes. This is the real signing check (the dev-profile "no devices"
  warning in Signing & Capabilities is expected and irrelevant to distribution). Also confirm
  the Sign in with Apple capability row + `iosApp/iosApp.entitlements` are present.

## Before Publishing Android
- App profiler: precompile in semester_marks before publishing.
