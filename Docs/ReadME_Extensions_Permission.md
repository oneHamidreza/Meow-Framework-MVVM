## 🚦 Permission Extensions

Use `FragmentActivityInterface.needPermissions()` to run an action which need some permissions. If given permissions are not granted, value of `isGrantedPermission` is false See this example : 

```kotlin
class MyFragment : MeowFragment<*>{
    override fun onViewCreated(savedInstanceState: Bundle?) {
        //...
        needPermissions (  
            Manifest.permission.READ_EXTERNAL_STORAGE,  
            Manifest.permission.WRITE_EXTERNAL_STORAGE  
        ) {  isGrantedPermission ->
            // If user give access this permissions, the value of `isGrantedPermission` will be `true` otherwise it will be `false`.  
        }
    }
}
```
