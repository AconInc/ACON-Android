package utils

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandler.compileOnly(dependency: Any) {
    add("compileOnly", dependency)
}