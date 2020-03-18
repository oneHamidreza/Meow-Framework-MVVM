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

/**
 * Build Application Configure object containing build info, dependencies, versioning.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-02-24
 */

object AppConfig {

    object Build {
        const val PACKAGE_NAME = "com.etebarian.meowframework"

        enum class PHASE(var alias: String) {
            ALPHA("alpha"),
            BETA("beta"),
            CANARY("canary"),
            RC("rc"),
            STABLE("")
        }
    }

    object Library {
        val mainDependencies = arrayOf(
            // Core
            kotlinx("serialization-runtime", "0.14.0"),
            kotlinx("coroutines-core", "1.3.0-gradle"),
            // Material
            "com.google.android.material:material:1.1.0",
            // Lifecycle
            "androidx.lifecycle:lifecycle-common-java8:2.2.0",
            "androidx.lifecycle:lifecycle-extensions:2.2.0",
            "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0",
            "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0",
            "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-alpha01",
            // ExifInterface
            "androidx.exifinterface:exifinterface:1.1.0",
            // OkHttp & Retrofit & Moshi
            "com.squareup.okhttp3:okhttp:4.4.0",
            "com.squareup.retrofit2:retrofit:2.7.2",
            "com.squareup.retrofit2:converter-moshi:2.7.2",
            "com.squareup.moshi:moshi:1.9.2",
            "com.squareup.moshi:moshi-kotlin:1.9.2",
            // Kodein
            "org.kodein.di:kodein-di-core:6.5.2",
            "org.kodein.di:kodein-di-erased:6.5.2",
            "org.kodein.di:kodein-di-framework-android-x:6.5.2",
            // Secure Shared Preferences
            "in.co.ophio:secure-preferences:0.1.3",
            // Navigation
            "androidx.navigation:navigation-fragment-ktx:2.2.1",
            "androidx.navigation:navigation-ui-ktx:2.2.1",
            // Glide
            "com.github.bumptech.glide:glide:4.11.0"
        )

        val sampleDependencies = emptyArray<String>()

    }

    /*
         Semantic Versioning
             by https://medium.com/@maxirosson/d6ec171cfd82
         and customized for supporting build type.
     */
    object Versions {
        const val API = 1
        const val MAJOR = 0
        const val MINOR = 0
        const val PATCH = 1
        val BUILD_PHASE = Build.PHASE.ALPHA

        const val SDK_COMPILE = 29
        const val SDK_MIN = 19
        const val SDK_TARGET = 29
        const val KOTLIN = "1.3.70"
        const val GLIDE = "4.11.0"
    }

    fun generateVersionCode(): Int {
        return Versions.API * 10000000 + Versions.BUILD_PHASE.ordinal * 1000000 + Versions.MAJOR * 10000 + Versions.MINOR * 100 + Versions.PATCH
    }

    fun generateVersionName(): String {
        val type = if (Versions.BUILD_PHASE.alias == "") "" else "-${Versions.BUILD_PHASE.alias}"
        return "${Versions.MAJOR}.${Versions.MINOR}.${Versions.PATCH}$type"
    }
}

fun kotlinx(module: String, version: String? = null): Any =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"