plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.feature)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
}

android {
    namespace = "com.acon.acon.feature.upload"
}

dependencies {

    implementation(projects.core.map)
    implementation(projects.core.common)

    implementation(libs.lottie.compose)
}