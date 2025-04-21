import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.catalog
import utils.implementation
import utils.ksp

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            dependencies {
                implementation(catalog.findLibrary("hilt-compose").get())
                implementation(catalog.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}