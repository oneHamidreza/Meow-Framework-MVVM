## 🤡 Exception Extensions 

Use `avoidException`  instead of `try{} catch()` to can handle when an exception has been occurred in your app. Just set `onException` block in your `MeowController`  then you can access the happen exceptions.

```kotlin
// MeowController() ...
onException = { exception ->
     // Log to Crashlytic or Fabric or any other Crash Management System.
}
```

### Migrating from `try{} catch()` into `avoidException{}` Samples

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
 
Legacy  `try{} catch()` : 

```kotlin
try { 
    someCallFunction()
} catch(e:Exception) {
    e.printStackTrace()
    someErrorFunction()
} finally {
    someFinallyFuntion()
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
        someFinallyFuntion()
    }
)
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTcyMzYwNzA2NV19
-->