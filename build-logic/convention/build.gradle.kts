plugins {
    `kotlin-dsl`
}

group = "com.acon.acon.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("nonAndroidLibrary") {
            id = "com.acon.non.android.library"
            implementationClass = "NonAndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "com.acon.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "com.acon.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.acon.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "com.acon.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibraryHilt") {
            id = "com.acon.android.library.hilt"
            implementationClass = "AndroidLibraryHiltConventionPlugin"
        }
        register("androidLibraryOrbit") {
            id = "com.acon.android.library.orbit"
            implementationClass = "AndroidLibraryOrbitConventionPlugin"
        }
        register("androidLibraryHaze") {
            id = "com.acon.android.library.haze"
            implementationClass = "AndroidLibraryHazeConventionPlugin"
        }
        register("androidLibraryCoil") {
            id = "com.acon.android.library.coil"
            implementationClass = "AndroidLibraryCoilConventionPlugin"
        }
        register("androidLibraryNaverMap") {
            id = "com.acon.android.library.naver.map"
            implementationClass = "AndroidLibraryNaverMapConventionPlugin"
        }
        register("firebase") {
            id = "com.acon.firebase"
            implementationClass = "FirebaseConventionPlugin"
        }
    }
}