# Consumer ProGuard rules for core-ui module - minimal necessary rules only

# Keep only the Koin DI module class
-keep class com.hussienfahmy.core_ui.domain.di.ModuleKt { *; }

# Keep only the classes that are directly referenced by app
-keep class com.hussienfahmy.core_ui.presentation.user_data.UserDataViewModel { *; }
-keep class com.hussienfahmy.core_ui.Dimensions* { *; }