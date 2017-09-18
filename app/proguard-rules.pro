# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files (x86)\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# android-support

-keepattributes Signature, InnerClasses, LineNumberTable
-dontwarn android.support.**
-keep class android.support.** { *; }

# app
-keep class com.ljt.fastlivery.utils.proguard.NoProGuard { *; }
-keep class * extends com.ljt.fastlivery.utils.proguard.NoProGuard { *; }

# volley
-dontwarn com.android.volley.**
-keep class com.android.volley.** { *; }

# ormlite
-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** { *; }

# zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *; }

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# ButterKnife 7
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}