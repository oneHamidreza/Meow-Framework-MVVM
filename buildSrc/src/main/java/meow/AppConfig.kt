package meow

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

        object Library {
            const val APPCOMPAT = "1.1.0"
            const val LIFECYCLE = "2.2.0"
            const val KTX_CORE = "1.2.0"
            const val COROUTINE = "1.3.0-gradle"
            const val SERIALIZE = "1.3.61"
            const val OKHTTP = "4.4.0"
            const val RETROFIT = "2.7.2"
            const val MOSHI = "1.9.2"
            const val JUNIT = "5.3.2"
        }
    }

    fun generateVersionCode(): Int {
        return Versions.API * 10000000 + Versions.BUILD_PHASE.ordinal * 1000000 + Versions.MAJOR * 10000 + Versions.MINOR * 100 + Versions.PATCH
    }

    fun generateVersionName(): String {
        val type = if (Versions.BUILD_PHASE.alias == "") "" else "-${Versions.BUILD_PHASE.alias}"
        return "${Versions.MAJOR}.${Versions.MINOR}.${Versions.PATCH}$type"
    }
}
