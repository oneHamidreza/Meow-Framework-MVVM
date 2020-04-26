## 🧩 Android Extensions

Use `LiveData<T>?.safeObserve()` when you want to observe any Live Data safely. See this example : 

```kotlin
// The value of lifecycleOwner is `this` in a MeowActivity/MeowFragment 
stringLiveData.safeOvserve(this){
	toast(it)
}
```

Use `instanceViewModel<T>()` lazy function  in **MeowActivity/MeowFragment** to have an instance of any ViewModel. See this example : 

```kotlin
class MyActivity : MeowActivity<*> {
    val viewModel by instanceViewModel<MyViewModel>()
} // You must provide instance of ViewModel into your Kodein module.
```

Use `sdkNeed(buildSdkInt)` from anywhere to run a block only in specified SDK version or higher than it. See this example : 

```kotlin
sdkNeed(23) {
    // Some runnable actions that need Build.VERSION.SDK_INT >= 23.
}
```

Use `MenuItem.setTypefaceResId(context,id)`  to set Typeface on menu items. See this example : 

```kotlin
// You must define your fonts in `res/font` folder.
menuItem.setTypefaceResId(R.font.my_regular)
```

