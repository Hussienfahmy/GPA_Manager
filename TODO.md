# TODO

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
