package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.util.RegexIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public enum ArmorTrimEffects {
    NONE(() -> new ArmorTrimEffect(new RegexIdentifier(".*", "none"), Text.of("none"))),
    QUARTZ(() -> of(ArmorTrimMaterials.QUARTZ)),
    IRON(() -> of(ArmorTrimMaterials.IRON)),
    NETHERITE(() -> of(ArmorTrimMaterials.NETHERITE)),
    REDSTONE(() -> of(ArmorTrimMaterials.REDSTONE)),
    COPPER(() -> of(ArmorTrimMaterials.COPPER)),
    GOLD(() -> of(ArmorTrimMaterials.GOLD)),
    EMERALD(() -> of(ArmorTrimMaterials.EMERALD)),
    DIAMOND(() -> of(ArmorTrimMaterials.DIAMOND)),
    LAPIS(() -> of(ArmorTrimMaterials.LAPIS)),
    AMETHYST(() -> of(ArmorTrimMaterials.AMETHYST)),
    COAL(() -> of(Items.COAL)),
    DRAGONS_BREATH(() -> of(Items.DRAGON_BREATH)),
    CHORUS_FRUIT(() -> of(Items.CHORUS_FRUIT)),
    ECHO_SHARD(() -> of(Items.ECHO_SHARD)),
    ENDER_PEARL(() -> of(Items.ENDER_PEARL)),
    FIRE_CHARGE(() -> of(Items.FIRE_CHARGE)),
    GLOWSTONE_DUST(() -> of(Items.GLOWSTONE_DUST)),
    LEATHER(() -> of(Items.LEATHER)),
    NETHER_BRICK(() -> of(Items.NETHER_BRICK)),
    PRISMARINE_SHARD(() -> of(Items.PRISMARINE_SHARD)),
    RABBIT_HIDE(() -> of(Items.RABBIT_HIDE)),
    SLIME_BALL(() -> of(Items.SLIME_BALL)),
    ENCHANTED_GOLDEN_APPLE(() -> of(Items.ENCHANTED_GOLDEN_APPLE)),
    PLATINUM(() -> of(new RegexIdentifier(".*", "platinum"))),
    SILVER(() -> of(new RegexIdentifier(".*", "silver")));

    private final Supplier<ArmorTrimEffect> effect;

    ArmorTrimEffects(Supplier<ArmorTrimEffect> effect) {
        this.effect = effect;
    }

    private static ArmorTrimEffect of(Item item) {
        return of(new RegexIdentifier(".*", Registries.ITEM.getId(item).getPath()));
    }

    private static ArmorTrimEffect of(RegistryKey<ArmorTrimMaterial> material) {
        return of(new RegexIdentifier(".*", material.getValue().getPath()));
    }

    private static ArmorTrimEffect of(RegexIdentifier material) {
        return new ArmorTrimEffect(material, getTooltip(material.path()));
    }

    private static Text getTooltip(String path) {
        return Text.translatable("bettertrims.effect.%s.tooltip".formatted(path));
    }


    public static void forEachAppliedEffect(ItemStack stack, Consumer<ArmorTrimEffect> effectConsumer) {
        for (ArmorTrimEffects effect : values()) {
            if (effect.appliesTo(stack)) {
                effectConsumer.accept(effect.getEffect());
            }
        }
    }

    public ArmorTrimEffect getEffect() {
        return effect.get();
    }

    public void apply(Iterable<ItemStack> itemStacks, ArmorTrimEffect.Effect effect) {
        getEffect().apply(itemStacks, effect);
    }

    public boolean appliesTo(Iterable<ItemStack> itemStacks) {
        return getEffect().appliesTo(itemStacks);
    }

    public boolean appliesTo(ItemStack itemStack) {
        return getEffect().appliesTo(itemStack);
    }
}
