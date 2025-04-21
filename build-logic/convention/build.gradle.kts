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
    }
    plugins {
        register("androidApplication") {
            id = "com.acon.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
    plugins {
        register("androidApplicationCompose") {
            id = "com.acon.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
    plugins {
        register("androidLibrary") {
            id = "com.acon.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
    plugins {
        register("androidLibraryCompose") {
            id = "com.acon.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
    }
    plugins {
        register("androidLibraryHilt") {
            id = "com.acon.android.library.hilt"
            implementationClass = "AndroidLibraryHiltConventionPlugin"
        }
    }
    plugins {
        register("androidLibraryOrbit") {
            id = "com.acon.android.library.orbit"
            implementationClass = "AndroidLibraryOrbitConventionPlugin"
        }
    }
    plugins {
        register("androidLibraryHaze") {
            id = "com.acon.android.library.haze"
            implementationClass = "AndroidLibraryHazeConventionPlugin"
        }
    }
    plugins {
        register("androidLibraryCoil") {
            id = "com.acon.android.library.coil"
            implementationClass = "AndroidLibraryCoilConventionPlugin"
        }
    }
    plugins {
        register("androidLibraryNaverMap") {
            id = "com.acon.android.library.naver.map"
            implementationClass = "AndroidLibraryNaverMapConventionPlugin"
        }
    }
}