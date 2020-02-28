buildscript {
    val kotlinVersion = "1.3.61"

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.6.0")
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath(kotlin("serialization", kotlinVersion))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}