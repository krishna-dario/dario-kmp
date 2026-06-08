# DarioKMP — Kotlin Multiplatform Shared Module

A standalone KMP module that delivers the **Discover** feature (Recipe / Article / Audio detail screens)
as a fully shared Compose Multiplatform UI. The same business logic and UI runs identically on
Android native, iOS native, and React Native apps — with zero KMP changes required per consumer.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [Building the Module](#building-the-module)
- [Integration: Android Native App](#integration-android-native-app)
- [Integration: iOS Native App](#integration-ios-native-app)
- [Integration: React Native App](#integration-react-native-app)
- [CI/CD — GitHub Actions](#cicd--github-actions)
- [Publishing a New Version](#publishing-a-new-version)
- [Local Development](#local-development)
- [Troubleshooting](#troubleshooting)

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                  DarioKMP Shared Module                      │
│                                                             │
│  domain/  →  usecase/  →  presentation/  →  ui/            │
│  (models)    (business)   (ViewModel)       (Compose UI)    │
└────────────────────┬────────────────────────────────────────┘
                     │ compiles to
          ┌──────────┴──────────┐
          │                     │
     Android AAR          iOS XCFramework
    (GitHub Packages)    (GitHub Actions artifact)
          │                     │
    ┌─────┴──────┐        ┌─────┴──────┐
    │            │        │            │
 Android      React    iOS Native   React
 Native App   Native   App          Native
              (Android)             (iOS)
```

**Key principle:** The KMP module is the single source of truth.
Consumer apps only add a thin bridge layer (~30–80 lines) — no business logic, no UI code.

---

## Project Structure

```
shared/src/
├── commonMain/kotlin/com/dario/kmp/
│   ├── App.kt                              # Dev-only demo entry point
│   ├── core/
│   │   └── network/
│   │       └── KtorClientFactory.kt        # Shared Ktor HTTP client factory
│   └── feature/
│       └── discover/
│           ├── data/
│           │   ├── datasource/             # DiscoverRemoteDataSource
│           │   ├── local/                  # DiscoverDemoDataSource (mock data)
│           │   ├── remote/
│           │   │   ├── api/                # DiscoverApi (Ktor)
│           │   │   └── dto/                # Response DTOs + mappers
│           │   └── repository/             # DiscoverRepositoryImpl
│           ├── di/
│           │   └── DiscoverModule.kt       # Manual DI — provideViewModel()
│           ├── domain/
│           │   ├── model/                  # DetailContent, ItemType, NextUpItem, etc.
│           │   ├── repository/             # DiscoverRepository (interface)
│           │   └── usecase/                # GetItemDetailUseCase
│           ├── presentation/
│           │   ├── ItemDetailViewModel.kt
│           │   ├── ItemDetailUiState.kt
│           │   ├── ItemDetailUiEvent.kt
│           │   └── ItemDetailSideEffect.kt
│           ├── theme/
│           │   ├── DiscoverColors.kt
│           │   ├── DiscoverTheme.kt
│           │   └── DiscoverTypography.kt
│           └── ui/
│               ├── ItemDetailScreen.kt     # Root composable
│               └── components/
│                   ├── ArticleContent.kt
│                   ├── AudioContent.kt
│                   ├── RecipeContent.kt
│                   └── SharedComponents.kt
└── iosMain/kotlin/com/dario/kmp/
    └── feature/discover/
        └── DiscoverViewController.kt       # UIViewController bridge for Swift
```

---

## Tech Stack

| Layer         | Library                   | Version |
|--------------|---------------------------|---------|
| Language      | Kotlin Multiplatform      | 2.4.0   |
| UI            | Compose Multiplatform     | 1.11.1  |
| Networking    | Ktor Client               | 3.1.3   |
| Serialization | kotlinx.serialization     | 2.0.0   |
| Images        | Coil3                     | 3.1.0   |
| ViewModel     | lifecycle-viewmodel (KMP) | 2.9.0   |
| Build         | AGP                       | 9.2.1   |

---

## Building the Module

### Prerequisites

- JDK 17+
- macOS (required for iOS XCFramework and `publishToMavenLocal`)
- Android Studio Narwhal or later

### Available item types

```kotlin
enum class ItemType { RECIPE, ARTICLE, AUDIO }
```

---

## Integration: Android Native App

### Step 1 — Add repository to `build.gradle` (project level)

```groovy
allprojects {
    repositories {
        // ... other repos
        maven {
            url = uri("https://maven.pkg.github.com/krishna-dario/dario-kmp")
            credentials {
                username = project.hasProperty('gp_user') ? project.property('gp_user') : System.getenv("USERNAME") ?: ""
                password = project.hasProperty('gp_token') ? project.property('gp_token') : System.getenv("CICD_TOKEN") ?: ""
            }
        }
    }
}
```

### Step 2 — Add dependency to `app/build.gradle`

```groovy
dependencies {
    implementation 'com.dario.kmp:shared:1.0.0'
}
```

### Step 3 — Add Kotlin compiler flag

The KMP module uses Kotlin 2.4.0. If your Android project uses an older Kotlin version,
add this flag to prevent metadata version mismatch errors:

```groovy
android {
    kotlinOptions {
        freeCompilerArgs += ["-Xskip-metadata-version-check"]
    }
}
```

### Step 4 — Create the host Activity

```kotlin
// DiscoverKmpActivity.kt
class DiscoverKmpActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val type = intent.getStringExtra(EXTRA_TYPE)
            ?.let { runCatching { ItemType.valueOf(it) }.getOrNull() }
            ?: ItemType.RECIPE

        val viewModel = DiscoverModule.provideViewModel(
            baseUrl = "https://your-cms.example.com/"
        )

        setContent {
            ItemDetailScreen(viewModel = viewModel, itemType = type, onClose = ::finish)
        }
    }

    companion object {
        private const val EXTRA_TYPE = "extra_type"

        fun launch(context: Context, type: ItemType) {
            context.startActivity(
                Intent(context, DiscoverKmpActivity::class.java)
                    .putExtra(EXTRA_TYPE, type.name)
            )
        }
    }
}
```

### Step 5 — Register in AndroidManifest.xml

```xml
<activity
    android:name=".discover.DiscoverKmpActivity"
    android:exported="false"
    android:windowSoftInputMode="adjustResize" />
```

### Step 6 — Launch from anywhere

```kotlin
DiscoverKmpActivity.launch(context, ItemType.RECIPE)
DiscoverKmpActivity.launch(context, ItemType.ARTICLE)
DiscoverKmpActivity.launch(context, ItemType.AUDIO)
```

---

## Integration: iOS Native App

### Step 1 — Get the XCFramework

**Option A — From GitHub Actions** (recommended for team):
1. Go to the [Actions tab](https://github.com/krishna-dario/dario-kmp/actions)
2. Run the `Build DarioKMP` workflow with `target = ios`
3. Download `Shared-XCFramework-debug` from the run summary

**Option B — Build locally** (macOS + Xcode required):
```bash
cd ~/path/to/DarioKMP
./gradlew :shared:assembleSharedDebugXCFramework
# Output: shared/build/XCFrameworks/debug/Shared.xcframework
```

### Step 2 — Add XCFramework to Xcode

1. Drag `Shared.xcframework` into your Xcode project navigator
2. In target settings → **General** → **Frameworks, Libraries, and Embedded Content**
3. Set `Shared.xcframework` to **Embed & Sign**

### Step 3 — Call from Swift

```swift
import Shared

// Present Recipe screen
let vc = DiscoverViewControllerKt.DiscoverViewController(
    type: "RECIPE",
    baseUrl: "https://your-cms.example.com/",
    onClose: { self.dismiss(animated: true) }
)
present(vc, animated: true)

// Present Article screen
let vc = DiscoverViewControllerKt.DiscoverViewController(
    type: "ARTICLE",
    baseUrl: "https://your-cms.example.com/",
    onClose: { self.dismiss(animated: true) }
)
present(vc, animated: true)

// Present Audio screen
let vc = DiscoverViewControllerKt.DiscoverViewController(
    type: "AUDIO",
    baseUrl: "https://your-cms.example.com/",
    onClose: { self.dismiss(animated: true) }
)
present(vc, animated: true)
```

### Supported type strings

| Swift string | Maps to      |
|-------------|--------------|
| `"RECIPE"`  | Recipe screen |
| `"ARTICLE"` | Article screen |
| `"AUDIO"`   | Audio screen  |

Invalid strings silently fall back to `RECIPE`.

---

## Integration: React Native App

The RN app uses the **same AAR and XCFramework** as the native apps.
You add a thin Native Module bridge on each platform (~80 lines total), then
call a single JS function from anywhere in your app.

### Android bridge

#### 1. Add repository and dependency

`android/app/build.gradle`:
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/krishna-dario/dario-kmp")
        credentials {
            username = System.getenv("USERNAME") ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: ""
        }
    }
}

dependencies {
    implementation 'com.dario.kmp:shared:1.0.0'
    implementation 'androidx.activity:activity-compose:1.9.0'
}
```

#### 2. Create `DiscoverKmpActivity.kt`

Same as the Android native app above — copy the exact same file into
`android/app/src/main/java/com/yourapp/`.

#### 3. Create `DiscoverKmpModule.kt`

```kotlin
// android/app/src/main/java/com/yourapp/kmp/DiscoverKmpModule.kt
package com.yourapp.kmp

import com.dario.kmp.feature.discover.di.DiscoverModule
import com.dario.kmp.feature.discover.domain.model.ItemType
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class DiscoverKmpModule(context: ReactApplicationContext) :
    ReactContextBaseJavaModule(context) {

    override fun getName() = "DiscoverKmp"

    @ReactMethod
    fun open(type: String, baseUrl: String) {
        val activity = currentActivity ?: return
        val itemType = runCatching { ItemType.valueOf(type.uppercase()) }
            .getOrDefault(ItemType.RECIPE)
        DiscoverKmpActivity.launch(activity, itemType)
    }
}
```

#### 4. Create `DiscoverKmpPackage.kt`

```kotlin
// android/app/src/main/java/com/yourapp/kmp/DiscoverKmpPackage.kt
package com.yourapp.kmp

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext

class DiscoverKmpPackage : ReactPackage {
    override fun createNativeModules(ctx: ReactApplicationContext) =
        listOf(DiscoverKmpModule(ctx))
    override fun createViewManagers(ctx: ReactApplicationContext) = emptyList()
}
```

#### 5. Register in `MainApplication.kt`

```kotlin
override fun getPackages(): List<ReactPackage> = listOf(
    MainReactPackage(),
    DiscoverKmpPackage(),   // ← add this
)
```

#### 6. Register Activity in `AndroidManifest.xml`

```xml
<activity
    android:name=".kmp.DiscoverKmpActivity"
    android:exported="false"
    android:windowSoftInputMode="adjustResize" />
```

---

### iOS bridge

#### 1. Add the XCFramework

Follow the same steps as [iOS Native App — Step 1 and Step 2](#step-1--get-the-xcframework) above.

#### 2. Create `DiscoverBridge.m`

```objc
// ios/DiscoverBridge.m
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(DiscoverKmp, NSObject)
RCT_EXTERN_METHOD(open:(NSString *)type baseUrl:(NSString *)baseUrl)
@end
```

#### 3. Create `DiscoverBridge.swift`

```swift
// ios/DiscoverBridge.swift
import Shared

@objc(DiscoverKmp)
class DiscoverBridge: NSObject {

    @objc func open(_ type: String, baseUrl: String) {
        DispatchQueue.main.async {
            guard let root = UIApplication.shared
                .connectedScenes
                .compactMap({ $0 as? UIWindowScene })
                .first?.windows
                .first(where: { $0.isKeyWindow })?.rootViewController
            else { return }

            let vc = DiscoverViewControllerKt.DiscoverViewController(
                type: type,
                baseUrl: baseUrl,
                onClose: { root.dismiss(animated: true) }
            )
            root.present(vc, animated: true)
        }
    }

    @objc static func requiresMainQueueSetup() -> Bool { true }
}
```

#### 4. Create or update `YourApp-Bridging-Header.h`

```objc
#import <React/RCTBridgeModule.h>
```

---

### JavaScript interface

Create this once and import it anywhere in your RN app:

```javascript
// src/modules/DiscoverKmp.js
import { NativeModules, Platform } from 'react-native';

const { DiscoverKmp } = NativeModules;

if (!DiscoverKmp) {
    console.warn('DiscoverKmp native module not found. Check your bridge setup.');
}

const BASE_URL = 'https://your-cms.example.com/';

export const openRecipe  = (baseUrl = BASE_URL) => DiscoverKmp?.open('RECIPE', baseUrl);
export const openArticle = (baseUrl = BASE_URL) => DiscoverKmp?.open('ARTICLE', baseUrl);
export const openAudio   = (baseUrl = BASE_URL) => DiscoverKmp?.open('AUDIO', baseUrl);
```

```javascript
// Usage anywhere in your RN app
import { openRecipe, openArticle, openAudio } from '../modules/DiscoverKmp';

<Button title="Open Recipe"  onPress={() => openRecipe()} />
<Button title="Open Article" onPress={() => openArticle()} />
<Button title="Open Audio"   onPress={() => openAudio()} />
```

---

## CI/CD — GitHub Actions

The workflow is **manual trigger only** — no automatic builds on push or PR.

**How to trigger:** GitHub → `Actions` tab → `Build DarioKMP` → `Run workflow`

### Inputs

| Input        | Options                              | Description                          |
|-------------|--------------------------------------|--------------------------------------|
| `target`    | `ios` / `android` / `both` / `publish` | What to build or publish           |
| `build_type`| `debug` / `release`                  | Build variant (ignored for `publish`) |

### Jobs

| Target    | Runner         | Output                                         | Retention |
|-----------|---------------|------------------------------------------------|-----------|
| `ios`     | `macos-latest` | `Shared.xcframework` as workflow artifact      | 14 days   |
| `android` | `ubuntu-latest`| AAR files as workflow artifact                 | 14 days   |
| `both`    | Both runners   | Both outputs in parallel                       | 14 days   |
| `publish` | `macos-latest` | Publishes `com.dario.kmp:shared` to GitHub Packages | —    |

Download artifacts from the **Summary** page of the completed workflow run.

---

## Publishing a New Version

### Via GitHub Actions (recommended)

1. Bump version in `shared/build.gradle.kts`:
   ```kotlin
   version = "1.0.1"  // ← increment
   ```
2. Commit and push
3. Run workflow with `target = publish`
4. Update the dependency version in all consumer apps

### Locally (macOS only)

```bash
cd ~/AndroidStudioProjects/DarioKMP
./gradlew :shared:publishToMavenLocal
```

Consumer app must have `mavenLocal()` in its repositories block.

---

## Local Development

```bash
# Verify Android compilation
./gradlew :shared:compileKotlinAndroid

# Build iOS XCFramework — debug
./gradlew :shared:assembleSharedDebugXCFramework

# Build iOS XCFramework — release
./gradlew :shared:assembleSharedReleaseXCFramework

# Publish to Maven Local (for local Android testing)
./gradlew :shared:publishToMavenLocal

# Publish to GitHub Packages (requires GITHUB_TOKEN env var)
./gradlew :shared:publish
```

---

## Troubleshooting

### `Metadata version mismatch` on Android build

The KMP module uses Kotlin 2.4.0. If your Android project uses an older version, add:
```groovy
kotlinOptions {
    freeCompilerArgs += ["-Xskip-metadata-version-check"]
}
```

### `Unresolved reference` errors after KMP update

Re-publish to Maven Local after every KMP change:
```bash
./gradlew :shared:publishToMavenLocal
```
Then **Sync Project** in Android Studio.

### OOM during iOS release build

Add to `gradle.properties`:
```properties
kotlin.daemon.jvmargs=-Xmx8192M -XX:+UseG1GC
kotlin.native.parallelThreads=1
```

### React Native — `DiscoverKmp is null` in JS

- **Android:** Verify `DiscoverKmpPackage` is registered in `MainApplication`
- **iOS:** Verify bridging header includes `<React/RCTBridgeModule.h>` and `DiscoverBridge.m` is in the Xcode target
- Run `npx react-native clean` and rebuild

### GitHub Packages auth failure (401)

Ensure `gp_user` and `gp_token` (or `GITHUB_TOKEN`) are set.
The token needs `read:packages` scope for consumers and `write:packages` scope for publishing.
Add to `~/.gradle/gradle.properties`:
```properties
gp_user=your-github-username
gp_token=ghp_xxxxxxxxxxxx
```
