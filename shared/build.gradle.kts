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
    applyDefaultHierarchyTemplate()

    val xcf = XCFramework("Shared")

    // iosX64 excluded: Compose Multiplatform 1.7+ dropped iosX64 support.
    // Intel Mac users must use Rosetta 2 (iosSimulatorArm64 slice).
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            binaryOption("bundleId", "com.dario.kmp.shared")
            linkerOpts += "-ld64"
            xcf.add(this)
        }
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

// Force Skiko to the version bundled with Compose Multiplatform 1.11.1.
// coil3 3.2.0 pulls skiko:0.9.4 which conflicts with CMP's skiko:0.144.6.
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.skiko") {
            useVersion("0.144.6")
            because("Align with Compose Multiplatform 1.11.1 bundled Skiko version")
        }
    }
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