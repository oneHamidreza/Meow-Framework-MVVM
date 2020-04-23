## JSON Extensions 📑

Use `String?.fromJson()`  to convert JSON Object string into a specified class instance. Example : 
```kotlin
data class Model (
    @Json(name = "id") var id: String,
    @Json(name = "name") var name: String
)

val jsonString = """
{
    "id":1,
    "name":"someName"
}
"""

val model = jsonString.fromJson<Model>()
```

Use `String?.fromJsonList()`  to convert JSON Array string into a specified class list. Example : 
```kotlin
val jsonString = """
[
    {
        "id":1,
        "name":"someName-01"
    },
    {
        "id":2,
        "name":"someName-02"
    },
]
"""

val modelList = jsonString.fromJsonList<Model>()
```

Use `Any?.toJson()`  to convert an Object to JSON Object String. Example : 
```kotlin
Model(id = 100, name = "SomeName").toJson() // Return "{"id":100,"name":"SomeName"}"
```