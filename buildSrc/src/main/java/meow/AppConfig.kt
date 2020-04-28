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

object AppConfig {//todo find way for show sources

    object Build {
        const val APPLICATION_ID = "com.etebarian.meowframework"
        const val APP_MODULE = "sample"
        const val APP_PACKAGE = "sample"
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
        const val MINOR = 6
        const val PATCH = 1
        val BUILD_PHASE = Build.PHASE.ALPHA

        const val SDK_COMPILE = 29
        const val SDK_MIN = 19
        const val SDK_TARGET = 29

        const val KOTLIN = "1.3.72"
        const val KOTLINX_SERIALIZATION = "1.3.70"
        const val NAVIGATION = "2.3.0-alpha04"
        const val DOKKA = "0.10.1"
        const val COROUTINE = "1.3.5"
        const val SERIALIZATION_RUNTIME = "0.20.0"
        const val MATERIAL = "1.2.0-alpha05"
        const val LIFECYCLE = "2.2.0"
        const val EXIF_INTERFACE = "1.1.0"
        const val OKHTTP = "4.4.0"//TODO TEST ON API 19
        const val RETROFIT = "2.7.2"
        const val MOSHI = "1.9.2"
        const val KODEIN = "6.5.2"
        const val SECURE_PREFERENCES = "0.1.3"
        const val GLIDE = "4.11.0"
        const val GOOGLE_PHONE_NUMBER = "8.10.5"
        const val LOCALIZATION = "1.2.4"
        const val SWIPE_REFRESH_LAYOUT = "1.0.0"
    }

    object Library {
        val implementationItems = arrayOf(
            // Kotlin
            kotlin("stdlib-jdk8", Versions.KOTLIN),
            // Kotlinx
            kotlinx("coroutines-core", Versions.COROUTINE),
            kotlinx("serialization-runtime", Versions.SERIALIZATION_RUNTIME),
            // Material
            "com.google.android.material:material:${Versions.MATERIAL}",
            "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE_REFRESH_LAYOUT}",
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
            "in.co.ophio:secure-preferences:${Versions.SECURE_PREFERENCES}",
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
        const val libraryDesc = "One MVVVM framework for Android Developers in Kotlin."
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

fun getAllResourcesSrcDirs(project: Project): ArrayList<String> {
    val list = arrayListOf<String>()
    val path =
        project.rootDir.absolutePath + "\\" + AppConfig.Build.APP_MODULE + "\\src\\main\\kotlin\\" + AppConfig.Build.APP_PACKAGE
    val root = File(path)
    list.add(project.rootDir.absolutePath + "\\" + AppConfig.Build.APP_MODULE + "\\src\\main\\res")
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