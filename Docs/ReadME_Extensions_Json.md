## 💉 Json Extensions

Use `String?.fromJson()` to convert a String to any Data Model. See this example : 

```kotlin
// Use `Moshi.JsonClass` and `Moshi.Json`
@JsonClass(generateAdapter = true)  
data class User(  
    @Json(name = "id") var id: String? = null,  
    @Json(name = "first_name") var firstName: String? = null,  
    @Json(name = "last_name") var lastName: String? = null  
)

val module = Module("NAME_OF_MODULE", false){
    bindAutoTag<MyViewModel>() with provider {  
        MyViewModel(kodein.direct.instance())  
    }
}
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTgwNjQ0OTc1Nl19
-->