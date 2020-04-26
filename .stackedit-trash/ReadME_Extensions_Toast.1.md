
## 🍞 Toast Extensions 

You can show toast with `toastL()` or `toastS()` in `MeowActivity/MeowFragment`.  See this example :

```kotlin
class MyFragment : MeowFragment<*> {  
    override fun onViewCreate(savedInstanceState: Bundle?) {
        //...
        // Shows Toast with LENGTH_SHORT duration.
        toastS(R.string.snackbars_message)
        // Shows Toast with LENGTH_LONG duration.
        toastL(R.string.snackbars_message)
    }
}
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTU0ODE2NjE0Ml19
-->