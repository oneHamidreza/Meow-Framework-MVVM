## 📅 Date Extensions

First set Calendar mode in Application's `MeowController`. Supported calendars are `GEORGIAN` , `JALALI` . 
 
```kotlin
class App : MeowApp() {
    override fun onCreate() {
        MeowController().apply {         
            // ...
            calendar = MeowController.Calendar.JALALI // or GEORGIAN
        }.bindApp(this)
    }
}
```

Use `Context?.dateFormatSimple(calendar)` to have a Date String with `Simple Format`. See this example : 

```kotlin
val calendar = Calendar().apply{
    add(Calendar.YEAR,-2)
}
dateFormatSimple(calendar) 
// Returns "2 Years Ago" and when App's Language is Farsi ("fa") : "2 سال قبل"  
```

Use `Context?.dateFormatDetail(calendar)` to have a Date String with `Detail Format`. See this example : 

```kotlin
val calendar = Calendar().apply{
    add(Calendar.DAY_OF_YEAR,-1)
}
dateFormatDetail(calendar)
// Returns "Yesterday 10:10 p.m." and when App's Language is Farsi ("fa") : "دیروز 10:10 " 
```

Use `Context?.dateFormatNormal(calendar)` to have a Date String with `Normal Format`. See this example : 

```kotlin
dateFormatNormal(calendar)
// Return "23 Jan 2020" and when App's Calendar is JALALI : "23 مهر ‌" 1392
```

Use `Context?.dateFormatNormalWithTime(calendar)` to have a Date String  with `Simple Format + clock 12 Hrs`. See this example : 

```kotlin
dateFormatNormalWithTime(calendar)
// Return "23 Jan 2020 10:10 a.m." and when App's Calendar is JALALI : "23 مهر ‌" 1392 10:10 ب.ظ
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTYwODkyMDUxM119
-->