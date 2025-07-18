import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import utils.androidTestImplementation
import utils.catalog
import utils.configureKotlinAndroid
import utils.getPropertyKey
import utils.implementation
import utils.testImplementation
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val localProperties = Properties().apply {
            load(target.rootProject.file("local.properties").inputStream())
        }

        with(target) {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("com.google.gms.google-services")
            }

            extensions.configure<ApplicationExtension> {

                signingConfigs {
                    create("release") {
                        storeFile = rootProject.file(localProperties["storePath"].toString())
                        storePassword = localProperties["storePassword"].toString()
                        keyAlias = localProperties["keyAlias"].toString()
                        keyPassword = localProperties["keyPassword"].toString()
                    }
                }

                defaultConfig {
                    manifestPlaceholders += mapOf()
                    manifestPlaceholders["naverClientId"] = getPropertyKey("naver_client_id")

                    // TODO - 진짜 admob으로 바꿔야 함
                    manifestPlaceholders["admobAppUnitId"] = getPropertyKey("admob_app_unit_id")
                    manifestPlaceholders["branchIoKey"] = getPropertyKey("branch_io_key")

                    applicationId = catalog.findVersion("projectApplicationId").get().toString()
                    targetSdk = catalog.findVersion("projectTargetSdk").get().toString().toInt()
                    versionCode = catalog.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = catalog.findVersion("projectVersionName").get().toString()

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                configureKotlinAndroid(this)

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }

            afterEvaluate {
                dependencies {
                    implementation(project(":core:common"))
                    implementation(catalog.findBundle("android-defaults").get())
                    implementation(catalog.findBundle("play-app-update").get())
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

