## 🍟 Snackbar Extensions

You can show Snackbars with `snackL()` or `snackS()`or `snackI()` in `MeowActivity/MeowFragment`.  See this example :

```kotlin
class MyActivity : MeowActivity<*> {  
    override fun onCreate(savedInstanceState: Bundle?) {
        //...
        // Shows Snack Bars with LENGTH_SHORT
        snackS(R.string.snackbars_message)
        // Shows Snack Bars with LENGTH_LONG
        snackL(R.string.snackbars_message)
        // Shows Snack Bars with LENGTH_INDEFINITE
        snackI(R.string.snackbars_message)

        // Shows Snack Bars with LENGTH_LONG with action button
        snackL(  
            message = R.string.snackbars_message,  
            resActionText = R.string.snackbars_action,
      
            // Optional if you want to set custom textApperance to message and action, set this attributes.
            messageTextAppearanceId = R.style.textAppearance_Snack_Message, 
            actionTextAppearanceId = R.style.textAppearance_Snack_Action  
            ) {
                // Callback for action button click 
         }
    }
}
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTg1MjkxNTgzMl19
-->