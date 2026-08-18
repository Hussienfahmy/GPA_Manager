# TODO

- Native back buttons in sub screens.

## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.

## Enhancements
- Add subject in history semester detail, the grades flow row doesn't have vertical spacing.
- History item, put the tag next to the title. leaving the line for gpa and hours.
- Finish semester, and + icon in history can be options on top or think of a better place. we can replace all fabs for that.
- Use navigation Suit to have navigation ars in different screen sizes. use standard.

## Before Publishing Android
- App profiler: precompile in semester_marks before publishing.