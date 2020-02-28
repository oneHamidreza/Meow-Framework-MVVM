import meow.AppConfig
import meow.AppConfig.Build
import meow.AppConfig.Versions
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Versions.SDK_COMPILE)

    defaultConfig {
        applicationId = Build.PACKAGE_NAME + "_sample"
        minSdkVersion(Versions.SDK_MIN)
        targetSdkVersion(Versions.SDK_TARGET)

        versionCode = AppConfig.generateVersionCode()
        versionName = AppConfig.generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        setConsumerProguardFiles(kotlin.arrayOf("consumer-rules.pro"))
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":meow"))

    //KOTLIN
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation(kotlin("serialization", Versions.Library.SERIALIZE))
    implementation(kotlinx("coroutines-core", Versions.Library.COROUTINE))

    // ANDROIDX
    implementation("androidx.appcompat:appcompat:${Versions.Library.APPCOMPAT}")
    implementation("androidx.core:core-ktx:${Versions.Library.KTX_CORE}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.Library.LIFECYCLE}")

    // AND OTHERS
    implementation("com.squareup.okhttp3:okhttp:${Versions.Library.OKHTTP}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.Library.RETROFIT}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.Library.RETROFIT}")
    implementation("com.squareup.moshi:moshi:${Versions.Library.MOSHI}")
    implementation("com.squareup.moshi:moshi-kotlin:${Versions.Library.MOSHI}")
}

fun kotlinx(module: String, version: String? = null): Any =
        "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"
