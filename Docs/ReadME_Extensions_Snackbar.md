## 🍟 Snackbar Extensions

You can show Snackbars with `snackL()` or `snackS()`or `snackI()` in `MeowActivity/MeowFragment`.  See this example :

```kotlin
class MyActivity : MeowActivity<*> {  
    override fun onCreate(savedInstanceState: Bundle?) {
        //...
        // Shows Snackbar with LENGTH_SHORT duration.
        snackS(R.string.snackbars_message)
        
        // Shows Snackbar with LENGTH_LONG duration.
        snackL(R.string.snackbars_message)
        
        // Shows Snackbar with LENGTH_INDEFINITE duration.
        snackI(R.string.snackbars_message)

        // Shows Snackbar with LENGTH_LONG with action button.
        snackL(  
            message = R.string.snackbars_message,  
            resActionText = R.string.snackbars_action,
      
            // Optional - if you want to set custom textApperance to message and action, set this attributes.
            messageTextAppearanceId = R.style.textAppearance_Snack_Message, 
            actionTextAppearanceId = R.style.textAppearance_Snack_Action  
        ) {
             // Callback for action button click.
          }
    }
}
```
