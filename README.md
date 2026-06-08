# DarioKMP — Kotlin Multiplatform Shared Module

Shared KMP module used by the Dario Android app and iOS app. Built with Compose Multiplatform,
Ktor, and kotlinx.serialization. Targets Android and iOS.

---

## Project Structure

```
shared/src/
├── commonMain/kotlin/com/dario/kmp/
│   ├── App.kt                          # Demo entry point (dev only)
│   ├── core/
│   │   └── network/
│   │       └── KtorClientFactory.kt   # Shared Ktor HTTP client
│   └── feature/
│       └── discover/
│           ├── data/
│           │   ├── datasource/        # Remote data source
│           │   ├── local/             # Demo/mock data source
│           │   ├── remote/api/        # Ktor API client
│           │   ├── remote/dto/        # Response models + mappers
│           │   └── repository/        # DiscoverRepositoryImpl
│           ├── di/
│           │   └── DiscoverModule.kt  # Manual DI (no Hilt/Koin)
│           ├── domain/
│           │   ├── model/             # DetailContent, ItemType, etc.
│           │   ├── repository/        # DiscoverRepository interface
│           │   └── usecase/           # GetItemDetailUseCase
│           ├── presentation/
│           │   ├── ItemDetailViewModel.kt
│           │   ├── ItemDetailUiState.kt
│           │   ├── ItemDetailUiEvent.kt
│           │   └── ItemDetailSideEffect.kt
│           ├── theme/                 # DiscoverTheme, colors, typography
│           └── ui/
│               ├── ItemDetailScreen.kt
│               └── components/        # RecipeContent, ArticleContent, AudioContent
└── iosMain/kotlin/com/dario/kmp/
    └── feature/discover/
        └── DiscoverViewController.kt  # UIViewController bridge for Swift
```

---

## Tech Stack

| Layer         | Library                         | Version |
|--------------|---------------------------------|---------|
| Language      | Kotlin Multiplatform            | 2.4.0   |
| UI            | Compose Multiplatform           | 1.11.1  |
| Networking    | Ktor Client                     | 3.1.3   |
| Serialization | kotlinx.serialization           | 2.0.0   |
| Images        | Coil3                           | 3.1.0   |
| ViewModel     | lifecycle-viewmodel (KMP)       | 2.9.0   |
| Build         | AGP                             | 9.2.1   |

---

## Android Integration

The shared module is published to Maven Local and consumed by the Android app.

**Publish to Maven Local** (must run on macOS — Kotlin/Native requires macOS host):

```bash
cd ~/AndroidStudioProjects/DarioKMP
./gradlew :shared:publishToMavenLocal
```

Then sync the Android project. The Android app consumes it via:

```groovy
// app/build.gradle
implementation 'com.dario.kmp:shared:1.0.0'
```

**Android entry point:**

```kotlin
// Launch the KMP Discover screen from any Android context
DiscoverKmpActivity.launch(context, ItemType.RECIPE)
```

---

## iOS Integration

The iOS framework is built as an XCFramework and consumed by the native iOS app.

**Build locally** (requires macOS + Xcode):

```bash
# Debug
./gradlew :shared:assembleSharedDebugXCFramework

# Release
./gradlew :shared:assembleSharedReleaseXCFramework
```

Output: `shared/build/XCFrameworks/{debug|release}/Shared.xcframework`

**Swift usage:**

```swift
let vc = DiscoverViewControllerKt.DiscoverViewController(
    type: "RECIPE",
    baseUrl: "https://your-cms.example.com/",
    onClose: { self.dismiss(animated: true) }
)
present(vc, animated: true)
```

---

## CI/CD — GitHub Actions

The workflow is **manual trigger only** — no automatic builds on push or PR.

**How to trigger:** GitHub → `Actions` tab → `Build DarioKMP` → `Run workflow`

### Inputs

| Input        | Options                      | Description       |
|-------------|------------------------------|-------------------|
| `target`    | `ios` / `android` / `both`   | What to build     |
| `build_type`| `debug` / `release`          | Build variant     |

### Jobs

**iOS** — runs on `macos-latest` (Xcode pre-installed)
- Builds `Shared.xcframework` (debug or release)
- Uploads as workflow artifact with 14-day retention

**Android** — runs on `ubuntu-latest`
- Builds `shared-{debug|release}.aar`
- Uploads as workflow artifact with 14-day retention

Download artifacts from the **Summary** page of the completed workflow run.

---

## Local Development

**Requirements:** JDK 17, Android Studio Narwhal or later, macOS for iOS/publish tasks.

```bash
# Verify Android compilation
./gradlew :shared:compileKotlinAndroid

# Build Android AAR
./gradlew :shared:assembleDebug

# Build iOS XCFramework (macOS only)
./gradlew :shared:assembleSharedDebugXCFramework

# Publish to Maven Local (macOS only)
./gradlew :shared:publishToMavenLocal
```
