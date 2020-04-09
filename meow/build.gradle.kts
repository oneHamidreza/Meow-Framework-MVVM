import com.novoda.gradle.release.PublishExtension
import meow.AppConfig
import meow.AppConfig.Library
import meow.AppConfig.Publishing
import meow.AppConfig.Versions

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version (meow.AppConfig.Versions.KOTLINX_SERIALIZATION)//need package
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

    // Implementation Dependencies
    Library.implementationItems.forEach {
        implementation(it)
    }

    // Kapt Dependencies
    Library.kaptItems.forEach {
        kapt(it)
    }
}

configure<PublishExtension> {
    userOrg = Publishing.bintrayUsername
    groupId = Publishing.groupId
    artifactId = Publishing.artifactId
    publishVersion = AppConfig.generateVersionName()
    repoName = Publishing.bintrayRepoName
    desc = Publishing.libraryDesc
    website = Publishing.siteUrl
    autoPublish = true
}
