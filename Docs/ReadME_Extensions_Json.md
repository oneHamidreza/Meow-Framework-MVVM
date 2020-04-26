## 💉 Json Extensions

Use `String?.fromJson()` to convert a String to any Data Model with .
Use `Any?.toJson()` to convert a any Data Model to a Json String .
 See this example : 

```kotlin
// Use `Moshi.JsonClass` and `Moshi.Json` annotations.
@JsonClass(generateAdapter = true)  
data class User(  
    @Json(name = "id") var id: String? = null,  
    @Json(name = "first_name") var firstName: String? = null,  
    @Json(name = "last_name") var lastName: String? = null  
)

fu

val user = 
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTUxMDU0NzAxNV19
-->