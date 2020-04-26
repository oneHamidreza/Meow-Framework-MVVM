## 💵 Currency Extensions

First set Currency mode in Application's `MeowController`. Supported currencies are `Rial` , `Toman` , `USD`. 
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

Use `String?.formatCurrency()` to have a Currency String by `meow.controller.currency` mode. See this example : 

```kotlin
// When `meow.controller.currency` is `Toman` or Rial Return "124,569,874" 
// When `meow.controller.currency` is `USD` Return "$124,569,874.00" 
"124569874.00".formatCurrency()
```

Use `Context?.createCurrency(string)` to have a Currency String with unit text. See this example : 

```kotlin
class MyActivity : MeowActivity<*> {
    override fun onCreate(){
        // ...
        // A TextView's text = "124,569,874 ریال" when `meow.controller.currency` is Currency.Rial
        // A TextView's text = "124,569,874 تومان" when `meow.controller.currency` is Currency.Toman
        // A TextView's text = "$124,569,874.00" when `meow.controller.currency` is Currency.USD
        binding.tvPrice.text = createCurrency("124569874.00") 
    }
}
```
