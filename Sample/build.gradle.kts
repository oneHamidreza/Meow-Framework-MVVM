import com.etebarian.meowframework.AppConfig
import com.etebarian.meowframework.AppConfig.Build
import com.etebarian.meowframework.AppConfig.Versions
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:${Versions.Library.APPCOMPAT}")
    implementation("androidx.core:core-ktx:${Versions.Library.KTX_CORE}")

//    testImplementation "org.junit.jupiter:junit-jupiter-api:$JUNIT_VERSION"
//    testImplementation "org.junit.jupiter:junit-jupiter-params:$JUNIT_VERSION"
//    runtime "org.junit.jupiter:junit-jupiter-engine:$JUNIT_VERSION"
}