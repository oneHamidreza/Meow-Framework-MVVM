## Network Extensions

Use `Context?.hasNetwork()` to check device's Network availability by using `Context.CONNECTIVITY_SERVICE`.  See this example : 

```kotlin
class MyFragment : MeowFragment<*>{
    override fun onViewCreated(savedInstanceState: Bundle?) {
        //...
        val allowCallApi = hasNetwork() 
        if (allowCallApi) {
            // Network (Wifi/Data) is active but maybe haven't Internet. so Call Api here.
        } else { 
            // No Network show error or fetch data from your local repository.
        }
    }
}
```