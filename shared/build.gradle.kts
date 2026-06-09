import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

group = "com.dario.kmp"
version = "1.0.0"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    `maven-publish`
}

kotlin {
    // Explicitly apply the default hierarchy so that iosMain, appleMain, nativeMain, etc.
    // are properly wired to their child targets BEFORE the custom jsAndWasmMain source set
    // adds its own dependsOn edges (which would otherwise suppress the default template).
    applyDefaultHierarchyTemplate()

    val xcf = XCFramework("Shared")
    listOf(
        iosArm64(),           // physical iPhone/iPad
        iosSimulatorArm64(),  // simulator on Apple Silicon Mac (M1/M2/M3)
        // iosX64 intentionally excluded: Compose Multiplatform 1.11.x dropped iosX64 support.
        // Intel Mac users: set EXCLUDED_ARCHS[sdk=iphonesimulator*] = x86_64 in Xcode build
        // settings to run the iosSimulatorArm64 slice via Rosetta 2.
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            freeCompilerArgs += listOf("-Xbinary=bundleId=com.dario.kmp.shared")
            xcf.add(this)
        }
    }
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    android {
       namespace = "com.dario.kmp.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        val jsAndWasmMain by creating {
            dependsOn(sourceSets.commonMain.get())
        }
        jsMain.get().dependsOn(jsAndWasmMain)
        wasmJsMain.get().dependsOn(jsAndWasmMain)
        jsAndWasmMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            // Serialization & Coroutines
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            // Coil3 for async image loading
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

// Lock the generated Res class to the original package name so existing source imports
// (dariokmp.shared.generated.resources.*) remain valid regardless of the Maven group setting.
compose.resources {
    packageOfResClass = "dariokmp.shared.generated.resources"
    publicResClass = true
    generateResClass = always
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/krishna-dario/dario-kmp")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gp_user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gp_token") as String?
            }
        }
    }
}