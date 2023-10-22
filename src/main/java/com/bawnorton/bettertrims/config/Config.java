package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.config.annotation.*;
import com.bawnorton.bettertrims.config.option.NestedConfigOption;
import com.bawnorton.bettertrims.config.option.OptionType;

public class Config {
    private static Config LOCAL_INSTANCE = new Config();
    private static Config SERVER_INSTANCE = new Config();

    @IntOption(type = OptionType.GAME, value = 1, min = 1)
    public Integer trimDurability;
    @FloatOption(value = 0.05f, max = 1)
    @TextureLocation("minecraft:textures/item/quartz.png")
    public Float quartzExperienceBonus;
    @FloatOption(value = 8f, max = 80)
    @TextureLocation("minecraft:textures/item/iron_ingot.png")
    public Float ironMiningSpeedIncrease;
    @FloatOption(value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/netherite_ingot.png")
    public Float netheriteFireResistance;
    @FloatOption(value = 0.1f, max = 1)
    @TextureLocation("minecraft:textures/item/redstone.png")
    public Float redstoneMovementSpeedIncrease;
    @FloatOption(value = 0.05f, max = 0.5f)
    @TextureLocation("minecraft:textures/item/copper_ingot.png")
    public Float copperSwimSpeedIncrease;
    @FloatOption(value = 0.125f, max = 1)
    @TextureLocation("minecraft:textures/item/emerald.png")
    public Float emeraldVillagerDiscount;
    @FloatOption(value = 0.05f, max = 1)
    @TextureLocation("minecraft:textures/item/diamond.png")
    public Float diamondDamageReduction;
    @IntOption(value = 30)
    @TextureLocation("minecraft:textures/item/lapis_lazuli.png")
    public Integer lapisEnchantability;
    @FloatOption(value = 0.0625f, max = 1)
    @TextureLocation("minecraft:textures/item/amethyst_shard.png")
    public Float amethystPotionDurationModifyChance;

    @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/glowstone_dust.png")
    public Float glowstonePotionAmplifierIncreaseChance;
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/chorus_fruit.png")
    public Float chorusFruitDodgeChance;
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 1f, max = 10)
    @TextureLocation("minecraft:textures/item/fire_charge.png")
    public Float fireChargeFireDuration;
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 0.4f, max = 4)
    @TextureLocation("minecraft:textures/item/leather.png")
    public Float leatherStepHeightIncrease;
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 1.25f, max = 5)
    @TextureLocation("minecraft:textures/item/dragon_breath.png")
    public Float dragonBreathRadius;
    @FloatOption(type = OptionType.ADDED_VANILLA, value = 1.5f, max = 5)
    @TextureLocation("minecraft:textures/item/echo_shard.png")
    public Float echoShardVibrationDistanceReduction;

    @NestedOption(type = OptionType.VANILLA)
    public Gold goldEffects;
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public PrismarineShard prismarineShardEffects;
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public EnchantedGoldenApple enchantedGoldenAppleEffects;
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public SlimeBall slimeBallEffects;
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public Coal coalEffects;
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public EnderPearl enderPearlEffects;
    @NestedOption(type = OptionType.ADDED_VANILLA)
    public NetherBrick netherBrickEffects;
    @NestedOption(type = OptionType.MODDED)
    public Silver silverEffects;
    @NestedOption(type = OptionType.MODDED)
    public Platinum platinumEffects;

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

    public static class EnchantedGoldenApple implements NestedConfigOption {
        @FloatOption(type = OptionType.INHERIT, value = 1200, max = 12000, min = 1)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Float absorptionDelay;
        @FloatOption(type = OptionType.INHERIT, value = 250, max = 12000)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Float absorptionDelayReduction;
        @IntOption(type = OptionType.INHERIT, value = 2, max = 20)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Integer absorptionAmount;
        @IntOption(type = OptionType.INHERIT, value = 6, max = 30)
        @TextureLocation("minecraft:textures/item/golden_apple.png")
        public Integer maxAbsorption;
    }

    public static class Silver implements NestedConfigOption {
        @FloatOption(type = OptionType.INHERIT, value = 0.05f, max = 0.5f)
        @TextureLocation(value = "silver", effectLookup = true)
        public Float movementSpeed;
        @FloatOption(type = OptionType.INHERIT, value = 0.05f, max = 0.5f)
        @TextureLocation(value = "silver", effectLookup = true)
        public Float jumpHeight;
        @FloatOption(type = OptionType.INHERIT, value = 0.5f, max = 5)
        @TextureLocation(value = "silver", effectLookup = true)
        public Float attackDamage;
        @FloatOption(type = OptionType.INHERIT, value = 0.3f, max = 3)
        @TextureLocation(value = "silver", effectLookup = true)
        public Float attackSpeed;
        @FloatOption(type = OptionType.INHERIT, value = 0.03f, max = 0.3f)
        @TextureLocation(value = "silver", effectLookup = true)
        public Float damageReduction;
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 1)
        @TextureLocation(value = "silver", effectLookup = true)
        public Float improveVision;
    }

    public static class SlimeBall implements NestedConfigOption {
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 1)
        @TextureLocation("minecraft:textures/item/slime_ball.png")
        public Float fallDamageReduction;
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 2.5f)
        @TextureLocation("minecraft:textures/item/slime_ball.png")
        public Float knockbackIncrease;
    }

    public static class Coal implements NestedConfigOption {
        @BooleanOption(type = OptionType.INHERIT, value = false)
        @TextureLocation("minecraft:textures/item/coal.png")
        public Boolean disableEffectToReduceLag;
        @FloatOption(type = OptionType.INHERIT, value = 5f, max = 10f)
        @TextureLocation("minecraft:textures/item/coal.png")
        public Float playerDetectionRadius;
        @IntOption(type = OptionType.INHERIT, value = 1, max = 10)
        @TextureLocation("minecraft:textures/item/coal.png")
        public Integer furnaceSpeedIncrease;
    }

    public static class EnderPearl implements NestedConfigOption {
        @FloatOption(type = OptionType.INHERIT, value = 0.25f, max = 1)
        @TextureLocation("minecraft:textures/item/ender_pearl.png")
        public Float dodgeChance;
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/ender_pearl.png")
        public Boolean waterDamagesUser;
    }

    public static class Platinum implements NestedConfigOption {
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation(value = "platinum", effectLookup = true)
        public Boolean illagersIgnore;
        @IntOption(type = OptionType.INHERIT, value = 1, min = 1, max = 6)
        @TextureLocation(value = "platinum", effectLookup = true)
        public Integer piecesForIllagersIgnore;
    }

    public static class PrismarineShard implements NestedConfigOption {
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Boolean guardiansIgnore;
        @IntOption(type = OptionType.INHERIT, value = 2, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Integer piecesForGuardiansIgnore;
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Boolean miningFatigueImmunity;
        @IntOption(type = OptionType.INHERIT, value = 4, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/prismarine_shard.png")
        public Integer piecesForMiningFatigueImmunity;
    }

    public static class Gold implements NestedConfigOption {
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/gold_ingot.png")
        public Boolean piglinsIgnore;
        @IntOption(type = OptionType.INHERIT, value = 1, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/gold_ingot.png")
        public Integer piecesForPiglinsIgnore;
    }

    public static class NetherBrick implements NestedConfigOption {
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Boolean blazesIgnore;
        @IntOption(type = OptionType.INHERIT, value = 1, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Integer piecesForBlazesIgnore;
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Boolean witherSkeletonsIgnore;
        @IntOption(type = OptionType.INHERIT, value = 2, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Integer piecesForWitherSkeletonsIgnore;
        @BooleanOption(type = OptionType.INHERIT, value = true)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Boolean piglinsEnrage;
        @IntOption(type = OptionType.INHERIT, value = 4, min = 1, max = 6)
        @TextureLocation("minecraft:textures/item/nether_brick.png")
        public Integer piecesForPiglinsEnrage;
    }
}
