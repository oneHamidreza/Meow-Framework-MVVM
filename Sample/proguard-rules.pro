# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod,*Annotation*

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Used for Kodein bindAutoTag
-keepnames public class * extends meow.core.arch.MeowViewModel

# Used for Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Used for OKHttp
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.ConscryptHostnameVerifier

# Used for Kotlinx Serialization
-dontnote kotlinx.serialization.SerializationKT
-keep,includedescriptorclasses class sample.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class sample.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class sample.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}

# Used for Navigation pass data
-keepnames class sample.ParcelableArg # <-- change package name to your app's
-keepnames class sample.SerializableArg # <-- change package name to your app's
-keepnames class sample.EnumArg # <-- change package name to your app's

# Used for Moshi
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
# Enum field names are used by the integrated EnumJsonAdapter.
# values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}
-keep class kotlin.reflect.jvm.internal.impl.builtins.BuiltInsLoaderImpl
-keepclassmembers class kotlin.Metadata {
     public <methods>;
}
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers @com.squareup.moshi.JsonClass @kotlin.Metadata class * {
    synthetic <init>(...);
}