plugins {
    alias(libs.plugins.acon.non.android.library)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.bundles.non.android.test)
}