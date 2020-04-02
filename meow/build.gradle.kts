import com.novoda.gradle.release.PublishExtension
import meow.AppConfig
import meow.AppConfig.Library
import meow.AppConfig.Publishing
import meow.AppConfig.Versions

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlinx-serialization")
    kotlin("kapt")
    id("com.novoda.bintray-release")
}

android {
    compileSdkVersion(Versions.SDK_COMPILE)

    defaultConfig {
        minSdkVersion(Versions.SDK_MIN)
        targetSdkVersion(Versions.SDK_TARGET)

        versionCode = AppConfig.generateVersionCode()
        versionName = AppConfig.generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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

    viewBinding {
        isEnabled = true
    }

    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = true
        disable("MissingDefaultResource")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // MAIN DEPENDENCIES
    Library.implementationItems.forEach {
        implementation(it)
    }

    // EXCLUSIVE DEPENDENCIES
    Library.kaptItems.forEach {
        implementation(it)
    }
}
configure<PublishExtension> {
    userOrg = Publishing.bintrayUsername
    groupId = Publishing.groupId
    artifactId = Publishing.artifactId
    publishVersion = AppConfig.generateVersionName()
    repoName = Publishing.bintrayRepoName
    desc = "A MVVVM framework for Android Developers in Kotlin."
    website = "https://github.com/oneHamidreza/meow-framework-mvvm"
    autoPublish = true
}
