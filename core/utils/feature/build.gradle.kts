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
    }
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.accompanist.permissions)
    implementation(libs.amplitude)
}