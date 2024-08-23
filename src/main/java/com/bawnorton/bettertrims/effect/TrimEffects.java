package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.data.TrimMaterialTags;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class TrimEffects {
    private static final Map<TagKey<Item>, TrimEffect<?>> TRIM_EFFECTS = new HashMap<>();

    public static final AmethystTrimEffect AMETHYST = of(TrimMaterialTags.AMETHYST, AmethystTrimEffect::new);
    public static final CopperTrimEffect COPPER = of(TrimMaterialTags.COPPER, CopperTrimEffect::new);
    public static final DiamondTrimEffect DIAMOND = of(TrimMaterialTags.DIAMOND, DiamondTrimEffect::new);
    public static final EmeraldTrimEffect EMERALD = of(TrimMaterialTags.EMERALD, EmeraldTrimEffect::new);
    public static final GoldTrimEffect GOLD = of(TrimMaterialTags.GOLD, GoldTrimEffect::new);
    public static final IronTrimEffect IRON = of(TrimMaterialTags.IRON, IronTrimEffect::new);
    public static final LapisTrimEffect LAPIS = of(TrimMaterialTags.LAPIS, LapisTrimEffect::new);
    public static final NetheriteTrimEffect NETHERITE = of(TrimMaterialTags.NETHERITE, NetheriteTrimEffect::new);
    public static final QuartzTrimEffect QUARTZ = of(TrimMaterialTags.QUARTZ, QuartzTrimEffect::new);
    public static final RedstoneTrimEffect REDSTONE = of(TrimMaterialTags.REDSTONE, RedstoneTrimEffect::new);

    private static <T extends TrimEffect<?>> T of(TagKey<Item> tag, TrimEffect.Factory<T> factory) {
        T effect = factory.create(tag);
        TRIM_EFFECTS.put(tag, effect);
        return effect;
    }

    public static void forEachTrimEffect(BiConsumer<TagKey<Item>, ? super TrimEffect<?>> biConsumer) {
        TRIM_EFFECTS.forEach(biConsumer);
    }
}
