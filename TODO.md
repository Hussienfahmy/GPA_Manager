## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.
- Test Arabic.

## Enhancements
- Use navigation Suit to have navigation ars in different screen sizes. use standard.
- Animation for semester and predictive cards? Morphing would be super great.

## Before Publishing Android
- Test release build.
- Test Arabic.
- App profiler: precompile in semester_marks before publishing.

## Bugs
- Clicking on Midterm, Practical, Oral in semester marks item should expand it.