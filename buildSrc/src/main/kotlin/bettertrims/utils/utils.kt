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

enum class ReplacementType {
    STRING,
    REGEX
}

class Replacement(val predicate: String, val from: String, val to: String, val type: ReplacementType = ReplacementType.STRING)

fun getReplacements() = listOf(
    Replacement("<1.21.8", "EntitySpawnReason", "MobSpawnType"),
    Replacement("<1.21.8", "snapTo", "moveTo"),
    Replacement("<1.21.8", "equipment.trim", "armortrim"),
    Replacement("<1.21.8", "util.context.ContextKeySet;", "world.level.storage.loot.parameters.LootContextParamSet;"),
    Replacement("<1.21.8", "util.context.ContextKey;", "world.level.storage.loot.parameters.LootContextParam;"),
    Replacement("<1.21.8", "ContextKeySet", "LootContextParamSet"),
    Replacement("<1.21.8", "ContextKey<", "LootContextParam<"),
    Replacement("<1.21.8", "MobEffects.HASTE", "MobEffects.DIG_SPEED")
)