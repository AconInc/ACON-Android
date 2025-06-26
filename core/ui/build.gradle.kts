plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
    alias(libs.plugins.acon.android.library.orbit)
}

android {
    namespace = "com.acon.core.ui"
}

dependencies {
    api(projects.core.model)
}