pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://repository.map.naver.com/archive/maven")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "Acon"

include(":app")

include(
    ":core:designsystem",
    ":core:utils:feature",
    ":core:utils:feature",
    ":core:common",
    ":core:map"
)

include(
    ":feature:areaverification",
    ":feature:spot",
    ":feature:signin",
    ":feature:upload",
    ":feature:onboarding"
)

include(":data")
include(":domain")
include(":feature:profile")
include(":feature:settings")
