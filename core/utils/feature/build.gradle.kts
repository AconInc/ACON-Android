import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.orbit)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.acon.core.utils.feature"

    defaultConfig {
        buildConfigField("String", "AMPLITUDE_API_TEST_KEY", "\"${localProperties["AMPLITUDE_API_TEST_KEY"]}\"")
        buildConfigField("String", "AMPLITUDE_API_PRODUCTION_KEY", "\"${localProperties["AMPLITUDE_API_PRODUCTION_KEY"]}\"")

        buildConfigField("String", "NATIVE_ADMOB_ID", "\"${localProperties["native_admob_id"]}\"")
        buildConfigField("String", "SAMPLE_NATIVE_ADMOB_ID", "\"${localProperties["sample_native_admob_id"]}\"")
    }
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.accompanist.permissions)
    implementation(libs.amplitude)
}