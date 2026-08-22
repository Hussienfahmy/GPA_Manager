## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.
- Test Arabic.

## Enhancements
- Animation between pills swap in Semester tab, Normal and Predictive, Personal Info First and Second Semester.
- Can the top bar be scrollable/collapse when scroll the screen?
- Use navigation Suit to have navigation ars in different screen sizes. use standard.

## Before Publishing Android
- Test release build.
- Test Arabic.
- App profiler: precompile in semester_marks before publishing.

## Bugs
- Can't add a subject with 0 Hours
- In Semester history detail, we need to be able to add "NP-No Grade Pass" and "NF-No Grade Fail" this will be separate from the Semester Grades and the database, those just in the History.
- Add Percentage for Grades Settings, change description to Minimal Percentage to achieve this grade, same for Points and next to it both, more description like "Percentage / Points this Grade starts from"
- Navigation animation all sub screen should be sliding or something funny rather than the current fade in