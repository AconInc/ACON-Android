plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
}

android {
    namespace = "com.acon.feature.ads_impl"
}

dependencies {
    implementation(projects.core.adsApi)
    implementation(projects.core.designsystem)

    implementation(libs.google.services.ads)
}