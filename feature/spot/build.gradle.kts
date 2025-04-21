plugins {
    alias(libs.plugins.acon.android.library)
    alias(libs.plugins.acon.android.library.compose)
    alias(libs.plugins.acon.android.library.hilt)
    alias(libs.plugins.acon.android.library.orbit)
    alias(libs.plugins.acon.android.library.haze)
}

android {
    namespace = "com.acon.acon.feature.spot"

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:utils:feature"))
    implementation(project(":core:map"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.pulltorefresh)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.lottie.compose)
}