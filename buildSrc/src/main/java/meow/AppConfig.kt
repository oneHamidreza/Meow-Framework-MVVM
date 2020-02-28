package meow

import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Created by 1HE on 2020-02-24.
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
        val deps = arrayOf(
                // core
                kotlin("serialization", "1.3.61"),
                kotlinx("coroutines-core", "1.3.0-gradle"),
                // android x
                "androidx.core:core-ktx:1.2.0",
                "androidx.appcompat:appcompat:1.1.0",
                "androidx.lifecycle:lifecycle-extensions:2.2.0",
                // connection & parsing data
                "com.squareup.okhttp3:okhttp:4.4.0",
                "com.squareup.retrofit2:retrofit:2.7.2",
                "com.squareup.retrofit2:converter-moshi:2.7.2",
                "com.squareup.moshi:moshi:1.9.2",
                "com.squareup.moshi:moshi-kotlin:1.9.2"
        )

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


    }

    fun generateVersionCode(): Int {
        return Versions.API * 10000000 + Versions.BUILD_PHASE.ordinal * 1000000 + Versions.MAJOR * 10000 + Versions.MINOR * 100 + Versions.PATCH
    }

    fun generateVersionName(): String {
        val type = if (Versions.BUILD_PHASE.alias == "") "" else "-${Versions.BUILD_PHASE.alias}"
        return "${Versions.MAJOR}.${Versions.MINOR}.${Versions.PATCH}$type"
    }
}

fun kotlin(module: String, version: String? = null): Any =
        "org.jetbrains.kotlin:kotlin-$module${version?.let { ":$version" } ?: ""}"

fun kotlinx(module: String, version: String? = null): Any =
        "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$version" } ?: ""}"