plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.naver.map)
}

android {
    namespace = "com.acon.acon.core.map"
}

dependencies {
    implementation(projects.core.utils.feature)

    implementation(libs.play.services.location)
}