plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
}

android {
    namespace = "com.acon.acon.feature.upload"
}

dependencies {

    implementation(projects.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.utils.feature)
    implementation(projects.core.map)
    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.feature.common)

    implementation(libs.lottie.compose)
}