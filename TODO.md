## iOS parity gaps

- Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS at all.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Fill in storeRatingUrl() for iOS once the app is on the App Store.

## Enhancements
- Add Summer semester in the options?!, if so consider finish semester shouldn't go to summer one. this is special
- is bottom sheets titles and top content are filled max width ? since it is wrapped to the left even on AR. it worth having a preview for bottom sheets contents in both languages.
- More screen. Rate app item description should be platform aware

## Bugs
- Subjects with 0 credit hours, have 0 Total marks, so Marks feature Have 0 too, and added marks count from negative and can't adjust how final marks count from since it is 0 and can't be more that the total marks of 0
- remove all "-" the long one added with texts, replace it.
- Quick semester results doesn't clear on navigation away, shouldn't the viewmodel destroy automatically?
- Grades Settings screen, percentage have double percentage sign
- isLandscapeOrientation() on IOS, doesn't re-evaluate on rotation. it should be in common and drived from widnow size class as it provide compsoe state

## Before Publishing Android
- App profiler: precompile in semester_marks before publishing.
