# Meow Framework MVVM Android/Kotlin

A Framework that simplify developing MVVM architecture and Material Design in Android with Kotlin language including useful Extensions and Sample Application. Also this Framework has some tools for Retrofit and OKHttp and Coroutine for calling REST API actions. 

![](/Resources/img_github.png)

[ ![Download](https://api.bintray.com/packages/infinitydesign/meow/Meow-Framework-MVVM/images/download.svg?version=0.2.4-alpha) ](https://bintray.com/infinitydesign/meow/Meow-Framework-MVVM/0.2.4-alpha/link)

## Setup
```groovy
implementation("com.etebarian:meow-framework-mvvm:0.2.4-alpha")
```
After adding library, some of most useful libraries (such as `Androidx AppCompat` , `Coroutine` , `Glide` , `Kodein` , `Kotlinx Serialization` , `Material Components` , `Moshi` , `Navigation Components` , `Retrofit` ) will be added in your app. So you don't need to add this libraries manually.
> Enable Androidx in `gradle.properties` 
>```properties
>android.useAndroidX=true
>android.enableJetifier=true
>```
>Update `build.gradle` like below : 
>```groovy
>android{
>    defaultConfig {    
>        multiDexEnabled true  
>        vectorDrawables.useSupportLibrary true
>    }
>    compileOptions {  
>        sourceCompatibility JavaVersion.VERSION_1_8  
>        targetCompatibility JavaVersion.VERSION_1_8 
>    }
>    dataBinding {  
>        enabled true  
>   }
>}
>```

## Getting Started
### Initialize
Create your application class that extends `MeowApp` and set it in `AndroidManifest.xml`. Dependency Injection in MVVM architecture is necessary so we use `Kodein-DI` Framework. 
You need to define `appModule` for ViewModels.
```kotlin
class App : MeowApp() {

   // Create a kodein module
   val appModule = Module("App Module", false){
       // Provide object of SomeOfClass(such as View Models) in Kodein with bind() function 
       // bind() from singleton { SomeOfClass(instance()) } 
   }
    
    override val kodein = Kodein.lazy {  
        bind() from singleton { kodein.direct }  
        bind() from singleton { this@App }  
        import(androidXModule(this@App))  
        import(meowModule)  
        import(appModule)  
   }
}
```
### Meow Controller 🐱
`MeowController` is a class that controls some of configurations in your app like language and theme. Configuration updates are in Real time . Dynamic Localization & Day/Night Theme configurations must be set here with `MeowController`.
  If you use `avoidException` in your app, this class can controls Exception Handlers with `onException` property. 
```Kotlin
class App : MeowApp() { 
    // Layout Direction would be set automatically. 
    // (Example: "en": LayoutDirection.LTR  "fa": LayoutDirection.RTL)
    override fun getLanguage(context: Context?) = "en" // or any language such as ("fa","fr","ar",etc.)
    
    // Sample app theme is setted by Android System Light/Dark (Day/Night) mode
    override fun getTheme(context: Context?) = if (context.isNightModeFromSettings()) MeowController.Theme.NIGHT else  MeowController.Theme.DAY
    
    override fun onCreate() {
        super.onCreate()
         MeowController().apply {         
            isDebugMode = BuildConfig.DEBUG
            language = getLanguage(this@App) 
            theme = getTheme(this@App)
        }.bindApp(this)
    }
}
```
You can update language and theme from UI Thread by using `MeowController` global instance.
```kotlin
import meow.controller

controller.updateLanguage(meowActivity, string)
controller.updateTheme(meowActivity, theme)
```
###  MVVM Architecture
MVVM is Model-View-ViewModel that we define it in Android App as Data Model - View (Activity, Fragment, DialogFragment, BottomSheetDialogFragment) - MeowViewModel.
 
Follow below steps to be have one activity with MVVM Architecture.
#### 1.  Create your ViewModel extends `MeowViewModel` .
```kotlin
class MainViewModel(app: App): MeowViewModel(app)
```
#### 2.  Provide your View Model in appModule in `App`.
```kotlin
val appModule = Module("App Module", false){
    bindAutoTag<MainViewModel>() with provider {  
        MainViewModel(kodein.direct.instance())  
    }
}
```
>`bindAutoTag()` is a function from `meow.util.*`
#### 3.  Create XML layout with `DataBinding` structure.
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
        <variable  
            name="viewModel"  
            type="MainViewModel" />
	</data>
	<LinearLayout ... /> <!-- or another View -->
</layout>
```

#### 4.  Create Activity/Fragment  + `DataBinding`  + `ViewModel` extends `MeowActivity/MeowFragment`.
‍‍‍`MainActivity` is a sample activity that needs Kodein Dependency Injection and ViewDataBinding and View Model. See this sample  :
```kotlin
class MainActivity : MeowActivity<ActivityMainBinding>() {
    // ActivityMainBinding is generated by Androidx Lifecycle DataBinding Utils
  
    private val viewModel: MainViewModel by instanceViewModel()

    override fun layoutId() = R.layout.activity_main
      
    override fun initViewModel() {// Set View Model in binding 
        binding.viewModel = viewModel
    }
}
```
Now you have one Activity with MVVM architecture. 

###  Material Design
Update App Theme in `styles.xml` with `DayNight` Material Theme. More details are at [Official Material Design Site](https://material.io/develop/android/docs/getting-started/).
```xml
<style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
   <!-- Original AppCompat attributes. -->  
   <!-- Define colors in colors.xml -->
   <item name="colorPrimary">@color/primary</item>  
   <item name="colorSecondary">@color/secondary</item>  
   <item name="android:colorBackground">@color/background</item>  
   <item name="colorError">@color/error_primary</item>  
  
   <!-- New MaterialComponents attributes. -->  
   <item name="colorPrimaryVariant">@color/primary_variant</item>  
   <item name="colorSecondaryVariant">@color/secondary_variant</item>  
   <item name="colorSurface">@color/surface</item>  
   <item name="colorOnPrimary">@color/on_primary</item>  
   <item name="colorOnSecondary">@color/on_secondary</item>  
   <item name="colorOnBackground">@color/on_background</item>  
   <item name="colorOnError">@color/on_error_primary</item>  
   <item name="colorOnSurface">@color/on_surface</item>  
   <item name="scrimBackground">@color/mtrl_scrim_color</item>
</style>
```
### Accessing views with DataBinding
You can access views like this code :
```xml
<com.google.android.material.appbar.MaterialToolbar  
    android:id="@+id/toolbar"  
    style="@style/Meow.Toolbar" />
```
```kotlin
class MainActivity: MeowActivity<ActivityMainBinding>(){
    override fun layoutId() = R.layout.activity_main 
    override fun onCreate(savedInstanceState: Bundle?) {
        // ...
        binding.toolbar.title = "custom_title"
    }
}
```
## Retrofit + OKHttp + Coroutine + Moshi
Meow Framework provides for you to call Server Api from Android App with `Retrofit`. Creating client connections will be with `OKHttp` and `Moshi` help us to serialize json responses. We replaced `RxJava` with `Coroutine` for multi thread handling.

###  Create Api extends `MeowApi`
```kotlin
class AppApi(
    var app: App,
    baseUrl : String = "http://api-url.any/api/v1/"
): MeowApi(baseUrl)
```
### Sample Index
For Example, server give this JSON response when call `persons` with get method : 
```json
[
   {
		"id":1,
		"username":"oneHamidreza",
		"alias":"Hamidreza Etebarian"
    },
    {
		"id":2,
		"username":"samhd82",
		"alias":"Ali Modares"
    }
]
```
#### Data Model
Create  data class for JSON response that use Moshi `@Json` annotation.
```kotlin
data class Person(
   @Json(name = "id") var id: Int = 0,
   @Json(name = "username") var username: String? = null,
   @Json(name = "alias") var alias: String? = null
){
   // RecyclerView List Adapter requires DiffCallBack.
   class DiffCallback : DiffUtil.ItemCallback<CatBreed>() {  
        override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id  
        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
    }
}
```
#### Retrofit API Interface
Define one interface containing Server API actions. We are using `Coroutine` so `suspend` prefix is necessary.
```kotlin
interface PersonApi {
    @GET("persons")
    suspend fun getPersonIndex(): List<Person>
}
```
#### Call API action from ViewModel 
Use `safeCallApi` function for calling API actions. update your ViewModel class like this :  
```kotlin
class PersonIndexViewModel(app:App) : MeowViewModel(app) {
    // Define LiveData variables
    var eventLiveData = MutableLiveData<MeowEvent<*>>()  
    var listLiveData = MutableLiveData<List<Person>>()
    var customLiveData = MutableLiveData<String>()

    fun callApi() {
       safeCallApi(
           liveData = eventLiveData,  
           apiAction = { AppApi(app).createServiceByAdapter<PersonApi>().getPersonIndex() }  
       ) { _, it ->  
	         // If connection was Success, this line will be run.
	         // Otherwise MeowEvent.Api.Error will be posted into eventLiveData
	         // You can observe it manually or use MeowFlow  
             listLiveData.postValue(it)  
         }
    }
}
```
#### XML Layout
Create `activity_sample_index.xml` containing `RecyclerView` for showing items.
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
        android:layout_height="match_parent">  
  
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
#### MeowActivty/MeowFragment + MeowFlow
Use `MeowFlow` for handling events from ViewModel.
 ```kotlin
class SampleIndexActivity : MeowActivity<ActivitySampleIndexBinding>(){
    //...
    override fun initViewModel() {  
        binding.viewModel = viewModel  
        callApiAndObserve()  
    }  
    
    private fun callApiAndObserve() {  
        MeowFlow.GetDataApi<Person>(this) {  
            viewModel.callApi()
        }.apply {  
            errorHandlerType = MeowFlow.ErrorHandlerType.TOAST  	
            progressBarInterface = binding.progressbar      
        }.observeForIndex(viewModel.eventLiveData, viewModel.listLiveData)
        
        // Optional - call safeObserve function for observe changes of liveData safely.
        viewModel.customLiveData.safeObserve(this) {
            // Access the value of liveData with it paramater 
        }
    }
}
 ```
`MeowFlow` is a helper class that observe `eventLiveData` and it handles errors from API automatically. You can set error handling with `errorHandlerType`. Supported types : `TOAST` , `SNACKBAR` , `EMPTY_STATE`  . For example, when `errorHandlerType` is `Toast` errors has been shown in toast form. See [strings_error.xml](/meow/src/main/res/values/strings_error.xml) to edit error messages.

#### Show API response into `RecyclerView`
`item_person.xml` describe the layout of each row of list and you can set properties with `DataBinding` structure. Define layout like this :
```xml
<layout>
   <data>
        <variable  
            name="model"  
            type="Person" />
   </data>
   <LinearLayout>
      <TextView
         android:text="@{model.alias}" />
   </LinearLayout>
<layout>
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
And finally bind adapter to `RecyclerView`. 
```kotlin
class PersonIndexActivity : MeowActivity<ActivitySampleIndexBinding>(){
    override fun onCreate(savedInstanceState: Bundle?) {
        //...
        binding.recyclerView.adapter = PersonAdapter() 
    }
}
```
Now you have one activity that connect to **REST API** and parse the response (if the response code is HttpCode.OK (200)) and it show items into a `RecyclerView` as a list.
Above sample can be used for other types of REST API actions (such as `Detail` ,`Form` ). for more details see [API Package](/Sample/src/main/kotlin/sample/ui/api) in `Sample` module.   

## Material Components
### Alerts
You can show Alert Dialog with `alert()` function in `MeowActivity/MeowFragment`
```kotlin
alert()  
    .setTitle(R.string.alerts_simple_with_listener_title)  
    .setMessage(R.string.alerts_simple_with_listener_message)  
    .setPositiveButton(R.string.ok) { d, _ ->  
        toastL(R.string.alerts_warn_ok_clicked)  
        d.dismiss()  
    }  
    .setNegativeButton(R.string.cancel) { d, _ ->  
        toastL(R.string.alerts_warn_cancel_clicked)  
        d.dismiss()  
    }  
    .show()
```
### Loading Alert

![](/Resources/img_material_loading_alert.png)

A Dialog with `MeowLoadingView` for showing progress bar with text into Dialog.   
```kotlin
loadingAlert(R.string.loading_title_custom).show()
```
Learn more about it at [AlertsFragment](/sample/ui/material/alert/AlertsFragment.kt)
### CardView
There are some customized styles related to `Material CardView`. 
|Style|Usage
|----------|:-------------:|
|`Meow.CardView`|Regular Card with `surface` background color 
|`Meow.CardView.Outlined`|Outlined Card with `surface` background color and `stroke_color.xml` outline color

Use it like this in XML Layout : 
```xml
<com.google.android.material.card.MaterialCardView
    style="@style/Meow.CardView"  
    app:contentPadding="16dp" >
   <!-- Place your views here -->
</com.google.android.material.card.MaterialCardView>
```    
Learn more about it at [Material Card Component](https://material.io/components/android/catalog/cards/) and [fragment_cards.xml](/sample/ui/material/cards/res/layout/fragment_cards.xml).
### Checkbox
There are some customized styles related to `Material CheckBox`. 
|Style|Usage
|----------|:-------------:|
|`Meow.Checkbox`|Checkbox with `accent_color` button tint 
|`Meow.Checkbox.Primary`|Checkbox with `primary` button tint
|`Meow.Checkbox.Secondary`|Checkbox with `secondary` button tint
|`Meow.Checkbox.OnPrimary`|Checkbox with `onPrimary` button tint & `textColor`
|`Meow.Checkbox.OnSecondary`|Checkbox with `onSecondary` button tint & `textColor`

Use it like this in XML Layout :
```xml  
<com.google.android.material.checkbox.MaterialCheckBox  
    style="@style/Meow.Checkbox"  
    android:text="@string/checkbox_text" />
```    
Learn more about it at [Material Checkbox Component](https://material.io/develop/android/components/checkbox/) and [fragment_checkboxes.xml](/sample/ui/material/checkboxes/res/layout/fragment_checkboxes.xml).


 
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


