import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import utils.androidTestImplementation
import utils.catalog
import utils.configureKotlinAndroid
import utils.implementation
import utils.testImplementation

class AndroidLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("kotlin-parcelize")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    release {
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }

            afterEvaluate {
                dependencies {
                    implementation(project(":core:common"))
                    implementation(catalog.findBundle("android-defaults").get())
                    implementation(catalog.findLibrary("timber").get())

                    testImplementation(catalog.findLibrary("junit").get())
                    androidTestImplementation(catalog.findLibrary("androidx-junit").get())
                    androidTestImplementation(catalog.findLibrary("androidx-espresso-core").get())
                    androidTestImplementation(catalog.findLibrary("androidx-ui-test-junit4").get())
                }
            }
        }
    }
}