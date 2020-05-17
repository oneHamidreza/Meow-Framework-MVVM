## 📇 JSON Extensions

Use `String?.fromJson()` to convert a String to any Data Model.

Use `String?.fromJsonList()` to convert a String to a list of Data Model.

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

fun testJsonObject() {
    val user = User(1,"SomeFirstName","SomeLastName")
    val json = user.toJson()   
    // json = {
    //      "id": 	1,
    //      "first_name": "SomeFirstName",
    //      "last_name": "SomeLastName"
    //  }
    
    // Create a model from JSON Object string.
    val modelFromJson = json.fromJson<User>()
    println(modelFromJson.firstName) // Prints `SomeFirstName`
}

fun testJsonArray(){
    val userList = listOf(
        User(1,"Hamidreza","Etebarian"),
        User(2,"Ali","Modares")
    )
    val json = userList.toJson()   
    // json = [
    //     {
    //          "id": ,
    //          "first_name": "i",
    //          "last_name": "ar"
    //     },
    //
    //     {
    //          "id": 2,
    //          "first_name": "Ali",
    //          "last_name": "Modares"
    //     }
    // ]
    
    // Create a list of models from JSON Array string.
    val modelFromJson = json.fromJsonList<User>()
    println(modelFromJson.size) // Prints `2`
}
```
