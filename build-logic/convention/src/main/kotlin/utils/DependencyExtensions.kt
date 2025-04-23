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

fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}

fun DependencyHandler.debugImplementation(dependency: Any) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}