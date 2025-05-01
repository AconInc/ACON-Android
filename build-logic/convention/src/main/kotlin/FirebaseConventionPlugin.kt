import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.catalog
import utils.implementation

class FirebaseConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                implementation(platform(catalog.findLibrary("firebase-bom").get()))
                implementation(catalog.findBundle("firebase").get())
            }
        }
    }
}