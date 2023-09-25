package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.config.option.NestedConfigOption;
import com.bawnorton.bettertrims.config.option.OptionType;
import com.bawnorton.bettertrims.config.option.annotation.*;
import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.google.gson.annotations.Expose;

public class Config {
    private static Config LOCAL_INSTANCE = new Config();
    private static Config SERVER_INSTANCE = new Config();

    private Config() {
    }

    public static Config getLocalInstance() {
        return LOCAL_INSTANCE;
    }

    public static Config getServerInstance() {
        return SERVER_INSTANCE;
    }

    static void updateLocal(Config config) {
        LOCAL_INSTANCE = config;
    }

    static void updateServer(Config config) {
        SERVER_INSTANCE = config;
    }

    @Expose
    @IntOption(type = OptionType.GAME, value = 1, min = 1)
    public Integer trimDurability;
    @Expose
    @FloatOption(value = 0.05f, max = 1)
    @TextureLocation("minecraft:textures/item/quartz.png")
    public Float quartzExperienceBonus;
    @Expose
    @FloatOption(value = 8f, max = 80)
    @TextureLocation("minecraft:textures/item/iron_ingot.png")
    public Float ironMiningSpeedIncrease;
    @Expose
    @FloatOption(value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/netherite_ingot.png")
    public Float netheriteFireResistance;
    @Expose
    @FloatOption(value = 0.1f, max = 1)
    @TextureLocation("minecraft:textures/item/redstone.png")
    public Float redstoneMovementSpeedIncrease;
    @Expose
    @FloatOption(value = 0.05f, max = 0.5f)
    @TextureLocation("minecraft:textures/item/copper_ingot.png")
    public Float copperSwimSpeedIncrease;
    @Expose
    @FloatOption(value = 0.125f, max = 1)
    @TextureLocation("minecraft:textures/item/emerald.png")
    public Float emeraldVillagerDiscount;
    @Expose
    @FloatOption(value = 0.05f, max = 1)
    @TextureLocation("minecraft:textures/item/diamond.png")
    public Float diamondDamageReduction;
    @Expose
    @IntOption(value = 30)
    @TextureLocation("minecraft:textures/item/lapis_lazuli.png")
    public Integer lapisEnchantability;
    @Expose
    @FloatOption(value = 0.0625f, max = 1)
    @TextureLocation("minecraft:textures/item/amethyst_shard.png")
    public Float amethystPotionDurationModifyChance;
    @Expose
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/glowstone_dust.png")
    public Float glowstonePotionAmplifierIncreaseChance;
    @Expose
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/chorus_fruit.png")
    public Float chorusFruitDodgeChance;
    @Expose
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 1f, max = 10)
    @TextureLocation("minecraft:textures/item/fire_charge.png")
    public Float fireChargeFireDuration;
    @Expose
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.4f, max = 4)
    @TextureLocation("minecraft:textures/item/leather.png")
    public Float leatherStepHeightIncrease;
    @Expose
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 1.25f, max = 5)
    @TextureLocation("minecraft:textures/item/dragon_breath.png")
    public Float dragonBreathRadius;
    @Expose
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 1.5f, max = 5)
    @TextureLocation("minecraft:textures/item/echo_shard.png")
    public Float echoShardVibrationDistanceReduction;

    @Expose
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public PrismarineShard prismarineShardEffects;
    @Expose
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public EnchantedGoldenApple enchantedGoldenAppleEffects;
    @Expose
    @NestedOption(type = OptionType.MODDED)
    public Silver silverEffects;
    @Expose
    @NestedOption(type = OptionType.MODDED)
    public Platinum platinumEffects;
    @Expose
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public SlimeBall slimeBallEffects;
    @Expose
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public Coal coalEffects;
    @Expose
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public EnderPearl enderPearlEffects;
    @Expose
    @NestedOption(type = OptionType.VANILLA)
    public Gold goldEffects;
    @Expose
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public NetherBrick netherBrickEffects;

    @SuppressWarnings("unused")
    public static class EnchantedGoldenApple implements NestedConfigOption {
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 1200, max = 12000, min = 1)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Float absorptionDelay;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 250, max = 12000)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Float absorptionDelayReduction;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 2, max = 20)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Integer absorptionAmount;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 6, max = 30)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Integer maxAbsorption;
    }

    @SuppressWarnings("unused")
    public static class Silver implements NestedConfigOption {
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.05f, max = 0.5f)
        @TextureLocation(effectLookup = ArmorTrimEffects.SILVER)
        public Float movementSpeed;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.05f, max = 0.5f)
        @TextureLocation(effectLookup = ArmorTrimEffects.SILVER)
        public Float jumpHeight;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.5f, max = 5)
        @TextureLocation(effectLookup = ArmorTrimEffects.SILVER)
        public Float attackDamage;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.3f, max = 3)
        @TextureLocation(effectLookup = ArmorTrimEffects.SILVER)
        public Float attackSpeed;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.03f, max = 0.3f)
        @TextureLocation(effectLookup = ArmorTrimEffects.SILVER)
        public Float damageReduction;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 1)
        @TextureLocation(effectLookup = ArmorTrimEffects.SILVER)
        public Float improveVision;
    }

    @SuppressWarnings("unused")
    public static class SlimeBall implements NestedConfigOption {
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 1)
        @TextureLocation("minecraft:textures/item/slime_ball.png")
        public Float fallDamageReduction;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 2.5f)
        @TextureLocation("minecraft:textures/item/slime_ball.png")
        public Float knockbackIncrease;
    }

    @SuppressWarnings("unused")
    public static class Coal implements NestedConfigOption {
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = false)
        @TextureLocation("minecraft:textures/item/coal.png")
        public Boolean disableEffectToReduceLag;
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 5f, max = 10f)
        @TextureLocation("minecraft:textures/item/coal.png")
        public Float playerDetectionRadius;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 1, max = 10)
        @TextureLocation("minecraft:textures/item/coal.png")
        public Integer furnaceSpeedIncrease;
    }

    @SuppressWarnings("unused")
    public static class EnderPearl implements NestedConfigOption {
        @Expose
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 1)
        @TextureLocation("minecraft:textures/item/ender_pearl.png")
        public Float dodgeChance;
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/ender_pearl.png")
        public Boolean waterDamagesUser;
    }

    @SuppressWarnings("unused")
    public static class Platinum implements NestedConfigOption {
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation(effectLookup = ArmorTrimEffects.PLATINUM)
        public Boolean illagersIgnore;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 1, min = 1, max = 6)
        @TextureLocation(effectLookup = ArmorTrimEffects.PLATINUM)
        public Integer piecesForIllagersIgnore;
    }

    @SuppressWarnings("unused")
    public static class PrismarineShard implements NestedConfigOption {
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Boolean guardiansIgnore;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 2, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Integer piecesForGuardiansIgnore;
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Boolean miningFatigueImmunity;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 4, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Integer piecesForMiningFatigueImmunity;
    }

    @SuppressWarnings("unused")
    public static class Gold implements NestedConfigOption {
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/gold_ingot.png")
        public Boolean piglinsIgnore;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 1, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/gold_ingot.png")
        public Integer piecesForPiglinsIgnore;
    }

    @SuppressWarnings("unused")
    public static class NetherBrick implements NestedConfigOption {
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Boolean blazesIgnore;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 1, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Integer piecesForBlazesIgnore;
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Boolean witherSkeletonsIgnore;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 2, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Integer piecesForWitherSkeletonsIgnore;
        @Expose
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Boolean piglinsEnrage;
        @Expose
        @IntOption(type = OptionType.INHERIT, value = 4, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Integer piecesForPiglinsEnrage;
    }
}
