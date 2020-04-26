## 📃 Log Extensions

Set `isDebugMode` in `MeowController` to Use `log{d,e,i,v,w}()`  functions.

```kotlin
// MeowController() ...
isDebugMode = BuildConfig.DEBUG
```

Tags is append on end with by `Thread.stackThread[5].fileName_` Example : 
```kotlin
class Foo {
    fun Bar(){
        // `_Thread.stackThread[5].fileName` = `X` here
		logD("tag","message") // Simple Log.d("X_tag","message")

        logD(m = "message") // Simple Log.D("X","message")
        
        logD("tag","message",SomeException()) // Simple Log.D("X_tag","message",SomeException())
    }
}
```

Above Example can be used for this types :

 - `logE`
 - `logI`
 - `logV`
 - `logW`

 
<!--stackedit_data:
eyJoaXN0b3J5IjpbODM0NTg1MTc5XX0=
-->