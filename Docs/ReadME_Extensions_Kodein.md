## 💉 Kodein Extensions

Use `bindAutoTag()` for binding instances into Kodein Modules. it acts like `bind()` but tag value sets automatically by `T::class.java.simpleName`. It is useful for binding `ViewModel` instances. See this example : 

```kotlin
val module = Module("NAME_OF_MODULE", false){
    bindAutoTag<MyViewModel>() with provider {  
        MyViewModel(kodein.direct.instance())  
    }
}
```
