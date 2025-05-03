plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.orbit)
}
android {
    namespace = "com.acon.feature.common"
}

dependencies {
    implementation(projects.core.designsystem)
}