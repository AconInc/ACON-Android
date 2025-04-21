plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.haze)
}

android {
    namespace = "com.acon.acon.core.designsystem"
}

dependencies {
    implementation(project(":core:common"))
}