plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
}

android {
    namespace = "com.acon.acon.feature.signin"
}

dependencies {

    implementation(projects.domain)
    implementation(projects.feature.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)
    implementation(projects.core.utils.feature)

    implementation(libs.lottie.compose)
}