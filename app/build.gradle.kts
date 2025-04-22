/** See AndroidApplicationConventionPlugin.kt */

plugins {
    alias(libs.plugins.acon.android.application)
    alias(libs.plugins.acon.android.application.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.naver.map)
}

android {
    namespace = "com.acon.acon"

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.core.utils.feature)
    implementation(projects.core.map)
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.feature.signin)
    implementation(projects.feature.spot)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.areaverification)
    implementation(projects.feature.upload)
    implementation(projects.feature.settings)
    implementation(projects.feature.profile)

    implementation(libs.play.services.location)

    implementation(libs.androidx.core.splashscreen)
}