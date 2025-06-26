/** See AndroidApplicationConventionPlugin.kt */

plugins {
    alias(libs.plugins.acon.android.application)
    alias(libs.plugins.acon.android.application.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.haze)
    alias(libs.plugins.acon.android.library.naver.map)
    alias(libs.plugins.acon.firebase)
}

android {
    namespace = "com.acon.acon"

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["app_name"] = "Acon Debug"
            manifestPlaceholders["app_domain"] = "aconpage"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["app_name"] = "Acon"
            manifestPlaceholders["app_domain"] = "aconpage"
        }
    }
}

dependencies {

    implementation(projects.core.designsystem)
    implementation(projects.core.utils.feature)
    implementation(projects.core.map)
    implementation(projects.core.adsApi)
    implementation(projects.core.analytics)
    implementation(projects.core.navigation)
    implementation(projects.core.ui)

    implementation(projects.domain)
    implementation(projects.data)

    implementation(projects.feature.adsImpl)
    implementation(projects.feature.signin)
    implementation(projects.feature.spot)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.areaverification)
    implementation(projects.feature.upload)
    implementation(projects.feature.settings)
    implementation(projects.feature.profile)

    implementation(libs.branch.io)
    implementation(libs.google.services.ads)
    implementation(libs.play.services.location)

    implementation(libs.androidx.core.splashscreen)
}