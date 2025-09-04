# ProGuard rules for app module
# Library modules have minification disabled, only app module is minified

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Koin DI framework
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Keep Kotlin reflection for Koin
-keep class kotlin.jvm.functions.Function* { *; }
-keep class kotlin.jvm.internal.Lambda { *; }
-keep class kotlin.reflect.** { *; }

# Keep your app package - since library modules aren't minified, their classes will be available
-keep class com.hussienfahmy.myGpaManager.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep Google Auth
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Baseline Profile
-keep class androidx.profileinstaller.** { *; }

# Keep Android framework classes
-keep class * extends android.app.Application
-keep class * extends androidx.activity.ComponentActivity
-keep class * extends androidx.fragment.app.Fragment