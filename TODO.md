# TODO

- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Light mode: Shadow on the subject cards in semester screen has cut off on the bottom. Also in Marks screen items too., history is fine.
- App profiler: precompile in semester_marks before publishing.
- Use navigation Suit to have navigation ars in different screen sizes. use standard.
- Use native FAB?
- Native back buttons in sub screens.
- Contribute to the app, did u find the app useful both links not open in ios.
- GPA: %1$.3f doesn't display formatted, it display exactly as it is, i think there is no float?
- History item, put the tag next to the title. leaving the line for gpa and hours.
- Finish semester, and + icon in history can be options on top or think of a better place.
- Add subject in history semester detail, the grades flow row doesn't have vertical spacing.

## iOS parity gaps

1. Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
