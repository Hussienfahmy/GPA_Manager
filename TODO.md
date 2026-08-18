# TODO

- GPA: %1$.3f doesn't display formatted, it display exactly as it is, i think there is no float?
- Contribute to the app, did u find the app useful both links not open in ios.
- Use native FAB?
- Native back buttons in sub screens.

## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.

## Enhancements
- Add subject in history semester detail, the grades flow row doesn't have vertical spacing.
- History item, put the tag next to the title. leaving the line for gpa and hours.
- Finish semester, and + icon in history can be options on top or think of a better place.
- Use navigation Suit to have navigation ars in different screen sizes. use standard.

## Before Publishing Android
- App profiler: precompile in semester_marks before publishing.