/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow

import org.gradle.api.Project
import java.io.File

/**
 * Build Application Configure object containing build info, dependencies, versioning.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-24
 */

object AppConfig {

    object Build {
        const val APPLICATION_ID = "com.etebarian.meowframework"
        const val APP_MODULE = "Sample"
        const val APP_PACKAGE = "sample"
        const val LIBRARY_MODULE = "MeowFramework"
        const val LIBRARY_PACKAGE = "meow"
        const val SRC_MAIN = "src/main/"

        enum class PHASE(var alias: String) {
            ALPHA("alpha"),
            BETA("beta"),
            CANARY("canary"),
            RC("rc"),
            STABLE("")
        }
    }

    object Versions {
        /*
             Semantic Versioning
                 by https://medium.com/@maxirosson/d6ec171cfd82
             and customized for supporting build type.
         */
        const val API = 1
        const val MAJOR = 0
        const val MINOR = 10
        const val PATCH = 0
        val BUILD_PHASE = Build.PHASE.STABLE

        const val SDK_COMPILE = 30
        const val SDK_MIN = 21
        const val SDK_TARGET = 30

        const val BROWSER = "1.3.0"
        const val COROUTINE = "1.4.2"
        const val DOKKA = "0.10.1"
        const val EXIF_INTERFACE = "1.3.2"
        const val GLIDE = "4.12.0"
        const val GOOGLE_PHONE_NUMBER = "8.12.18"
        const val KODEIN = "6.5.5"
        const val KOTLIN = "1.4.30"
        const val KOTLINX_SERIALIZATION = "1.4.30"
        const val LIFECYCLE = "2.2.0"
        const val LOCALIZATION = "1.2.5"
        const val MATERIAL = "1.3.0"
        const val MOSHI = "1.11.0"
        const val MULTIDEX = "2.0.1"
        const val NAVIGATION = "2.3.3"
        const val OKHTTP = "3.12.0" // Don't update.
        const val RETROFIT = "2.9.0"
        const val RECYCLER_VIEW = "1.2.0-beta01"
        const val SECURITY_CRYPTO = "1.1.0-alpha03"
        const val SERIALIZATION_RUNTIME = "1.0-M1-1.4.0-rc"
        const val SWIPE_REFRESH_LAYOUT = "1.1.0"
        const val VIEWPAGER2 = "1.1.0-alpha01"
    }

    object Dependencies {
        val implementationItems = arrayOf(
                // Kotlin
                kotlin("stdlib-jdk8", Versions.KOTLIN),
                // Kotlinx
                kotlinx("coroutines-core", Versions.COROUTINE),
                kotlinx("serialization-runtime", Versions.SERIALIZATION_RUNTIME),
                // MultiDex
                "androidx.multidex:multidex:${Versions.MULTIDEX}",
                // Material
                "com.google.android.material:material:${Versions.MATERIAL}",
                "androidx.browser:browser:${Versions.BROWSER}",
                "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE_REFRESH_LAYOUT}",
                "androidx.viewpager2:viewpager2:${Versions.VIEWPAGER2}",
                // Recycler View
                "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}",
                // Lifecycle
                "androidx.lifecycle:lifecycle-common-java8:${Versions.LIFECYCLE}",
                "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE}",
                "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}",
                "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}",
                "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}",
                // ExifInterface
                "androidx.exifinterface:exifinterface:${Versions.EXIF_INTERFACE}",
                // OkHttp & Retrofit & Moshi
                "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}",
                "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}",
                "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}",
                "com.squareup.retrofit2:converter-scalars:${Versions.RETROFIT}",
                "com.squareup.moshi:moshi:${Versions.MOSHI}",
                "com.squareup.moshi:moshi-kotlin:${Versions.MOSHI}",
                // Kodein
                "org.kodein.di:kodein-di-core:${Versions.KODEIN}",
                "org.kodein.di:kodein-di-erased:${Versions.KODEIN}",
                "org.kodein.di:kodein-di-framework-android-x:${Versions.KODEIN}",
                // Secure Shared Preferences
                "androidx.security:security-crypto:${Versions.SECURITY_CRYPTO}",
                // Navigation
                "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}",
                "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}",
                // Glide
                "com.github.bumptech.glide:glide:${Versions.GLIDE}",
                // Google Phone Number
                "com.googlecode.libphonenumber:libphonenumber:${Versions.GOOGLE_PHONE_NUMBER}",
                // Localization
                "com.akexorcist:localization:${Versions.LOCALIZATION}"
        )

        val kaptItems = arrayListOf(
                "com.github.bumptech.glide:compiler:${Versions.GLIDE}",
                "com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI}"
        )
    }

    fun generateVersionCode(): Int {
        return Versions.API * 10000000 + Versions.BUILD_PHASE.ordinal * 1000000 + Versions.MAJOR * 10000 + Versions.MINOR * 100 + Versions.PATCH
    }

    fun generateVersionName(): String {
        val type = if (Versions.BUILD_PHASE.alias == "") "" else "-${Versions.BUILD_PHASE.alias}"
        return "${Versions.MAJOR}.${Versions.MINOR}.${Versions.PATCH}$type"
    }

    object Publishing {
        const val name = "Meow-Framework-MVVM"
        const val repo = "meow"
        const val developerId = "oneHamidreza"
        const val userOrg = "infinitydesign"
        const val groupId = "com.etebarian"
        const val artifactId = "meow-framework-mvvm"
        const val libraryDesc = "Develop MVVM & Material Android App Easy."
    }
}

fun <T> Project.getPropertyAny(key: String): T {
    val properties = java.util.Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }
    @Suppress("UNCHECKED_CAST")
    return properties.getProperty(key) as T
}

fun kotlinx(module: String, version: String? = null): Any =
        "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"

fun kotlin(module: String, version: String? = null): Any =
        "org.jetbrains.kotlin:kotlin-$module${version?.let { ":$version" } ?: ""}"

fun getAllResourcesSrcDirs(project: Project, isLibrary: Boolean = false): ArrayList<String> {
    val moduleName = if (isLibrary) AppConfig.Build.LIBRARY_MODULE else AppConfig.Build.APP_MODULE
    val packageName =
            if (isLibrary) AppConfig.Build.LIBRARY_PACKAGE else AppConfig.Build.APP_PACKAGE
    val list = arrayListOf<String>()
    val path =
            project.rootDir.absolutePath + "\\" + moduleName + "\\src\\main\\kotlin\\" + packageName
    val root = File(path)
    list.add(project.rootDir.absolutePath + "\\" + moduleName + "\\src\\main\\res")
    root.listDirectoriesWithChild().forEach { directory ->
        if (directory.isRes())
            list.add(directory.path)
    }
    return list
}

fun File.listDirectories() = listFiles()?.filter { it.isDirectory } ?: arrayListOf()
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