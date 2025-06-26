plugins {
    alias(libs.plugins.acon.non.android.library)
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}