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

fun testJsonObject(){
    val user = User(1,"SomeFirstName","SomeLastName")
    val json = user.toJson()   
    // json = {
    //      "id": 	1,
    //      "first_name": "SomeFirstName",
    //      "last_name": "SomeLastName"
    //  }
    
    // Create a model from JSON string.
    val modelFromJson = json.fromJson<User>()
    println(modelFromJson.firstName)
}

fun testJsonArray(){
    val userList = listof(User(1,"Ham","SomeLastName"),
    User(1,"SomeFirstName","SomeLastName")
    )
    val json = user.toJson()   
    // json = {
    //      "id": 	1,
    //      "first_name": "SomeFirstName",
    //      "last_name": "SomeLastName"
    //  }
    
    // Create a model from JSON string.
    val modelFromJson = json.fromJson<User>()
    println(modelFromJson.firstName)
}

```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTIwOTcwNDM5OTIsLTQ0NDYyMTA3NiwtMT
gzNjQ4MzkwNl19
-->