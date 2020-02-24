buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.6.0-rc03")
        classpath(kotlin("gradle-plugin", "1.3.61"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}