import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.BuildType
import meow.AppConfig
import meow.AppConfig.Build
import meow.AppConfig.Dependencies
import meow.AppConfig.Versions
import meow.getAllResourcesSrcDirs
import meow.getPropertyAny

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version (meow.AppConfig.Versions.KOTLINX_SERIALIZATION)//need package
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(Versions.SDK_COMPILE)

    defaultConfig {
        applicationId = Build.APPLICATION_ID + "_sample"
        minSdkVersion(Versions.SDK_MIN)
        targetSdkVersion(Versions.SDK_TARGET)

        versionCode = AppConfig.generateVersionCode()
        versionName = AppConfig.generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        setConsumerProguardFiles(kotlin.arrayOf("consumer-rules.pro"))

        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }

    applicationVariants.all {
        outputs.all {
            val fileName = "Meow-Framework-Sample-v${AppConfig.generateVersionName()}.apk"
            (this as BaseVariantOutputImpl).outputFileName = fileName
        }
    }

    buildTypes {

        fun BuildType.setupApiUrl() {
            val apiUrl = getPropertyAny<String>("api.url")
            buildConfigField("String", "API_URL", "\"$apiUrl\"")
        }

        getByName("debug") {
            setupApiUrl()
        }

        getByName("release") {
            setupApiUrl()
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    dataBinding {
        isEnabled = true
    }

    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = true
        disable("MissingDefaultResource")
    }

    sourceSets {
        getByName("main") {
            setRoot(Build.SRC_MAIN)
            manifest.srcFile("${Build.SRC_MAIN}AndroidManifest.xml")

            java.srcDirs("${Build.SRC_MAIN}kotlin")
            java.includes.add("/${Build.SRC_MAIN}**")
            java.excludes.add("**/build/**")

            res.srcDirs(getAllResourcesSrcDirs(project))
        }
    }

    // Exclude annotation from all of markwon dependency
    configurations {
        implementation.exclude(
            mapOf(
                "group" to "org.jetbrains",
                "module" to "annotations"
            )
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":Framework"))

    // Implementation Dependencies
    Dependencies.implementationItems.forEach {
        implementation(it)
    }

    // Kapt Dependencies
    Dependencies.kaptItems.forEach {
        kapt(it)
    }

    // Firebase
    implementation("com.google.firebase:firebase-analytics:17.4.0")
    implementation("com.google.firebase:firebase-messaging:20.1.6")

    // Markwon
    val markwonVersion = "4.3.1"
    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:ext-tables:$markwonVersion")
    implementation("io.noties.markwon:ext-tasklist:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")
    implementation("io.noties.markwon:image:$markwonVersion")
    implementation("io.noties.markwon:image-glide:$markwonVersion")
    implementation("io.noties.markwon:linkify:$markwonVersion")
    implementation("io.noties.markwon:recycler:$markwonVersion")
    implementation("io.noties.markwon:recycler-table:$markwonVersion")
    implementation("io.noties.markwon:syntax-highlight:$markwonVersion")

    implementation("io.noties:prism4j:2.0.0")
}