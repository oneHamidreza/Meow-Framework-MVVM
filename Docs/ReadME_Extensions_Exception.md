## 🤡 Exception Extensions 

Use `avoidException`  instead of `try{} catch()` to can handle when an exception has been occurred in your app. Just set `onException` block in your `MeowController`  then you can access the happen exceptions (Only Non-Fatal Error).

```kotlin
// MeowController() ...
onException = { exception ->
     // Log to Crashlytic or Fabric or any other Crash Management System.
}
```

### Migrating from `try{} catch()` into `avoidException{}`

Consider that usage of `avoidException` is easier than `try{} catch()`. See this examples :
  
#### 1. Simple Exception Handling
 
Legacy  `try{} catch()` :

```kotlin
try { 
    someCallFunction()
} catch(e:Exception) {
    e.printStackTrace()
}
```

Above codes with `avoidException` will be like this :  

```kotlin
avoidException {
    someCallFunction()
}
```

#### 2. Advanced Exception Handling
 
Legacy  `try{} catch() finally{}` :

```kotlin
try { 
    someCallFunction()
} catch(e:Exception) {
    e.printStackTrace()
    someErrorFunction()
} finally {
    someFinallyFunction()
}
```

Above codes with `avoidException` will be like this :

```kotlin
avoidException (
    tryBlock = {
        someCallFunction()
    },
    exceptionBlock = {
        someErrorFunction()
    },
    finallyBlock = {
        someFinallyFunction()
    }
)
```

#### 3. Return Some Value + Exception Handling
 
Legacy  `val v = try{} catch()` : 

```kotlin
val someValue = try {
    someReturnFunction()
} catch(e:Exception) {
    e.printStackTrace()
    null // or `someValueForErrors`
}
```
Above codes with `avoidException` will be like this :
```kotlin
val someValue = avoidException { 
    someReturnFunction()
} // or  ?: `someValueForErrors`
```