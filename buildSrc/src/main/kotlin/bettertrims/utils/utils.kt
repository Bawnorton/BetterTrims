package bettertrims.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.component.ModuleComponentIdentifier

fun Project.deps(name: String): String? = findProperty("deps.${name}") as String?
fun Project.deps(name: String, consumer: (prop: String) -> Unit) = deps(name)?.let(consumer)

fun Project.mod(name: String): String? = findProperty("mod.${name}") as String?
fun Project.mod(name: String, consumer: (prop: String) -> Unit) = mod(name)?.let(consumer)

fun Project.applyMixinDebugSettings(
    vmArgConsumer: (String) -> Unit,
    propertyConsumer: (String, String) -> Unit
) {
    val mixinJarFile = configurations.named("runtimeClasspath").get().incoming.artifactView {
        componentFilter {
            it is ModuleComponentIdentifier && it.group == "net.fabricmc" && it.module == "sponge-mixin"
        }
    }.files.singleFile

    vmArgConsumer("-javaagent:$mixinJarFile")
    vmArgConsumer("-XX:+AllowEnhancedClassRefinition")
    propertyConsumer("mixin.hotSwap", "true")
    propertyConsumer("mixin.debug.export", "true")
}