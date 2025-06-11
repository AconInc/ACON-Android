import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.androidTestImplementation
import utils.catalog
import utils.debugImplementation
import utils.implementation
import utils.ksp

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            afterEvaluate {
                dependencies {
                    implementation(catalog.findLibrary("kotlinx-serialization-json").get())
                    implementation(catalog.findBundle("compose-defaults").get())
                    implementation(platform(catalog.findLibrary("androidx-compose-bom").get()))
                    implementation(catalog.findLibrary("kotlinx-immutable").get())

                    androidTestImplementation(platform(catalog.findLibrary("androidx-compose-bom").get()))
                }
            }
        }
    }
}