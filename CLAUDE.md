# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Project Overview

GPA Manager is a multi-module Android application built with Jetpack Compose for managing student
GPA calculations, grades, and academic data. The app uses clean architecture with feature-based
modular structure, Hilt for dependency injection, and Firebase for data synchronization.

## Build Commands

### Development

```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK (minified)
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run tests for specific module
./gradlew :app:test
./gradlew :core:testDebugUnitTest

# Install debug build on connected device
./gradlew installDebug

# Clean build
./gradlew clean
```

### Verification

```bash
# Lint check
./gradlew lint

# Check dependencies
./gradlew dependencies
```

## Architecture

### Multi-Module Structure

The project follows a feature-based modular architecture with clean architecture principles:

- **app**: Main application module with navigation, DI setup, and WorkManager integration
- **core**: Shared utilities, data models, domain logic, and Hilt DI providers
- **core-ui**: Shared UI components and design system
- **build-logic**: Custom Gradle plugins for consistent module configuration

### Feature Modules

Each feature follows the pattern: `feature_name/{domain,data,presentation}`

Key features:

- **grades_setting**: Grade configuration and management
- **gpa_system_settings**: GPA calculation system configuration
- **quick**: Quick GPA calculation functionality
- **subject_settings**: Subject/course management
- **semester_marks**: Semester-based grade tracking
- **semester_subjctets**: Semester subject management
- **onboarding**: User onboarding and authentication
- **sync**: Firebase data synchronization
- **user_data**: User profile and preferences

### Dependency Injection with Hilt

- Uses **Hilt** for dependency injection throughout the codebase
- Each module has its own DI module in `{module}/di/Module.kt`
- Application class (`GPAManagerApplication`) annotated with `@HiltAndroidApp`
- Core module provides base dependencies like `@DispatcherDefault` coroutine dispatcher and
  application scope
- WorkManager integration with `HiltWorkerFactory`

### Custom Build Logic

Two custom Gradle plugins in `build-logic` provide consistent configuration:

- `base-module`: Base configuration for Android library modules (Hilt, testing, Kotlin setup)
- `base-compose-module`: Additional Compose-specific configuration and dependencies

## Technology Stack

### Core Framework

- **Kotlin** (2.2.0) with Java 17 compatibility
- **Android Gradle Plugin** (8.12.0)
- **Compose** (2025.07.00 BOM) for UI with Kotlin Compose plugin
- **Hilt** (2.57) for dependency injection
- **KSP** (KSP2 enabled) for annotation processing

### Key Libraries

- **Room** (2.7.2) for local database with migration schemas
- **DataStore** for preferences storage
- **WorkManager** for background tasks
- **Compose Destinations** (2.2.0) for type-safe navigation
- **Firebase** (Auth, Firestore, Storage) for backend services
- **Coil** for image loading
- **Material 3** design system
- **Kotlinx Serialization** for JSON handling

### Testing Stack

- **JUnit** for unit tests
- **Mockk** for mocking in tests
- **Google Truth** for fluent assertions
- **Robolectric** for Android unit tests
- **Espresso** for UI instrumented tests
- **Kotlinx Coroutines Test** for coroutine testing

## Build Configuration

### Version Management

All versions centralized in `gradle/libs.versions.toml`:

- **Compile SDK**: 36
- **Min SDK**: 26
- **Target SDK**: 36
- **Version Code**: 76
- **Version Name**: 6.4

### Build Features

- **Namespace**: Uses version catalog references for consistent configuration
- **Proguard**: Enabled for release builds with optimization
- **Debug builds**: Include `.debug` application ID suffix
- **KSP2**: Enabled for faster annotation processing

## Module Dependencies

### App Module Strategy

The app module includes presentation layers and specific data modules to ensure Hilt can generate a
complete dependency graph:

- All presentation modules for features
- Required data modules (user_data_data, subject_settings_data, gpa_system_settings_data, sync_data)
- Core and core-ui modules
- WorkManager and Hilt Work integration

### Custom Plugin Usage

When creating new modules:

- Apply `base-module` plugin for standard Android library setup
- Apply `base-compose-module` plugin for Compose-enabled modules
- These plugins automatically configure Hilt, testing dependencies, build settings, and Compose
  features

## Firebase Integration

- Google Services plugin with version 4.3.15 (avoid upgrading - newer versions have module
  compatibility issues)
- Multiple modules include `google-services.json` for Firebase features
- Authentication with Google Sign-In credential support
- Firestore for data persistence
- Cloud Storage integration
- Sync functionality for data backup and restore

## Development Patterns

### Package Structure

- Follow established pattern: `com.hussienfahmy.{module_name}`
- Clean architecture layers: data/domain/presentation
- DI modules consistently placed in `di/Module.kt`
- Use Hilt annotations: `@Module`, `@InstallIn(SingletonComponent::class)`, `@Provides`,
  `@Singleton`

### Dependency Injection Patterns

- Use custom qualifiers like `@DispatcherDefault` for specific dispatcher injection
- Provide application-scoped coroutine scope from core module
- WorkManager integration through Hilt for background tasks
- Each feature module manages its own dependencies while depending on core providers