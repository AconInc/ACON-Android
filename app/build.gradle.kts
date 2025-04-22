import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.acon.acon"
    compileSdk = 35

    defaultConfig {
        manifestPlaceholders += mapOf()
        applicationId = "com.acon.acon"
        minSdk = 28
        targetSdk = 35
        versionCode = 6
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.compose)

    implementation(libs.naver.map.compose)
    implementation(libs.play.services.location)
    implementation(libs.naver.map.location)

    implementation(libs.haze)
    implementation(libs.haze.materials)

    implementation(libs.androidx.core.splashscreen)
}