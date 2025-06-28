import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.core.analytics"

    defaultConfig {
        buildConfigField("String", "AMPLITUDE_API_TEST_KEY", "\"${localProperties["AMPLITUDE_API_TEST_KEY"]}\"")
        buildConfigField("String", "AMPLITUDE_API_PRODUCTION_KEY", "\"${localProperties["AMPLITUDE_API_PRODUCTION_KEY"]}\"")
    }
}

dependencies {
    implementation(libs.amplitude)

    testImplementation(libs.bundles.android.test)
    testImplementation(libs.bundles.non.android.test)
}