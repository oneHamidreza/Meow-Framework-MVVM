
## 🚂 System Extensions

Use `getDeviceModel()` to get model of Device as a String.

Use `Context?.isPackageInstalled(packageName)` to check a package name is installed or not. 

Use `Context?.getIMEI()` to get IMEI of Device as a String.

Use `Context?.getPhoneNumber()` to get Phone Number (if it exists) of Device by sim card information as a String. You'll need to add `android.permission.READ_PHONE_STATE` in `AndroidManifest.xml`.

Use `Context?.getCountryCode()` to get Country Code (if it exists) of Device as a String.

Use `Context?.getDisplaySize()` to get size of Device's Display as a Point.

Use `Context?.getToolbarHeight()` to get height of Toolbar/ActionBar as a Int.

Use `Context?.getStatusBarHeight()` to get height of Statusbar as a Int.

Use `Float.dp()` or `Int.dp()` to get a value of float or int into dip unit. See this example :

```kotlin
fun testDisplayMetricsDP() {
    // Returns 2f * app.resources.displayMetrics.density. If device has xxhdpi density (factor = 3) , the value of `someDp` will be `6f`.
    val someDp = 2f.dp()
    val someDpInt = 2.dp()
}
```
Use `Float.px()` or `Int.px()` to get a value of float or int (dip) into px unit. See this example :
```kotlin
fun testDisplayMetricsPX() {
    // Returns 6f / app.resources.displayMetrics.density. If device has xxhdpi density (factor = 3) , the value of `someDp` will be `2f`.
    val somePx = 6f.dp()
    val somePxInt = 6.dp()
}
```  
> You must bind your Application class with `meow.controller` in Application `onCreate()` to use above examples.

Use `Context?.isNightModeFromSettings()` to check that Night/Dark mode is enabled by System or not.

Use `Context?.showOrHideKeyboard()` to show or hide soft Keyboard.

Use `Context?.vibrate(duration)` to vibrate Device for specific duration time in millisecond. Default value of `duration` is 150.
