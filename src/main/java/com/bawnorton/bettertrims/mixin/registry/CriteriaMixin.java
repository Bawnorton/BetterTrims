package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.data.advancement.criterion.*;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import java.util.Map;

@Mixin(Criteria.class)
public abstract class CriteriaMixin {
    //? if >=1.21 {
    /*static {
        TrimCriteria.BREWERS_DREAM_EXTENDED = bettertrims$register("brewers_dream_extended", new BrewersDreamExtendedCriteron());
        TrimCriteria.DODGED = bettertrims$register("dodged", new DodgeCriterion());
        TrimCriteria.WALKING_FURNACE_SMELTED = bettertrims$register("walking_furnace_smelted", new WalkingFurnaceSmeltedCriteron());
        TrimCriteria.KILLED_WITH_ELECTRICITY = bettertrims$register("killed_with_electrify", new ElectrifyingKilledCriterion());
        TrimCriteria.MINERS_RUSH_MAX_LEVEL = bettertrims$register("miners_rush_max_level", new MinersRushMaxLevelCriterion());
        TrimCriteria.SHARED_EFFECT = bettertrims$register("shared_effect", new SharedEffectCriterion());
        TrimCriteria.ECHOING_TRIGGERED = bettertrims$register("echoing_triggered", new EchoingTriggeredCriterion());
        TrimCriteria.DISCOUNTED_TRADE = bettertrims$register("discounted_trade", new DiscountedTradeCriterion());
        TrimCriteria.HYDROPHOBIC_TOUCH_WATER = bettertrims$register("hydrophobic_touch_water", new HydrophobicTouchWaterCriterion());
        TrimCriteria.MAGNETIC_HELMET_WORN = bettertrims$register("magnetic_helmet_worn", new MagneticHelmetWornCriterion());
        TrimCriteria.ENCHANTERS_FAVOUR_MAX_REROLLS = bettertrims$register("enchanters_favour_max_rerolls", new EnchantersFavourRerolledMaxCriterion());
        TrimCriteria.SNUCK_BY_CREEPER = bettertrims$register("snuck_by_creeper", new LightFootedSneakByCreeperCriterion());
        TrimCriteria.DECAPITATED_PIGLIN = bettertrims$register("decapitated_piglin", new CleavingDecapitatePiglinCriterion());
        TrimCriteria.BOUNCY_BOOTS_WORN = bettertrims$register("bouncy_boots_worn", new BouncyBootsWornCriterion());
    }

    @Unique
    private static <T extends Criterion<?>> T bettertrims$register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, BetterTrims.id(id), criterion);
    }
    *///?} else {
    @Shadow @Final private static Map<Identifier, Criterion<?>> VALUES;

    static {
        TrimCriteria.BREWERS_DREAM_EXTENDED = bettertrims$register(new BrewersDreamExtendedCriteron());
        TrimCriteria.DODGED = bettertrims$register(new DodgeCriterion());
        TrimCriteria.WALKING_FURNACE_SMELTED = bettertrims$register(new WalkingFurnaceSmeltedCriteron());
        TrimCriteria.KILLED_WITH_ELECTRICITY = bettertrims$register(new ElectrifyingKilledCriterion());
        TrimCriteria.MINERS_RUSH_MAX_LEVEL = bettertrims$register(new MinersRushMaxLevelCriterion());
        TrimCriteria.SHARED_EFFECT = bettertrims$register(new SharedEffectCriterion());
        TrimCriteria.ECHOING_TRIGGERED = bettertrims$register(new EchoingTriggeredCriterion());
        TrimCriteria.DISCOUNTED_TRADE = bettertrims$register(new DiscountedTradeCriterion());
        TrimCriteria.HYDROPHOBIC_TOUCH_WATER = bettertrims$register(new HydrophobicTouchWaterCriterion());
        TrimCriteria.MAGNETIC_HELMET_WORN = bettertrims$register(new MagneticHelmetWornCriterion());
        TrimCriteria.ENCHANTERS_FAVOUR_MAX_REROLLS = bettertrims$register(new EnchantersFavourRerolledMaxCriterion());
        TrimCriteria.SNUCK_BY_CREEPER = bettertrims$register(new LightFootedSneakByCreeperCriterion());
        TrimCriteria.DECAPITATED_PIGLIN = bettertrims$register(new CleavingDecapitatePiglinCriterion());
        TrimCriteria.BOUNCY_BOOTS_WORN = bettertrims$register(new BouncyBootsWornCriterion());
    }

    @Unique
    private static <T extends Criterion<?>> T bettertrims$register(T criterion) {
        VALUES.put(criterion.getId(), criterion);
        return criterion;
    }
    //?}
}
