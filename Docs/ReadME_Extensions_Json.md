## 💉 JSON Extensions

Use `String?.fromJson()` to convert a String to any Data Model.

Use `Any?.toJson()` to convert any Data Model to a JSON String.

 See this example : 

```kotlin
// Use `Moshi.JsonClass` and `Moshi.Json` annotations.
@JsonClass(generateAdapter = true)  
data class User(  
    @Json(name = "id") var id: String? = null,  
    @Json(name = "first_name") var firstName: String? = null,  
    @Json(name = "last_name") var lastName: String? = null  
)

fun testJson(){
    val user = User(1,"SomeFirstName 

}

```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE5MjAzNzk5MDYsLTE4MzY0ODM5MDZdfQ
==
-->