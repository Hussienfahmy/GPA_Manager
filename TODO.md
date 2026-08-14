# TODO

- Delete examples tests and empty manifest and their directory if it becomes empty.
- When enabling sign in with apple id, make the onboarding screen in common main not expect actual.
- Why do we need UPLOAD_SYNC_TASK_IDENTIFIER? handleTask why dispatcher main ? 
- Auth Flow on android and ios needs a refactor to not using interfaces and using expect actual AFTER moving shared code to common, we have many ridiculous interfaces. 
- Why not using MainViewControllerKt.MainViewController() directly in iosApp? why intermediate struct compose view.
- App profiler: precompile in semester_marks before publishing.

## iOS parity gaps

1. Background sync — `sync_domain`'s `SyncWorkerUpload`/`SyncWorkerModule` (WorkManager-based).
   Comment in that module already says iOS's BGTaskScheduler equivalent is "deferred to the iOS
   phase."
2. Push notifications — `GPAFirebaseMessagingService` (FCM). Android-only, no APNs wiring on iOS
   at all.
3. Firebase Performance — `firebase.perf` in `app/build.gradle.kts`. Not portable via GitLive:
   only ~1% API coverage upstream.
4. Firebase In-App Messaging — `firebase.inappmessaging`. GitLive doesn't ship this product at
   all; no multiplatform path exists.
