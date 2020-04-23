## Android Extensions

Use `LiveData<T>?.safeObserve()` when you want to observe any Live Data. Example : 

```kotlin
// The value of lifecycleOwner is `this` in a MeowActivity/MeowFragment 
stringLiveData.safeOvserve(this){
	toast(it)
}
```

Use `instanceViewModel<T>()` lazy function  in **MeowActivity/MeowFragment** to have an instance of any ViewModel. Example : 

```kotlin
class MyActivity : MeowActivity<*> {
    val viewModel by instanceViewModel<MyViewModel>()
} // You must provide instance of ViewModel into your Kodein module.
```

Use `sdkNeed(buildSdkInt)` function  anywhere to run a block only in specified SDK version or above it. Example : 

```kotlin
sdkNeed(23) {
    // Some runnable actions that need Build.VERSION.SDK_INT >= 23.
}
```

Use `MenuItem.setTypefaceResId(context,id)` function  to set Typeface on menu items. Example : 

```kotlin
menuItem.setTypefaceResId(R.font.my_regular)
```
