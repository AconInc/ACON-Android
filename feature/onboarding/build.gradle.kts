plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
}

android {
    namespace = "com.acon.acon.feature.onboarding"
}

dependencies {

    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.utils.feature)
    implementation(projects.core.analytics)
    implementation(projects.feature.common)

    implementation(libs.lottie.compose)
}