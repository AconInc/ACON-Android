import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import utils.catalog
import utils.implementation

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion =
                        catalog.findVersion("composeCompilerVersion").get().requiredVersion
                }
            }

            afterEvaluate {
                dependencies {
                    implementation(catalog.findLibrary("kotlinx-serialization-json").get())
                    implementation(platform(catalog.findLibrary("androidx-compose-bom").get()))
                    implementation(catalog.findBundle("compose-defaults").get())
                }
            }
        }
    }
}