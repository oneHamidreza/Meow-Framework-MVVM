
# Meow Framework MVVM Android/Kotlin 
A Framework for Android Developers based on MVVM architecture and Material Design with most useful Kotlin Extensions.
![](/Resources/logo_meow_framework.png)
[ ![Download](https://api.bintray.com/packages/infinitydesign/meow/Meow-Framework-MVVM/images/download.svg?version=0.2.4-alpha) ](https://bintray.com/infinitydesign/meow/Meow-Framework-MVVM/0.2.4-alpha/link)

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

### Initialize
Create your application class that extends `MeowApp`. Dynamic Localization & Day/Night Theme congifurations must be set here with `MeowController`.
```kotlin
class App : MeowApp {

    // Layout Direction would be set automatically. 
    // (Example: "en": LayoutDirection.LTR  "fa": LayoutDirection.RTL)
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

### Activity or Fragment
Activities and Fragments in meow with supporting `DataBinding`
```kotlin
class MainActivity : MeowActivity<ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)// You can access Toolbar like this line
    }
}
```

`MeowController` is a class that controlls some of configurations in your app like language and theme. Configuration updates are in Real time.
```kotlin
import meow.controller

controller.updateLanguage(meowActivity, string)
controller.updateTheme(meowActivity, theme)
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


