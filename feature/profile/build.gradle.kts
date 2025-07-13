import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
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
    namespace = "com.acon.acon.feature.profile"

    defaultConfig {
        buildConfigField("String", "BUCKET_URL", "\"${localProperties["BUCKET_URL"]}\"")
    }
}

dependencies {
    implementation(libs.google.services.ads) // TODO - admob Plugin 분리?
    implementation(libs.androidx.paignig.compose)

    implementation(projects.domain)
    implementation(projects.feature.common)
    implementation(projects.core.analytics)
    implementation(projects.core.designsystem)
    implementation(projects.core.utils.feature)
}