## 💎 Kotlin Extensions

Use `scopeLaunch()` to launch a `Coroutine` Scope + `Job` + `exceptionHandler` properties. Default coroutine context is `Dispatchers.IO`.  See this example :

```kotlin
// Simple launch coroutine from anywhere.
scopeLaunch {
    // Call some functions which need `Dispatchers.IO` scope.
}

// Advanced launch coroutine.
scopeLaunch(
    context = Dispatchers.Main,
    exceptionHandler = CoroutineExceptionHandler{_,e -> e.printStackTrace()},
    job = myJob, // Optional
    start = CoroutineStart.DEFAULT // Optional
){
    // Call some functions which need `Dispatchers.IO` scope.  
}
```
  
Use `ofPair<A,B>(first:A,second:B)` to have a `Pair` instance of `A` type and `B` type .  See this example :

```kotlin
fun testOfPair() {
    val myPair = ofPair(1,"SomeString")
    logD(m = "myPair >> first: ${myPair.first} second: ${myPair.second}")
	// Log into DEBUG level : myPair >> first: 1 second: SomeString
}
```