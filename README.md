# Meow Framework MVVM 🐱 in Kotlin
A Framework for Android Developers based on MVVM architecture and Material Design.
![](/Resources/logo_meow_framework.png)

## Setup
```kotlin
implementation("com.etebarian:meow-framework-mvvm:0.2.4-alpha")
```
After adding library, some of most useful libraries (such as `Androidx`,`Coroutine`,`Glide`,`Kodein`,`Kotlin Serialization`,`Material` ,`Navigation`,`Retrofit` ) will be added in your app. So you don't need add this libraries manually.

> Enable Androidx in `gradle.properties` 
>```properties
>android.useAndroidX=true
>android.enableJetifier=true
>```

## Getting Started
Create your application class that extends `MeowApp`. Dynamic Localization & Day/Night Theme congifurations must be set here with `MeowController`.
```kotlin
class App : MeowApp {

    override fun getLanguage(context: Context?) = "en" // or any language such as ("fa","fr","ar",etc.)
    
    // Sample app theme is setted by Android System Light/Dark (Day/Naight) mode
    override fun getTheme(context: Context?) = if (context.isNightModeFromSettings()) MeowController.Theme.NIGHT else  MeowController.Theme.DAY
    
    override fun onCreate() {
        super.onCreate()
         MeowController().apply {
            language = getLanguage(this@App)
            isDebugMode = BuildConfig.DEBUG
            theme = getTheme(this@App)
        }.bindApp(this)
    }

}


```
`MeowController` is a class that controlls some of configuration
```kotlin
import meow.controller

controller.updateLanguage()
```
License
--------

    Copyright 2020 Hamidreza Etebarian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


