import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.acon.android.application)
    alias(libs.plugins.acon.android.application.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.haze)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.acon"

    defaultConfig {
        manifestPlaceholders += mapOf()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["naverClientId"] = getPropertyKey("naver_client_id")
        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            "String.valueOf(\"${localProperties["naver_client_id"]}\")"
        )
        buildConfigField("String", "BASE_URL", "String.valueOf(\"${localProperties["BASE_URL"]}\")")
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties["storePath"].toString())
            storePassword = localProperties["storePassword"].toString()
            keyAlias = localProperties["keyAlias"].toString()
            keyPassword = localProperties["keyPassword"].toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

            manifestPlaceholders["naverClientId"] = getPropertyKey("naver_client_id")
            buildConfigField(
                "String",
                "NAVER_CLIENT_ID",
                "String.valueOf(\"${localProperties["naver_client_id"]}\")"
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "String.valueOf(\"${localProperties["BASE_URL"]}\")"
            )
        }
    }
}

fun getPropertyKey(propertyKey: String): String {
    val nullableProperty: String? =
        gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
    return nullableProperty ?: "null"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:utils:feature"))
    implementation(project(":core:map"))

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(project(":feature:signin"))
    implementation(project(":feature:spot"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:areaverification"))
    implementation(project(":feature:upload"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:profile"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.naver.map.compose)
    implementation(libs.play.services.location)
    implementation(libs.naver.map.location)

    implementation(libs.androidx.core.splashscreen)
}