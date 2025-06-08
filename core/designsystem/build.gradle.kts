plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
}

android {
    namespace = "com.acon.acon.core.designsystem"
}

dependencies {
    implementation(libs.google.services.ads) // TODO - admob Plugin 분리?
    implementation(libs.lottie.compose)
    implementation(libs.palette)
}