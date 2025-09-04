# Consumer ProGuard rules for core module - minimal necessary rules only

# Keep only the Koin DI module classes that are actually referenced by the app
-keep class com.hussienfahmy.core.di.ModuleKt { *; }
-keep class com.hussienfahmy.core.data.local.di.DatabaseModuleKt { *; }

# Keep only domain interfaces that are actually injected via Koin
-keep interface com.hussienfahmy.core.domain.auth.repository.AuthRepository
-keep interface com.hussienfahmy.core.domain.auth.service.AuthService  
-keep interface com.hussienfahmy.core.domain.storage.repository.StorageRepository
-keep interface com.hussienfahmy.core.domain.user_data.repository.UserDataRepository

# Keep domain models that are used across modules
-keep class com.hussienfahmy.core.domain.auth.repository.AuthResult* { *; }
-keep class com.hussienfahmy.core.domain.auth.repository.AuthUserData { *; }
-keep class com.hussienfahmy.core.domain.auth.service.AuthServiceResult* { *; }
-keep class com.hussienfahmy.core.domain.auth.service.AuthServiceUserData { *; }
-keep class com.hussienfahmy.core.domain.user_data.model.UserData* { *; }

# Keep use case that is directly used
-keep class com.hussienfahmy.core.domain.user_data.use_cases.GetUserData { *; }