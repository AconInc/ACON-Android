plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
}

android {
    namespace = "com.acon.core.navigation"
}

dependencies {
    implementation(projects.core.model)
}