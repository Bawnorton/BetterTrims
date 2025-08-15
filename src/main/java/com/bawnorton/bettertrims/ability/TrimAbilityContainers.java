package com.bawnorton.bettertrims.ability;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.ability.type.WearingGoldTrimAbility;
import com.bawnorton.bettertrims.registry.BetterTrimsRegistries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public class TrimAbilityContainers {
    public static final ResourceKey<TrimAbilityContainer> WEARING_GOLD = key("wearing_gold");

    public static void bootstrap(BootstrapContext<TrimAbilityContainer> context) {
        HolderGetter<TrimMaterial> materialGetter = context.lookup(Registries.TRIM_MATERIAL);
        HolderGetter<TrimPattern> patternGetter = context.lookup(Registries.TRIM_PATTERN);
        register(
                context,
                WEARING_GOLD,
                new TrimAbilityContainer(
                        new ApplicationPredicate(
                                HolderSet.direct(materialGetter.getOrThrow(TrimMaterials.GOLD)),
                                HolderSet.empty(),
                                1
                        ),
                        new WearingGoldTrimAbility()
                )
        );
    }

    private static void register(BootstrapContext<TrimAbilityContainer> context, ResourceKey<TrimAbilityContainer> key, TrimAbilityContainer container) {
        context.register(key, container);
    }

    private static ResourceKey<TrimAbilityContainer> key(String key) {
        return ResourceKey.create(BetterTrimsRegistries.TRIM_ABILITY, BetterTrims.rl(key));
    }
}
