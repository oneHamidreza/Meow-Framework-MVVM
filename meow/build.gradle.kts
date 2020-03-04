import meow.AppConfig
import meow.AppConfig.Versions
import meow.AppConfig.Library
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlinx-serialization")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.SDK_COMPILE)

    defaultConfig {
        minSdkVersion(Versions.SDK_MIN)
        targetSdkVersion(Versions.SDK_TARGET)

        versionCode = AppConfig.generateVersionCode()
        versionName = AppConfig.generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // KOTLIN
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

//     MOSHI SERIALIZE
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")

    Library.mainDependencies.forEach {
        implementation(it)
    }
}
