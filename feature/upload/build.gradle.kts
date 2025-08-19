import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.feature)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.acon.feature.upload"

    defaultConfig {
        buildConfigField("String", "BUCKET_URL", "\"${localProperties["BUCKET_URL"]}\"")
    }
}

dependencies {

    implementation(projects.core.map)
    implementation(projects.core.common)

    implementation(libs.lottie.compose)
}