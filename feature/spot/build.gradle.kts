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
    namespace = "com.acon.acon.feature.spot"
}

dependencies {

    implementation(projects.core.map)
    implementation(projects.core.adsApi)

    implementation(libs.branch.io)
    implementation(libs.pulltorefresh)
    implementation(libs.lottie.compose)
}