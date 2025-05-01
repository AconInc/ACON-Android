import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import utils.catalog
import utils.implementation

class AndroidLibraryCoilConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            dependencies {
                implementation(catalog.findBundle("coil").get())
            }
        }
    }
}
