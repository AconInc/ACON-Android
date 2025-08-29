plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.feature)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
    alias(libs.plugins.acon.android.library.naver.map)
}

android {
    namespace = "com.acon.acon.feature.onboarding"
}

dependencies {

    implementation(projects.core.map)

    implementation(libs.play.services.location)
    implementation(libs.lottie.compose)
}