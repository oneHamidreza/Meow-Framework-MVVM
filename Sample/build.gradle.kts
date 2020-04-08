import com.android.build.gradle.internal.dsl.BuildType
import meow.AppConfig
import meow.AppConfig.Build
import meow.AppConfig.Library
import meow.AppConfig.Versions
import java.util.*
import kotlin.collections.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
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

    buildTypes {

        fun BuildType.setupApiUrl() {
            val apiUrl = getProperty<String>("api.url")
            buildConfigField("String", "API_URL", "\"$apiUrl\"")
        }

        getByName("debug") {
            setupApiUrl()
        }

        getByName("release") {
            setupApiUrl()
            isUseProguard = false
            isMinifyEnabled = true
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

            res.srcDirs(getAllResourcesSrcDirs())
        }
    }

    // exclude annotation from all of markwon dependency
    configurations {
        implementation.exclude(
            mapOf(
                "group" to "org.jetbrains",
                "module" to "annotations"
            )
        )
    }
}

fun <T> getProperty(key: String): T {
    val properties = Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }
    return properties.getProperty(key) as T
}

fun getAllResourcesSrcDirs(): ArrayList<String> {
    val list = arrayListOf<String>()
    val path =
        project.rootDir.absolutePath + "\\" + Build.APP_MODULE + "\\src\\main\\kotlin\\" + Build.APP_PACKAGE
    val root = File(path)
    root.listDirectoriesWithChild().forEach { directory ->
        if (directory.isRes())
            list.add(directory.path)
    }
    return list
}

fun File.listDirectories() = listFiles()!!.filter { it.isDirectory }
fun File.listDirectoriesWithChild(): List<File> {
    val list = ArrayList<File>()

    fun File.findAllDirectories(list: ArrayList<File>) {
        listDirectories().forEach {
            it.findAllDirectories(list)
            list.add(it)
        }
    }

    findAllDirectories(list)
    return list
}

fun File.isRes() = name == "res"

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":meow"))

    // Implementation Dependencies
    Library.implementationItems.forEach {
        implementation(it)
    }

    // Kapt Dependencies
    Library.kaptItems.forEach {
        kapt(it)
    }

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

    // Markwon Image Loader
    implementation("ru.noties:markwon-image-loader:1.0.5")
}