# Meow Framework MVVM Android/Kotlin

A Framework that simplify developing MVVM architecture and Material Design in Android with Kotlin language including useful Extensions and Sample Application. Also this Framework has some tools for Retrofit and OKHttp and Coroutine for calling REST API requests.

<p align="center">
<img src="https://raw.githubusercontent.com/oneHamidreza/Meow-Framework-MVVM/master/Resources/img_github.png" width="60%" />
</p>

<p align="center">
<a href="https://bintray.com/infinitydesign/meow/Meow-Framework-MVVM/0.7.2-alpha/link" >
<img src="https://api.bintray.com/packages/infinitydesign/meow/Meow-Framework-MVVM/images/download.svg?version=0.7.2-alpha" />
</a>
</p>


## 📱 Sample Application

We suggest you to install [Meow-Sample.apk](https://github.com/oneHamidreza/Meow-Framework-MVVM/releases/download/v0.7.2-alpha/Meow-Framework-Sample-v0.7.2-alpha.apk) to be familiar with `Meow Framework`.

## 🛠 Setup

```groovy
implementation("com.etebarian:meow-framework-mvvm:0.7.2-alpha")
```

After adding library, some of most useful libraries (such as `Androidx AppCompat` , `Coroutine` , `Glide` , `Kodein` , `Kotlinx Serialization` , `Material Components` , `Moshi` , `Navigation Components` , `Retrofit` ) will be added in your app. So you don't need to add this libraries manually.  

List of dependencies can be found in [meow.AppConfig.kt](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/buildSrc/src/main/java/meow/AppConfig.kt) in `Dependencies` Object.

Check out [build.gradle.kts](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Framework/build.gradle.kts) in Sample module to avoid any issues related to setup & adding Framework.

> Enable androidx in `gradle.properties`.
>
> ```properties
>android.useAndroidX=true
>android.enableJetifier=true
>```
>
> Remember that you'll need to enable Java 8 & DataBinding in your app module `build.gradle`.

## 📃 Table of Contents

- [💡 Getting Started](https://github.com/oneHamidreza/Meow-Framework-MVVM#-getting-started)
  - [Initialization](https://github.com/oneHamidreza/Meow-Framework-MVVM#-initialization) : How to have a `MeowApp`.
  - [Meow Controller 🐈](https://github.com/oneHamidreza/Meow-Framework-MVVM#-meow-controller-) : Trust this CAT.
  - [MVVM Architecture](https://github.com/oneHamidreza/Meow-Framework-MVVM#-mvvm-architecture) : Get to know this architecture.
  - [Accessing Views with DataBinding](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/README.md#accessing-views-with-databinding) : Forget `findViewById()`.
- [🧩 Meow KTX (Kotlin Extensions)](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/README.md#-meow-ktx-kotlin-extensions)
- [📶 REST API : Retrofit + OKHttp + Coroutine + Moshi](https://github.com/oneHamidreza/Meow-Framework-MVVM#-rest-api--retrofit--okhttp--coroutine--moshi)
  - [MeowApi](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/README.md#create-api-that-extends-meowapi) : A new way to connect server.
  - [Common API Flow/Patterns](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/README.md#common-api-flowpatterns) : Some predefined patterns for REST APIs.
  - [Sample `Index` Api](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/README.md#sample-index-api) : A Sample for filling `RecyclerView` from JSON Data.
- [🎨 Material Components](https://github.com/oneHamidreza/Meow-Framework-MVVM#-material-components)
- [🌌 Meow Custom Widgets](https://github.com/oneHamidreza/Meow-Framework-MVVM#-meow-custom-widgets)

## 💡 Getting Started

We assume that you know MVVM architecture, but if you have some problems in understanding its, this [Articles](https://proandroiddev.com/tagged/mvvm) can help you.

### 🎮 Initialization

Create your application class which extends `MeowApp` and set it in `AndroidManifest.xml`. Dependency Injection in MVVM architecture is necessary, so we use `Kodein-DI` Framework.
You need to define `appModule` for View Models. Update application class like below :

```kotlin
class App : MeowApp() {

    // Create a kodein module.
    val appModule = Module("App Module", false) {
        // Provide object of SomeOfClass(such as View Models) in Kodein with bind() function.
        bind() from singleton { SomeOfClass(instance()) }
    }

   // Source is `KodeinAware` interface.
    override val kodein = Kodein.lazy {
        bind() from singleton { kodein.direct }
        bind() from singleton { this@App }
        import(androidXModule(this@App))
        import(meowModule) // Important
        import(appModule)
    }
}
```

### 🐱 Meow Controller 🐈

This Framework has two Highlighted Features :
- Dynamic **Day/Night** Theme to switch from `LIGHT` to `DARK` mode.
- Dynamic **Localization** to change language , currency formatting , date formatting of app Realtime.

to use above features, you'll need to define your `MeowController`.

`MeowController` is a class that controls some features in app such as above.
 If you want to use `avoidException` in your app, this class can controls Exception Handlers with `onException` property.

Update your application class like this :

```kotlin  
class App : MeowApp() {
    // Layout Direction would be set automatically by Android System.
    // (Example: "en": LayoutDirection.LTR  "fa": LayoutDirection.RTL).
    
    // Our Primary Sample app's language is English.
    override fun getLanguage(context: Context?) = "en" // or any language such as ("fa","fr","ar",etc.)     
    
    // Our Sample app's theme is set by Android System Light/Dark (Day/Night) mode.  
    override fun getTheme(context: Context?) =
        if (context.isNightModeFromSettings()) MeowController.Theme.NIGHT else  MeowController.Theme.DAY
         
    override fun onCreate() {
        super.onCreate()
        bindMeow { // Import it from meow package.
            it.isDebugMode = BuildConfig.DEBUG
            // Set other properties here.
            it.onException = {
                // Log to Fabric or any other Crash Management System. Just use `avoidException` instead of `try{}catch{}`
            }
        } 
    }
}
```

You can update language and theme from UI Thread by using `MeowController` global instance.

```kotlin
import meow.controller
  
controller.updateLanguage(meowActivity, string)
controller.updateTheme(meowActivity, theme)
```

### 📐 MVVM Architecture

MVVM is Model-View-ViewModel that we define it in Android App as Data Model - View (Activity, Fragment, DialogFragment, BottomSheetDialogFragment) - MeowViewModel.
   
Follow below steps to have an activity with MVVM Architecture.

#### 1. Create your ViewModel that extends `MeowViewModel`.

```kotlin
class MainViewModel(app: App): MeowViewModel(app)
```

#### 2. Provide your View Model in appModule in `App`.

```kotlin
val appModule = Module("App Module", false) {
    bindAutoTag<MainViewModel>() with provider {
        MainViewModel(kodein.direct.instance())
    }
}
```

>`bindAutoTag()` was imported from `meow.ktx.*` package.

#### 3.  Create XML layout with `DataBinding` structure.

```xml
<layout>
    <data>
        <variable
            name="viewModel"
            type="MainViewModel" />
    </data>
    <LinearLayout /> <!-- or another View -->
</layout>
```

#### 4. Create Activity/Fragment extends `MeowActivity/MeowFragment` + `DataBinding` + `ViewModel`.

‍‍‍`MainActivity` is a sample activity that needs Kodein Dependency Injection and ViewDataBinding and View Model. See this example :

```kotlin
class MainActivity : MeowActivity<ActivityMainBinding>() {
    // ActivityMainBinding is generated by Androidx Lifecycle DataBinding Utils.
    
    private val viewModel: MainViewModel by instanceViewModel()
    override fun layoutId() = R.layout.activity_main
    
    override fun initViewModel() {// Set View Model in binding.
        binding.viewModel = viewModel    
    }
}
```

### Accessing Views with DataBinding

You can access views like this code :

```xml
<com.google.android.material.appbar.MaterialToolbar
    android:id="@+id/toolbar"
    style="@style/Meow.Toolbar" />  
```  

```kotlin
class MainActivity : MeowActivity<ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        // ...
        binding.toolbar.title = "custom_title" 
    }
}
```

Now you have an Activity with MVVM architecture. In above sample, you can replace `MeowActivity` with `MeowFragment` to have MVVM Fragment.

## 🧩 Meow KTX (Kotlin Extensions)

We have developed some Kotlin Extensions that can be help us in building Android Apps. Just import `meow.ktx` package which include the following :

- [🧩 Android Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Android.md)
- [💵 Currency Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Currency.md)
- [📅 Date Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Date.md)
- [📂 File Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_File.md)
- [📇 JSON Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Json.md)
- [💉 Kodein Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Kodein.md)
- [💎 Kotlin Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Kotlin.md)
- [📃 Log Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Log.md)
- [📶 Network Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Network.md)
- [🚦 Permission Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Permission.md)
- [📬 Shared Preferences Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Shared_Preferences.md)
- [🍟 Snackbar Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Snackbar.md)
- [🧬 String Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_String.md)
- [🚂 System Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_System.md)
- [🍞 Toast Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Toast.md)
- [✅ Validate Extensions](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Docs/ReadME_Extensions_Validate.md)

### Ⓜ Material Design

Update App Theme in `styles.xml` with `DayNight` Material Theme. More details are at [Official Material Design Site](https://material.io/develop/android/docs/getting-started/).

```xml
<style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <!-- Original AppCompat attributes. -->   <!-- Define colors in colors.xml -->
    <item name="colorPrimary">YOUR_PRIMARY_COLOR</item>
    <item name="colorSecondary">YOUR_SECONDARY_COLOR</item>
    
    <item name="android:colorBackground">@color/meow_background</item>
    <item name="colorError">@color/meow_error</item>
    
    <!-- New MaterialComponents attributes. -->
    <item name="colorPrimaryVariant">YOUR_PRIMARY_VARIANT_COLOR</item>
    <item name="colorSecondaryVariant">YOUR_SECONDARY_VARIANT_COLOR</item>
    <item name="colorSurface">@color/meow_surface</item>
    <item name="colorOnPrimary">YOUR_ON_PRIMARY_COLOR</item>
    <item name="colorOnSecondary">YOUR_ON_SECONDARY_COLOR</item>
    <item name="colorOnBackground">@color/meow_on_background</item>
    <item name="colorOnError">@color/meow_on_error</item>
    <item name="colorOnSurface">@color/meow_on_surface</item>
    <item name="scrimBackground">@color/mtrl_scrim_color</item>
</style>  
```

#### 🖌 Material Text Styles + Font by using `Meow.TextAppearance` Style

Just do same as [styles_text_appearances.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/res/values/styles_text_appearances.xml).

> You must apply styles in `AppTheme`.
>
>```xml
><style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar" >
>   <item name="textAppearanceHeadline1">@style/textAppearance.Headline1</item>
>   <item name="textAppearanceHeadline2">@style/textAppearance.Headline2</item>
>   <item name="textAppearanceHeadline3">@style/textAppearance.Headline3</item>
>   <item name="textAppearanceHeadline4">@style/textAppearance.Headline4</item>
>   <item name="textAppearanceHeadline5">@style/textAppearance.Headline5</item>
>   <item name="textAppearanceHeadline6">@style/textAppearance.Headline6</item>
>   <item name="textAppearanceSubtitle1">@style/textAppearance.Subtitle1</item>
>   <item name="textAppearanceSubtitle2">@style/textAppearance.Subtitle2</item>
>   <item name="textAppearanceBody1">@style/textAppearance.Body1</item>
>   <item name="textAppearanceBody2">@style/textAppearance.Body2</item>
>   <item name="textAppearanceCaption">@style/textAppearance.Caption</item>
>   <item name="textAppearanceButton">@style/textAppearance.Button</item>
>   <item name="textAppearanceOverline">@style/textAppearance.Overline</item>
></style>
>```
  
## 📶 REST API : Retrofit + OKHttp + Coroutine + Moshi

Meow Framework provides for you to call Server REST API actions from Android App with `Retrofit`. Creating client connections will be with `OKHttp` and `Moshi` which help us to serialize json responses. We replaced `RxJava` with `Coroutine` to multi thread handling.
  
### Create Api that extends `MeowApi`

```kotlin
class AppApi(
    var app: App,
    baseUrl : String = "http://api-url.any/api/v1/"
): MeowApi(baseUrl)  
```  

### Common API Flow/Patterns

We'll show you how to call a request to server and get response from it. Then Data has been shown in UI by parsing Data.
Some of actions that is related to REST API can be have a flow/pattern. We define this patterns as :

- `Index` : Response with simple request from server can be parsed as List of Data Model.
- `Detail` : Response with simple request from server can be parsed as a Data Model.
- `Form` : Response with advanced request (send a form) from server can be parsed as a Data Model.

### Sample `Index` Api

For example, server gives this JSON response when we call `/api/v1/persons` with GET method :

```json
[
  {
    "id":1,
    "username":"oneHamidreza",
    "alias":"Hamidreza Etebarian"
  },
  
  {
    "id":2,
    "username":"samdh82",
    "alias":"Ali Modares"
  }
]
```

#### Data Model

Create a data class for JSON response that use Moshi `@Json` annotation.

```kotlin
data class Person(
    @Json(name = "id") var id: Int = 0,
    @Json(name = "username") var username: String? = null,
    @Json(name = "alias") var alias: String? = null) {
    
    // RecyclerView List Adapter requires DiffCallBack.
    class DiffCallback : DiffUtil.ItemCallback<CatBreed>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
    }
}
```  

#### Retrofit API Interface

Define an interface containing Rest API actions. Meow Framework uses `Coroutine` library to calling Rest API actions, so you must write `suspend` prefix for functions.

```kotlin
interface PersonApi {
    @GET("persons") // Don't need to write absolute path. OKHTTP appends this string at end of baseUrl.
    suspend fun getPersonIndex(): List<Person>
}
```

#### Call API action from ViewModel by using `safeCallApi()`. Update your ViewModel class like this :

```kotlin  
class PersonIndexViewModel(app:App) : MeowViewModel(app) {
    // Define LiveData variables.
    var eventLiveData = MutableLiveData<MeowEvent<*>>()
    var listLiveData = MutableLiveData<List<Person>>()
    var customLiveData = MutableLiveData<String>()
    
    fun callApi() {
        safeCallApi(
            liveData = eventLiveData,   
            apiAction = { AppApi(app).createServiceByAdapter<PersonApi>().getPersonIndex() }
        ) { _, it ->
            // If connection was Success, this line will be run.
            // Otherwise MeowEvent.Api.Error will be posted into eventLiveData.
            
            // You can observe it manually or use MeowFlow.
            listLiveData.postValue(it)
        }
    }
}
```

#### XML Layout

Create `activity_sample_index.xml` that hast `RecyclerView` to showing items.

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data> 
        <variable
            name="viewModel"
            type="PersonIndexViewModel" />
    <!-- Remember that viewModel type must be with package -->
    </data>
    
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
        
        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            style="@style/Meow.RecyclerView.Linear"
            meow_items="@{viewModel.listLiveData}" />
            
        <meow.widget.MeowProgressBar
            android:id="@+id/progressbar"
            style="@style/Meow.ProgressBar.Medium.Primary" />
     
    </FrameLayout>
</layout>
```

#### MeowActivity/MeowFragment + MeowFlow

Use `MeowFlow` to handle events from ViewModel automatically.

```kotlin
class SampleIndexActivity : MeowActivity<ActivitySampleIndexBinding>() {
    //...
    override fun initViewModel() {
        binding.viewModel = viewModel
        callApiAndObserve()    
    }
    
    private fun callApiAndObserve() {
        MeowFlow.GetDataApi<Person>(this) { // You must define type of API response.
            viewModel.callApi()
        }.apply {
            errorHandlerType = MeowFlow.ErrorHandlerType.TOAST
            progressBarInterface = binding.progressbar
        }.observeForIndex(viewModel.eventLiveData, viewModel.listLiveData)
        
        // Optional - call safeObserve function for observe changes of liveData safely.
        viewModel.customLiveData.safeObserve(this) {
            // Access the value of liveData with it parameter.
        }
    }
}
```

`MeowFlow` is a helper class that observes `eventLiveData` and it handles errors from API automatically. You can set error handling with `errorHandlerType`. Supported types : `TOAST` , `SNACKBAR` , `EMPTY_STATE`.
 For example, when `errorHandlerType` is `Toast` errors has been shown in toast form. See [strings_error.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Framework/src/main/res/values/strings_error.xml) to edit error messages.
  
#### Show API response into `RecyclerView`

`item_person.xml` describes the layout of each row of list and you can set properties with `DataBinding` structure. Define layout like this :

```xml
<layout>
    <data>
        <variable
            name="model"
            type="Person" />           
    </data> 

    <LinearLayout>
        <TextView  android:text="@{model.alias}" />
    </LinearLayout>
</layout>  
```

We suggest you to use `MeowAdapter`. Let's take a look at this sample :

```kotlin
class PersonAdapter : MeowAdapter<Model, ViewHolder>(Person.DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeowViewHolder(binding.root) { position, model ->
            binding.let {
                it.setVariable(BR.model, model)
                it.executePendingBindings()
            }
        }
    }
}  
```

finally bind adapter to `RecyclerView`.

```kotlin  
class PersonIndexActivity : MeowActivity<ActivitySampleIndexBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        //...
        binding.recyclerView.adapter = PersonAdapter()
    }
}
```  
Now you have a activity that connect to **REST API** and parse the response (if the response code is HttpCode.OK (200)) and it shows items into a `RecyclerView` as a list.
Above sample can be used for other types of REST API patterns/flows (such as `Detail` ,`Form`). for more details see [API Package](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/api) in `Sample` module.

## 🎨 Material Components

### Alerts

You can show Alert Dialog with `alert()` function in `MeowActivity/MeowFragment`.

```kotlin
fun testAlert() {
    alert()
        .setTitle(R.string.alert_title)
        .setMessage(R.string.alert_message) 
        .setPositiveButton(R.string.ok) { d, _ ->
            toastL(R.string.alerts_warn_ok_clicked)
            d.dismiss()
        }
        .setNegativeButton(R.string.cancel) { d, _ ->  
            toastL(R.string.alerts_warn_cancel_clicked)
            d.dismiss()
        }.show()
}
```

### Loading Alert
  
![](https://raw.githubusercontent.com/oneHamidreza/Meow-Framework-MVVM/master/Resources/img_material_loading_alert.png)
  
A Dialog with `MeowLoadingView` for showing progress bar with text into Dialog.

```kotlin  
loadingAlert(R.string.loading_title_custom).show()  
```

Learn more about it at [AlertsFragment.kt](https://github.com/oneHamidreza/Meow-Framework-MVVM/masterhttps://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/alert/AlertsFragment.kt).

### Button

There are some customized styles related to `Material Button`.

|Style|Usage|
|---|:---:|
|`Meow.Button`|Regular Button with `primary` background color|
|`Meow.Button.Outlined`|Outlined Button with `transparent` background color and `stroke_color.xml` outline color|
|`Meow.Button.Flat`|Flat Button with `transparent` background color|
|`Meow.Button.Unelevated`|Regular Button with `primary` background color with `0dp` elevation|
|`Meow.Button.IconOnly`|A Meow Button that shows icon only |
|`Meow.Button.IconOnly`|A Meow Button that shows icon only |

Use it like this in XML Layout :

```xml
<LinearLayout>
    <Button
        style="@style/Meow.Button"
        android:text="SomeRegularButton" />
    <Button
        style="@style/Meow.Button"
        android:textColor="?colorOnSecondaryVariant"
        android:text="SomeCustomizedButton"
        app:backgroundTint="?colorSecondaryVariant" />
</LinearLayout>
```

Learn more about it at [Material Card Component](https://material.io/components/android/catalog/cards/) and [fragment_cards.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/masterhttps://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/cards/res/layout/fragment_cards.xml).


### CardView

There are some customized styles related to `Material CardView`.

|Style|Usage|
|---|:---:|
|`Meow.CardView`|Regular Card with `surface` background color|
|`Meow.CardView.Outlined`|Outlined Card with `surface` background color and `stroke_color.xml` outline color|

Use it like this in XML Layout :

```xml
<com.google.android.material.card.MaterialCardView
    style="@style/Meow.CardView"
    app:contentPadding="16dp" >
        <!-- Place your views here -->
    </com.google.android.material.card.MaterialCardView>
```

Learn more about it at [Material Card Component](https://material.io/components/android/catalog/cards/) and [fragment_cards.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/masterhttps://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/cards/res/layout/fragment_cards.xml).

### Checkbox

There are some customized styles related to `Material CheckBox`.

|Style|Usage|
|---|:---:|
|`Meow.Checkbox`|Checkbox with `accent_color` button tint|
|`Meow.Checkbox.Primary`|Checkbox with `primary` button tint|
|`Meow.Checkbox.Secondary`|Checkbox with `secondary` button tint|
|`Meow.Checkbox.OnPrimary`|Checkbox with `onPrimary` button tint & `textColor`|
|`Meow.Checkbox.OnSecondary`|Checkbox with `onSecondary` button tint & `textColor`|
  
Use it like this in XML Layout :

```xml
<com.google.android.material.checkbox.MaterialCheckBox
    style="@style/Meow.Checkbox"
    android:text="@string/checkbox_text" />  
```

Learn more about it at [Material Checkbox Component](https://material.io/develop/android/components/checkbox/) and [fragment_checkboxes.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/masterhttps://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/checkboxes/res/layout/fragment_checkboxes.xml).

### Floating Action Button

Use it like this in XML Layout :
```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content" >
    
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        style="@style/Meow.RecyclerView.Linear"
        app:meow_items="@{viewModel.listLiveData}" />
        
    <com.google.android.material.floatingactionbutton.FloatingActionButton
        style="@style/Meow.FloatingActionButton"
        android:onClick="@{viewModel::onClickedFab}"
        app:icon="@drawable/ic_add"
        app:layout_anchor="@id/recyclerView"
        app:layout_anchorGravity="bottom|center_horizontal" />
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

Learn more about it at [Material Floating Action Button Component](https://material.io/develop/android/components/floating-action-button/) and [fragment_fab_simple.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/masterhttps://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/fab/simple/res/layout/fragment_fab_simple.xml).
  
### Extended Floating Action Button

A FAB that supports `android:text` property.
Use it like this in XML Layout :

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    style="@style/Meow.FloatingActionButton"
    android:onClick="@{viewModel::onClickedFab}"
    app:icon="@drawable/ic_add"
    app:layout_anchor="@id/recyclerView"
    app:layout_anchorGravity="bottom|center_horizontal" />
```

Learn more about it at [Material Extended Floating Action Button Component](https://material.io/develop/android/components/extended-floating-action-button/) and [fragment_fab_simple.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/fab/extended/res/layout/fragment_fab_extended.xml).

### Radio Group  

There are some customized styles related to `Material Radio Group`.

|Style|Usage|
|---|:---:|
|`Meow.RadioGroup.Horizontal`|RadioGroup with Horizontal Radio Buttons|
|`Meow.RadioGroup.Vertical`|RadioGroup with Vertical Radio Buttons|
  
### Radio Button

There are some customized styles related to `Material Radio Button`.

|Style|Usage|
|---|:---:|
|`Meow.RadioButton.Vertical`|Vertical RadioButton with `accent_color` button tint|
|`Meow.RadioButton.Horizontal`|Horizontal RadioButton with `accent_color` button tint|
|`Meow.RadioButton.Vertical.Primary`|Vertical RadioButton with `primary` button tint|
|`Meow.RadioButton.Horizontal.Primary`|Horizontal RadioButton with `primary` button tint|
|`Meow.RadioButton.Vertical.Secondary`|Vertical RadioButton with `secondary` button tint|
|`Meow.RadioButton.Horizontal.Secondary`|Horizontal RadioButton with `secondary` button tint|
|`Meow.RadioButton.Vertical.OnPrimary`|Vertical RadioButton with `onPrimary` button tint & `textColor`|
|`Meow.RadioButton.Horizontal.OnPrimary`|Horizontal RadioButton with `onPrimary` button tint & `textColor`|
|`Meow.RadioButton.Vertical.OnSecondary`|Vertical RadioButton with `onSecondary` button tint & `textColor`|
|`Meow.RadioButton.Horizontal.OnSecondary`|Horizontal RadioButton with `onSecondary` button tint & `textColor`|

Use it like this in XML Layout :
```xml
<RadioGroup style="@style/Meow.RadioGroup.Vertical">
    <com.google.android.material.radiobutton.MaterialRadioButton
        style="@style/Meow.RadioButton.Vertical.Primary"
        android:text="@string/radio_buttons_option_a" />
             
    <com.google.android.material.radiobutton.MaterialRadioButton
        style="@style/Meow.RadioButton.Vertical.Primary"
        android:text="@string/radio_buttons_option_b" />
</RadioGroup>
```

Learn more about it at [Material Radio Button Component](https://material.io/develop/android/components/radiobutton/) and [fragment_radio_buttons.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/masterhttps://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/radiobuttons/res/layout/fragment_radio_buttons.xml).
  
### Snack Bars

You can show Snack Bars with `snackL()` or `snackS()` functions in `MeowActivity/MeowFragment`.

```kotlin
fun testSnackbars(){
    // Shows Snack Bars with LENGTH_SHORT.  
    snackS(R.string.snackbars_message)
    // Shows Snack Bars with LENGTH_LONG.
    snackL(R.string.snackbars_message)
    // Shows Snack Bars with LENGTH_INDEFINITE.
    snackI(R.string.snackbars_message)
      
    // Shows Snack Bars with LENGTH_LONG with action button.
    snackL(    
        message = R.string.snackbars_message,    
        resActionText = R.string.snackbars_action,
        
        // Optional - if you want to set custom textAppearances to message and action, set this attributes.
        messageTextAppearanceId = R.style.textAppearance_Snack_Message,
        actionTextAppearanceId = R.style.textAppearance_Snack_Action
    ) {
        // Callback for action button click
    }    
}
```  
Learn more about it at [SnackBarsFragment](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/snackbars/SnackBarsFragment.kt).
  
### Switch

There are some customized styles related to `Material Switch`.

|Style|Usage|
|---|:---:|
|`Meow.Switch`|Switch with `accent_color` button tint|
|`Meow.Switch.Primary`|Switch with `primary` button tint|
|`Meow.Switch.Secondary`|Switch with `secondary` button tint|
|`Meow.Switch.OnPrimary`|Switch with `onPrimary` button tint & `textColor`|
|`Meow.Switch.OnSecondary`|Switch with `onSecondary` button tint & `textColor`|
  
Use it like this in XML Layout :

```xml
<com.google.android.material.switchmaterial.SwitchMaterial
    style="@style/Meow.Switch"
    android:text="@string/switch_text" />
```

Learn more about it at [Material Switch Component](https://material.io/develop/android/components/switch/) and [fragment_switches.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/switches/res/layout/fragment_switches.xml).
  
### TabLayout + ViewPager2  

If you want to show contents into a ViewPager, we recommend to use `ViewPager2`. `TabLayout` is the indicator of ViewPager state. Follow below steps to have a View with Swipe Gesture.

#### 1. Define XML layout like this :

```xml
<layout>
    <data />
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        
        <com.google.android.material.tabs.TabLayout
            android:id="@+id/tabLayout"
            style="@style/Meow.TabLayout.Surface" />
            
        <androidx.viewpager2.widget.ViewPager2
            android:id="@+id/viewpager"
            style="@style/Meow.ViewPager" />    
    
    </LinearLayout>  
</layout>  
```

#### 2. Create Custom Pager Adapter extends `MeowPagerAdapter`

```kotlin
class MyPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : MeowPagerAdapter(fragmentManager, lifecycle) {
        // Replace this with the array of Fragments that you want to show into ViewPager.
        private val fragmentArray = Array<Fragment>(3) { ChildFragment.newInstance(it) }
        override fun getFragments() = fragmentArray
}
```  
  
#### 3. Bind Adapter to `ViewPager2` & Attach `TabLayout` to `ViewPager2`

```kotlin
fun onCreate(savedInstanceState: Bundle?) {
    // ...
    binding.apply {
        binding.viewPager.adapter = MyPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = "Tab Title #" + (position + 1)
        }.attach()
        
        // Optional - If you want to show Material Badge on TabLayout.
        tabLayout.getTabAt(0)?.orCreateBadge?.apply {
            isVisible = true
            number = 10
        }
    }
}  
```

Now you have `ViewPager2` + `TabLayout` in an Activity/Fragment.

There are some customized styles related to `Material TabLayout`.

|Style|Usage|
|---|:---:|
|`Meow.TabLayout.Surface`|TabLayout with `surface` background color|
|`Meow.TabLayout.Primary`|TabLayout with `primary` background color|
|`Meow.TabLayout.Secondary`|TabLayout with `primary` background color|
|`Meow.TabLayout.PrimarySurface`|TabLayout with `primary` background color in DAY mode and `surface` background color in NIGHT Mode|

Learn more about it at [Material TabLayout Component](https://material.io/develop/android/components/tab-layout/).
  
### TextView

Use it like this in XML Layout :  

```xml
<TextView
    style="@style/Meow.TextView"
    android:text="@string/some_text"
    android:textAppearance="?textAppearanceBody1"
    android:textColor="@color/on_background_high" />
```

Learn more about it at [fragment_textviews.xml](https://github.com/oneHamidreza/Meow-Framework-MVVM/blob/master/Sample/src/main/kotlin/sample/ui/material/textviews/res/layout/fragment_textviews.xml).

Colors for texts based on Material Colors contain `EMPHASIS_HIGH` , `EMPHASIS_MEDIUM` , `DISABLED`.

|Color|Value|
|---|:---:|
|`@color/on_background_high`| `onBackground` color with %87 transparency|
|`@color/on_background_medium`| `onBackground` color with %60 transparency|
|`@color/on_background_disabled`| `onBackground` color with %38 transparency|
|`@color/on_surface_high`| `onSurface` color with %87 transparency|
|`@color/on_surface_medium`| `onSurface` color with %60 transparency|
|`@color/on_surface_disabled`| `onSurface` color with %38 transparency|
|`@color/on_primary_high`| `onPrimary` color with %87 transparency|
|`@color/on_primary_medium`| `onPrimary` color with %60 transparency|
|`@color/on_primary_disabled`| `onPrimary` color with %38 transparency|
|`@color/on_secondary_high`| `onSecondary` color with %87 transparency|
|`@color/on_secondary_medium`| `onSecondary` color with %60 transparency|
|`@color/on_secondary_disabled`| `onSecondary` color with %38 transparency|

### Top App Bar using `Material Toolbar`  

Use it like this in XML Layout :

```xml
<layout>
    <data/>
    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true" >    

         <com.google.android.material.appbar.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:liftOnScroll="true" >
            
            <com.google.android.material.appbar.MaterialToolbar
                android:id="@+id/toolbar"
                style="@style/Meow.Toolbar.Surface" />
            
          </com.google.android.material.appbar.AppBarLayout>  
      
    <!-- Main Layout -->
    
    </androidx.coordinatorlayout.widget.CoordinatorLayout>
</layout>  
```

There are some customized styles related to `Material Toolbar`.

|Style|Usage|
|---|:---:|
|`Meow.Toolbar.Surface`|Toolbar with `surface` background color|
|`Meow.Toolbar.PrimarySurface`|Toolbar with `primary` background color in DAY mode and `surface` background color in NIGHT Mode|
|`Meow.Toolbar.Primary`|Toolbar with `primary` background color|
|`Meow.Toolbar.Secondary`|Toolbar with `secondary` background color|
  
Learn more about it at [Material Top App Bars Component](https://material.io/develop/android/components/top-app-bars/).

## 🌌 Meow Custom Widgets

## Contributing

If you want to contribute to this project, just send an email with `Meow Framework Contributing` to oneHamidreza@gmail.com .

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