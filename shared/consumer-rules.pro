# Consumer ProGuard rules for shared module.

# GitLive Firestore models - kotlinx.serialization decodes these by field name at runtime, so R8
# must not rename/strip their properties.
-keep class com.hussienfahmy.myGpaManager.data.sync.model.FirebaseNetworkSemester { *; }
-keep class com.hussienfahmy.myGpaManager.data.sync.model.FirebaseNetworkSubjects { *; }
-keep class com.hussienfahmy.myGpaManager.data.sync.model.FirebaseSettings { *; }
-keep class com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData { *; }
-keep class com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData$AcademicInfo { *; }
-keep class com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData$AcademicProgress { *; }
-keep enum com.hussienfahmy.myGpaManager.data.user_data.model.FirebaseUserData$AcademicInfo$Semester { *; }
