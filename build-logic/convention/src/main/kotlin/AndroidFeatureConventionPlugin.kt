import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import utils.implementation

/**
 * Feature 모듈이 반드시 의존하는 모듈만 명시
 */
class AndroidFeatureConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            afterEvaluate {
                dependencies {
                    implementation(project(":core:ui"))
                    implementation(project(":core:designsystem"))
                    implementation(project(":core:analytics"))
                    implementation(project(":domain"))
                }
            }
        }
    }
}