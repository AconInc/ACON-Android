import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.protobuf)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.core.data"

    defaultConfig {
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${localProperties["GOOGLE_CLIENT_ID"]}\"")
        buildConfigField("String", "BASE_URL", "\"${localProperties["BASE_URL"]}\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"${localProperties["naver_client_id"]}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${localProperties["naver_client_secret"]}\"")
        buildConfigField("String", "NAVER_DEVELOPERS_CLIENT_ID", "\"${localProperties["naver_developers_client_id"]}\"")
        buildConfigField("String", "NAVER_DEVELOPERS_CLIENT_SECRET", "\"${localProperties["naver_developers_client_secret"]}\"")
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val capName = variant.name.capitalized()
            tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
                setSource(tasks.getByName("generate${capName}Proto").outputs)
            }
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.32.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
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
    implementation(libs.proto.datastore)
    implementation(libs.protobuf.kotlin)

    testImplementation(libs.bundles.non.android.test)
    testRuntimeOnly(libs.bundles.junit5.runtime)
    androidTestImplementation(libs.bundles.android.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}