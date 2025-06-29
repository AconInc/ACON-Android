plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.feature)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
}

android {
    namespace = "com.acon.acon.feature.profile"
}

dependencies {
    implementation(libs.google.services.ads) // TODO - admob Plugin 분리?
    implementation(libs.androidx.paignig.compose)

}