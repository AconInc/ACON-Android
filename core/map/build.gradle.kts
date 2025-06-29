import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.naver.map)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.acon.core.map"

    defaultConfig {
        buildConfigField("String", "NAVER_NCP_KEY_ID", "\"${localProperties["naver_ncp_key_id"]}\"")
    }
}

dependencies {

    implementation(projects.core.ui)
    implementation(libs.play.services.location)
}