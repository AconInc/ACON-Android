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

    implementation(libs.play.services.location)
    implementation(libs.play.services.coroutines)

    implementation(libs.accompanist.permissions)
}