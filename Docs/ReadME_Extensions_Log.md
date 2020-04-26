## 📃 Log Extensions

Set `isDebugMode` in `MeowController` in Application's `onCreate()` to Use `log{d,e,i,v,w}()`  functions.

```kotlin
// MeowController() ...
isDebugMode = BuildConfig.DEBUG // By setting this property, logs are enabled only in DEBUG mode. 
```

Tags is append on end with by `Thread.stackThread[5].fileName_`. See this  example : 

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

Above example can be used for this types :

 - `logE()`
 - `logI()`
 - `logV()`
 - `logW()`

 
