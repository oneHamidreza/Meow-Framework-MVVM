## 🚦 Permission Extensions

Use `FragmentActivityInterface.needPermissions()` to run an action which need some permissions. If given permissions are not granted, value of `grantedPermission` is false See this example : 

```kotlin
class MyFragment : MeowFragment<*>{
    override fun onViewCreated(savedInstanceState: Bundle?) {
        //...
        needPermissions (  
            Manifest.permission.READ_EXTERNAL_STORAGE,  
            Manifest.permission.WRITE_EXTERNAL_STORAGE  
        ) {  grantedPermission ->
            // If user give access this permissions, the value of `grantedPermission` will be `true` otherwise it will be `false`.  
        }
    }
}
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTU4ODMwMzQwMl19
-->