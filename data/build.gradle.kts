import utils.androidTestImplementation
import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.acon.data"

    defaultConfig {
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${localProperties["GOOGLE_CLIENT_ID"]}\"")
        buildConfigField("String", "BASE_URL", "\"${localProperties["BASE_URL"]}\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${localProperties["naver_client_id"]}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${localProperties["naver_client_secret"]}\"")
        buildConfigField("String", "NAVER_DEVELOPERS_CLIENT_ID", "\"${localProperties["naver_developers_client_id"]}\"")
        buildConfigField("String", "NAVER_DEVELOPERS_CLIENT_SECRET", "\"${localProperties["naver_developers_client_secret"]}\"")
    }
}

dependencies {

    implementation(projects.domain)
    implementation(projects.core.analytics)
    implementation(projects.core.launcher)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.bundles.googleSignIn)

    implementation(libs.preferences.datastore)
    
    testImplementation(libs.bundles.non.android.test)
    testRuntimeOnly(libs.bundles.junit5.runtime)
    androidTestImplementation(libs.bundles.android.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}