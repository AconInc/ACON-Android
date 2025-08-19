import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.coil)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.feature.ads_impl"

    defaultConfig {
        buildConfigField("String", "NATIVE_ADMOB_ID", "\"${localProperties["native_admob_id"]}\"")
        buildConfigField("String", "SAMPLE_NATIVE_ADMOB_ID", "\"${localProperties["sample_native_admob_id"]}\"")
    }
}

dependencies {
    implementation(projects.core.adsApi)
    implementation(projects.core.designsystem)

    implementation(libs.google.services.ads)
}