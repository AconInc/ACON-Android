import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.catalog
import utils.implementation
import utils.ksp

class AndroidLibraryHiltConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                implementation(catalog.findLibrary("hilt-android").get())
                ksp(catalog.findLibrary("hilt-compiler").get())
            }
        }
    }
}
