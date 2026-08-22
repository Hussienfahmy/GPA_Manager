## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.
- Test Arabic.

## Enhancements
- Can the top bar be scrollable/collapse when scroll the screen?
- Use navigation Suit to have navigation ars in different screen sizes. use standard.
- Animation for semester and predictive cards? Morphing would be super great.

## Before Publishing Android
- Test release build.
- Test Arabic.
- App profiler: precompile in semester_marks before publishing.

## Bugs
- In Semester history detail, we need to be able to add "NP-No Grade Pass" and "NF-No Grade Fail" this will be separate from the Semester Grades and the database, those just in the History.
- Navigation animation all sub screen should be sliding or something funny rather than the current fade in