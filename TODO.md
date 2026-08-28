## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.
- iOS reinstall auto-login: iOS Keychain survives app deletion, so Firebase Auth session (Google/Apple/email) is restored on reinstall with no sign-in. Android wipes it. Detect a fresh install via DataStore (`has_launched_before`, wiped on uninstall); on that detection, trigger a sync download to pull the user's data back down — do NOT force logout.

## Before Publishing Android
- App profiler: precompile in semester_marks before publishing.
