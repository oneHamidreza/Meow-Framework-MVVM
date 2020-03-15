import com.android.build.gradle.internal.dsl.BuildType
import meow.AppConfig
import meow.AppConfig.Build
import meow.AppConfig.Library
import meow.AppConfig.Versions
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
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

        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }

    buildTypes {

        fun BuildType.setupApiUrl() {
            val properties = Properties().apply {
                load(
                    project.rootProject.file("local.properties").inputStream()
                )
            }
            val apiUrl = properties.getProperty("api.ip") ?: "noIP"
            buildConfigField("String", "Api_IP", "\"$apiUrl\"")
        }

        getByName("debug") {
            setupApiUrl()
        }

        getByName("release") {
            setupApiUrl()
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

    viewBinding {
        isEnabled = true
    }

    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // KOTLIN
    implementation(kotlin("stdlib-jdk8", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))

    implementation(project(":meow"))

    // MAIN DEPENDENCIES
    Library.mainDependencies.forEach {
        implementation(it)
    }

    // EXCLUSIVE DEPENDENCIES
    Library.sampleDependencies.forEach {
        implementation(it)
    }

}