plugins {
    alias(libs.plugins.acon.non.android.library)
}

dependencies {
    api(projects.core.model)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}