## 💉 JSON Extensions

Use `String?.fromJson()` to convert a String to any Data Model.

Use `Any?.toJson()` to convert any Data Model to a JSON String.

 See this example : 

```kotlin
// Use `Moshi.JsonClass` and `Moshi.Json` annotations.
@JsonClass(generateAdapter = true)  
data class User(  
    @Json(name = "id") var id: Int = 0,  
    @Json(name = "first_name") var firstName: String? = null,  
    @Json(name = "last_name") var lastName: String? = null  
)

fun testJson(){
    val user = User(1,"SomeFirstName","SomeLastName")
    val json = user.toJson()   
    // json = {
    //      "id": 	1,
    //      "first_name": "SomeFirstName",
    //      "last_name": "SomeLastName"
    //  }
    val modelFromJson = json.fromJson<User>()
    
}

```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTg2MDM0NDE5NSwtNDQ0NjIxMDc2LC0xOD
M2NDgzOTA2XX0=
-->