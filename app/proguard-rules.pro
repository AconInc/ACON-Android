# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 기본 ProGuard 최적화
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes EnclosingMethod

# kotlin
-keepclassmembers class kotlin.Metadata { *; }
-keep class kotlin.** { *; }
-dontwarn kotlin.**

-keep interface com.acon.acon.feature.profile.composable.ProfileRoute { *; }
-keep interface com.acon.acon.feature.spot.SpotRoute { *; }

# Android 기본 구성 요소
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Application
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers enum * { *; }
-keepclassmembers class * {
    public void *(android.os.Bundle);
    public void *(android.view.Menu, android.view.MenuItem);
}
-dontwarn android.**

# Google Mobile Services
-keep class com.google.android.gms.** { *; }

# Kotlin Serialization에서 사용하는 클래스 유지
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class * {*;}
-keepclassmembers class * { @kotlinx.serialization.* <fields>; }

# Compose Navigation에서 `@Serializable`을 사용하는 경우
-keep class androidx.navigation.** { *; }
-keep class androidx.navigation.compose.** { *; }
-dontwarn androidx.navigation.**

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Lifecycle & ViewModel
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class androidx.lifecycle.ViewModel {
    public <init>(...);
}
-dontwarn androidx.lifecycle.**

# Navigation Compose
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class androidx.hilt.** { *; }
-dontwarn dagger.**

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Naver Maps
-keep class io.github.fornewid.naver.maps.** { *; }
-dontwarn io.github.fornewid.naver.maps.**

# Credentials
-keep class androidx.credentials.** { *; }
-keep class androidx.credentials.playservices.** { *; }
-keepclassmembers class androidx.credentials.** { *; }
-keepclassmembers class androidx.credentials.playservices.** { *; }
-dontwarn androidx.credentials.**

# Retrofit & OkHttp
-keep class com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.retrofit2.** { *; }
-keep interface com.squareup.retrofit2.** { *; }
-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-dontwarn com.squareup.retrofit2.**

# domain classes
-keep class com.acon.acon.domain.model.** { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Amplitude
-keep class com.amplitude.** { *; }
-dontwarn com.amplitude.**

# Lottie
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# 기타 설정
-dontnote okhttp3.**
-dontnote retrofit2.**
-dontnote kotlinx.coroutines.**

# 에러 발생시 라인 표시
-keepattributes SourceFile,LineNumberTable

# jdk 컴파일할 때 발생하는 오류 메시지 방지
-keepattributes EnclosingMethod

# Begin : material, androidx
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }