import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.dagger.hilt)
}

android {
    namespace = "com.serson.my_pet"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.serson.my_pet"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    buildFeatures {
        compose = true
    }

    signingConfigs {
        create("release") {
            val keystoreProperties = Properties()
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                keystoreProperties.load(FileInputStream(localPropertiesFile))
            }

            // пытаемся взять значение из local.properties, если нет -> из Env (для CI)
            val storePath = keystoreProperties.getProperty("SIGNING_STORE_PATH") ?: System.getenv("SIGNING_STORE_PATH")
            val storePassVal = keystoreProperties.getProperty("SIGNING_STORE_PASSWORD") ?: System.getenv("SIGNING_STORE_PASSWORD")
            val keyAliasVal = keystoreProperties.getProperty("SIGNING_KEY_ALIAS") ?: System.getenv("SIGNING_KEY_ALIAS")
            val keyPassVal = keystoreProperties.getProperty("SIGNING_KEY_PASSWORD") ?: System.getenv("SIGNING_KEY_PASSWORD")

            if (!storePath.isNullOrEmpty()) {
                storeFile = file(storePath)
                storePassword = storePassVal
                keyAlias = keyAliasVal
                keyPassword = keyPassVal
            }
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro"),
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended:1.7.7")

    debugImplementation(libs.androidx.compose.ui.tooling)

    // retrofit2
    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    // okhttp3
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    // datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Dagger-Hilt
    ksp(libs.hilt.android.compiler) // Hilt compiler
//    ksp("com.google.dagger:dagger-compiler:2.49") // Dagger compiler
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)
}
