## 📬 Shared Preferences Extensions

We used `in.co.ophio:secure-preferences` library to have a real secured Shared Preferences. 

Use `MeowSharedPreferences` to store data into Android Shared Preferences. See this example : 

```kotlin
class MyActivity : MeowActivity<*> {  
    override fun onCreate(savedInstanceState: Bundle?) {
        //...
        // Initialize an object of MeowSharedPreferences with `Main_Settings_v1` settings name.
        val sp = MeowSharedPreferences(application, "Main_Settings_v1")

        // Example: put a String with `KEY_USERNAME` key and `SomeUserName` value.
        sp.put("KEY_USERNAME", "SomeUserName")

        // Example: get a String with `KEY_USERNAME` key and default value `public_user_default`
        val username = sp.get("KEY_USERNAME", "public_user_default")

        // Example: put a Object with `KEY_STUDENT`.
        sp.put("KEY_STUDENT", Student(1,"oneHamidreza-samdh82",18.5))

        // Example: get a Student with default value.
        val student = sp.get("KEY_STUDENT",  Student(0,"Not-Registered",0))
    }

    // A Model class with `@JsonClass()` & `@Json()` Moshi-Kotlin annotation.
    @JsonClass(generateAdapter = true)
    data class Student(
        @Json(name = "id") var id: Int, 
        @Json(name = "user_name") var username: String,
        @Json(name = "average") var average: Double
    )
}
```
