import com.android.build.api.dsl.ApplicationExtension
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

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<ApplicationExtension> {
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                defaultConfig {
                    applicationId = catalog.findVersion("projectApplicationId").get().toString()
                    targetSdk = catalog.findVersion("projectTargetSdk").get().toString().toInt()
                    versionCode = catalog.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = catalog.findVersion("projectVersionName").get().toString()

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                configureKotlinAndroid(this)
            }
            afterEvaluate {
                dependencies {
                    implementation(project(":core:common"))
                    implementation(catalog.findBundle("android-defaults").get())

                    testImplementation(catalog.findLibrary("junit").get())
                    androidTestImplementation(catalog.findLibrary("androidx-junit").get())
                    androidTestImplementation(catalog.findLibrary("androidx-espresso-core").get())
                    androidTestImplementation(catalog.findLibrary("androidx-ui-test-junit4").get())
                }
            }
        }
    }
}
