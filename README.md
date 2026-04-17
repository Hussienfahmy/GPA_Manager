# GPA Manager

A feature-rich Android application for managing student GPA calculations, grades, and academic data — built with modern Android development practices.

---

## Download

<a href="https://play.google.com/store/apps/details?id=com.hussienFahmy.myGpaManager&hl=en">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png"
       alt="Get it on Google Play"
       height="80"/>
</a>

---

## Screenshots

<!-- TODO: Add app screenshots here -->
<!-- Example:
| Home | GPA Calculator | Semester View |
|------|---------------|---------------|
| ![Home](screenshots/home.png) | ![Calculator](screenshots/calculator.png) | ![Semester](screenshots/semester.png) |
-->
<img width="305" height="678" alt="image" src="https://github.com/user-attachments/assets/a28aa0dd-837a-46c9-9270-409b506c6e1d" />
<img width="305" height="678 alt="image" src="https://github.com/user-attachments/assets/d1320c42-95fc-4b59-a880-04d5463d6731" />
<img width="305" height="678" alt="image" src="https://github.com/user-attachments/assets/011f9ff0-aa09-4b0c-8b60-73857dfecde4" />
<img width="305" height="678" alt="image" src="https://github.com/user-attachments/assets/e1dc2e00-63ac-4294-ac57-e61323611372" />

---

## Features

- **GPA Calculation** — Quick and accurate GPA calculations based on your grading system
- **Grade Management** — Track and manage grades across subjects and semesters
- **Custom GPA Systems** — Configure custom grading scales to match your institution
- **Semester Tracking** — Organize academic data by semester
- **Firebase Sync** — Backup and restore data across devices
- **Google Sign-In** — Secure authentication with your Google account
- **Material 3 Design** — Clean, modern UI following Material Design guidelines

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture (multi-module) |
| DI | Koin 4 |
| Database | Room |
| Preferences | DataStore |
| Navigation | Compose Destinations |
| Backend | Firebase (Auth, Firestore, Storage) |
| Background | WorkManager |
| Image Loading | Coil |

---

## Architecture

The project follows **clean architecture** with a **feature-based multi-module** structure:

```
GPA_Manager/
├── app/                    # Main module — navigation, DI setup, WorkManager
├── core/                   # Shared utilities, models, domain logic, Koin providers
├── core-ui/                # Shared UI components and design system
├── build-logic/            # Custom Gradle plugins for consistent module config
└── feature modules/        # Each feature in its own module
    ├── grades_setting/
    ├── gpa_system_settings/
    ├── quick/
    ├── subject_settings/
    ├── semester_marks/
    ├── semester_subjctets/
    ├── onboarding/
    ├── sync/
    └── user_data/
```

Each feature module follows the layered pattern:
```
feature_name/
├── domain/        # Use cases, interfaces
├── data/          # Repositories, data sources
└── presentation/  # ViewModels, Composable screens
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK with compile SDK 36

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Hussienfahmy/GPA_Manager.git
   cd GPA_Manager
   ```

2. **Configure Firebase**

   The app uses Firebase for authentication and data sync. You'll need to obtain `google-services.json` files from the [Firebase Console](https://console.firebase.google.com/) and place them in:
   ```
   onboarding/onboarding_presentation/google-services.json
   user_data/user_data_data/google-services.json
   sync/sync_data/google-services.json
   ```
   > These files are excluded from version control for security reasons.

3. **Build the project**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on a device or emulator**
   ```bash
   ./gradlew installDebug
   ```

---

## Build Commands

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (minified)
./gradlew assembleRelease

# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Lint check
./gradlew lint

# Clean build
./gradlew clean
```

---

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository
2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Follow the code style**
   - Use Kotlin idioms and coroutines
   - Follow the existing clean architecture layer separation
   - Place DI definitions in `{module}/di/Module.kt`
   - Use Koin DSL (`single`, `factory`, `viewModel`) for dependency injection
   - Use `koinViewModel()` in Composables
4. **Write tests** for new functionality where applicable
5. **Run lint and tests** before submitting
   ```bash
   ./gradlew lint test
   ```
6. **Commit** with a clear, descriptive message
7. **Open a Pull Request** with a description of your changes

### Code Style Guidelines

- Package structure: `com.hussienfahmy.{module_name}`
- Apply `base-module` plugin for new Android library modules
- Apply `base-compose-module` plugin for Compose-enabled modules
- Coroutine dispatcher injection via `named(CoreQualifiers.DEFAULT_DISPATCHER)`

---

## License

This project is proprietary software. All rights reserved.

---

## Developer

**Hussien Fahmy**
