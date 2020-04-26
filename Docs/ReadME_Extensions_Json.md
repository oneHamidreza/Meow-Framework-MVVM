## 💉 Json Extensions

Use `String?.fromJson()` to convert a String to any data   See this example : 

```kotlin
val module = Module("NAME_OF_MODULE", false){
    bindAutoTag<MyViewModel>() with provider {  
        MyViewModel(kodein.direct.instance())  
    }
}
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTUwMjc2NjkxNV19
-->