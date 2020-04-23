## Currency Extensions 💵

First set Currency mode in `MeowController` App.
```kotlin
class App : MeowApp() {
    override fun onCreate() {
        MeowController().apply {         
            // ...
            currency = MeowCurrency.Toman // or USD or Rial
        }.bindApp(this)
    }
}
```

Use `String?.formatCurrency()` to have a Currency String by `MeowController.currency` value. Example : 

```kotlin
"124569874.00".formatCurrency() // Return "124,569,874" 
```

Use `Context?.createCurrency(string)` to have a Currency String with unit text. Example : 

```kotlin
class MyActivity : MeowActivity<*> {
    override fun onCreate(){
        // ...
        // A TextView's text = "124,569,874 تومان" when meow.controller.currency is Currency.Toman
        binding.tvPrice.text = "124569874.00".formatCurrency() 
    }
}
```