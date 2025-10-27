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
    vmArgConsumer("-XX:+AllowEnhancedClassRedefinition")
    propertyConsumer("mixin.hotSwap", "true")
    propertyConsumer("mixin.debug.export", "true")
}

enum class ReplacementType {
    STRING,
    REGEX
}

class Swap(val id: String, val predicate: String, val from: String, val to: String)

fun getSwaps() = listOf(
    Swap("item_data_predicate",  "<1.21.8", "DataComponentPredicate", "ItemSubPredicate"),
    Swap("damage_predicate", "<1.21.8", "DamagePredicate", "ItemDamagePredicate"),
    Swap("enchantments_predicate", "<1.21.8", "EnchantmentsPredicate", "ItemEnchantmentsPredicate"),
    Swap("potions_predicate", "<1.21.8", "PotionsPredicate", "ItemPotionsPredicate"),
    Swap("custom_data_predicate", "<1.21.8", "CustomDataPredicate", "ItemCustomDataPredicate"),
    Swap("container_predicate", "<1.21.8", "ContainerPredicate", "ItemContainerPredicate"),
    Swap("bundle_predicate", "<1.21.8", "BundlePredicate", "ItemBundlePredicate"),
    Swap("firework_explosion_predicate", "<1.21.8", "FireworkExplosionPredicate", "ItemFireworkExplosionPredicate"),
    Swap("fireworks_predicate", "<1.21.8", "FireworksPredicate", "ItemFireworksPredicate"),
    Swap("writable_book_predicate", "<1.21.8", "WritableBookPredicate", "ItemWritableBookPredicate"),
    Swap("written_book_predicate", "<1.21.8", "WrittenBookPredicate", "ItemWrittenBookPredicate"),
    Swap("attribute_modifiers_predicate", "<1.21.8", "AttributeModifiersPredicate", "ItemAttributeModifiersPredicate"),
    Swap("jukebox_playable_predicate", "<1.21.8", "JukeboxPlayablePredicate", "ItemJukeboxPlayablePredicate"),
    Swap("trim_predicate", "<1.21.8", "TrimPredicate", "ItemTrimPredicate")
)

class Replacement(val predicate: String, val from: String, val to: String, val type: ReplacementType = ReplacementType.STRING, val id: String? = null)

fun getReplacements() = listOf(
    Replacement("<1.21.8", "EntitySpawnReason", "MobSpawnType"),
    Replacement("<1.21.8", "snapTo", "moveTo"),
    Replacement("<1.21.8", "equipment.trim", "armortrim"),
    Replacement("<1.21.8", "util.context.ContextKeySet;", "world.level.storage.loot.parameters.LootContextParamSet;"),
    Replacement("<1.21.8", "util.context.ContextKey;", "world.level.storage.loot.parameters.LootContextParam;"),
    Replacement("<1.21.8", "ContextKeySet", "LootContextParamSet"),
    Replacement("<1.21.8", "ContextKey<", "LootContextParam<"),
    Replacement("<1.21.8", "MobEffects.HASTE", "MobEffects.DIG_SPEED"),
    Replacement("<1.21.8", "MobEffects.STRENGTH", "MobEffects.DAMAGE_BOOST"),
    Replacement("<1.21.8", "MobEffects.SPEED", "MobEffects.MOVEMENT_SPEED"),
    Replacement("<1.21.8", "MobEffects.JUMP_BOOST)", "MobEffects.JUMP)"),
    Replacement("<1.21.8", "MobEffects.RESISTANCE", "MobEffects.DAMAGE_RESISTANCE"),
    Replacement("<1.21.8", "registry::getValueOrThrow", "registry::getOrThrow"),
    Replacement("<1.21.8", "MacosUtil.IS_MACOS", "Minecraft.ON_OSX"),

    Replacement("<1.21.8", "DataComponentPredicates", "ItemSubPredicates", id = "item_data_predicates"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.DataComponentPredicate", "net.minecraft.advancements.critereon.ItemSubPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.DamagePredicate", "net.minecraft.advancements.critereon.ItemDamagePredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.EnchantmentsPredicate", "net.minecraft.advancements.critereon.ItemEnchantmentsPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.PotionsPredicate", "net.minecraft.advancements.critereon.ItemPotionsPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.CustomDataPredicate", "net.minecraft.advancements.critereon.ItemCustomDataPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.ContainerPredicate", "net.minecraft.advancements.critereon.ItemContainerPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.BundlePredicate", "net.minecraft.advancements.critereon.ItemBundlePredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.FireworkExplosionPredicate", "net.minecraft.advancements.critereon.ItemFireworkExplosionPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.FireworksPredicate", "net.minecraft.advancements.critereon.ItemFireworksPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.WritableBookPredicate", "net.minecraft.advancements.critereon.ItemWritableBookPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.WrittenBookPredicate", "net.minecraft.advancements.critereon.ItemWrittenBookPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.AttributeModifiersPredicate", "net.minecraft.advancements.critereon.ItemAttributeModifiersPredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.JukeboxPlayablePredicate", "net.minecraft.advancements.critereon.ItemJukeboxPlayablePredicate"),
    Replacement("<1.21.8", "net.minecraft.core.component.predicates.TrimPredicate", "net.minecraft.advancements.critereon.ItemTrimPredicate")
)