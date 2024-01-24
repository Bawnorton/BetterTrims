package com.bawnorton.bettertrims.config;

import com.bawnorton.bettertrims.config.annotation.*;

@Groups({
        "game",
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})
public class Config {
    private static Config LOCAL_INSTANCE = new Config();
    private static Config SERVER_INSTANCE = new Config();

    @IntOption(group = "game", value = 1, min = 1)
    public Integer trimDurability;
    @FloatOption(group = "vanilla", value = 0.05f, max = 1)
    @TextureLocation("minecraft:textures/item/quartz.png")
    public Float quartzExperienceBonus;
    @FloatOption(group = "vanilla", value = 0.5f, max = 5)
    @TextureLocation("minecraft:textures/item/iron_ingot.png")
    public Float ironMiningSpeedIncrease;
    @FloatOption(group = "vanilla", value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/netherite_ingot.png")
    public Float netheriteFireResistance;
    @FloatOption(group = "vanilla", value = 0.1f, max = 1)
    @TextureLocation("minecraft:textures/item/redstone.png")
    public Float redstoneMovementSpeedIncrease;
    @FloatOption(group = "vanilla", value = 0.05f, max = 0.5f)
    @TextureLocation("minecraft:textures/item/copper_ingot.png")
    public Float copperSwimSpeedIncrease;
    @FloatOption(group = "vanilla", value = 0.125f, max = 1)
    @TextureLocation("minecraft:textures/item/emerald.png")
    public Float emeraldVillagerDiscount;
    @FloatOption(group = "vanilla", value = 0.05f, max = 1)
    @TextureLocation("minecraft:textures/item/diamond.png")
    public Float diamondDamageReduction;
    @IntOption(group = "vanilla", value = 30)
    @TextureLocation("minecraft:textures/item/lapis_lazuli.png")
    public Integer lapisEnchantability;
    @FloatOption(group = "vanilla", value = 0.0625f, max = 1)
    @TextureLocation("minecraft:textures/item/amethyst_shard.png")
    public Float amethystPotionDurationModifyChance;

    @FloatOption(group = "added_vanilla", value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/glowstone_dust.png")
    public Float glowstonePotionAmplifierIncreaseChance;
    @FloatOption(group = "added_vanilla", value = 0.25f, max = 1)
    @TextureLocation("minecraft:textures/item/chorus_fruit.png")
    public Float chorusFruitDodgeChance;
    @FloatOption(group = "added_vanilla", value = 1f, max = 10)
    @TextureLocation("minecraft:textures/item/fire_charge.png")
    public Float fireChargeFireDuration;
    @FloatOption(group = "added_vanilla", value = 0.4f, max = 4)
    @TextureLocation("minecraft:textures/item/leather.png")
    public Float leatherStepHeightIncrease;
    @FloatOption(group = "added_vanilla", value = 1.25f, max = 5)
    @TextureLocation("minecraft:textures/item/dragon_breath.png")
    public Float dragonBreathRadius;
    @FloatOption(group = "added_vanilla", value = 1.5f, max = 5)
    @TextureLocation("minecraft:textures/item/echo_shard.png")
    public Float echoShardVibrationDistanceReduction;

    @NestedOption(group = "vanilla")
    @TextureLocation("minecraft:textures/item/gold_ingot.png")
    public Gold goldEffects;
    @NestedOption(group = "added_vanilla")
    @TextureLocation("minecraft:textures/item/prismarine_shard.png")
    public PrismarineShard prismarineShardEffects;
    @NestedOption(group = "added_vanilla")
    @TextureLocation("minecraft:textures/item/golden_apple.png")
    public EnchantedGoldenApple enchantedGoldenAppleEffects;
    @NestedOption(group = "added_vanilla")
    @TextureLocation("minecraft:textures/item/slime_ball.png")
    public SlimeBall slimeBallEffects;
    @NestedOption(group = "added_vanilla")
    @TextureLocation("minecraft:textures/item/coal.png")
    public Coal coalEffects;
    @NestedOption(group = "added_vanilla")
    @TextureLocation("minecraft:textures/item/ender_pearl.png")
    public EnderPearl enderPearlEffects;
    @NestedOption(group = "added_vanilla")
    @TextureLocation("minecraft:textures/item/nether_brick.png")
    public NetherBrick netherBrickEffects;
    @NestedOption(group = "modded")
    @TextureLocation(value = "silver", effectLookup = true)
    public Silver silverEffects;
    @NestedOption(group = "modded")
    @TextureLocation(value = "platinum", effectLookup = true)
    public Platinum platinumEffects;

    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/quartz.png")
    public Boolean enableQuartz;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/iron_ingot.png")
    public Boolean enableIron;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/netherite_ingot.png")
    public Boolean enableNetherite;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/redstone.png")
    public Boolean enableRedstone;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/copper_ingot.png")
    public Boolean enableCopper;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/gold_ingot.png")
    public Boolean enableGold;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/emerald.png")
    public Boolean enableEmerald;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/diamond.png")
    public Boolean enableDiamond;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/lapis_lazuli.png")
    public Boolean enableLapis;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/amethyst_shard.png")
    public Boolean enableAmethyst;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/coal.png")
    public Boolean enableCoal;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/dragon_breath.png")
    public Boolean enableDragonsBreath;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/chorus_fruit.png")
    public Boolean enableChorusFruit;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/echo_shard.png")
    public Boolean enableEchoShard;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/ender_pearl.png")
    public Boolean enableEnderPearl;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/fire_charge.png")
    public Boolean enableFireCharge;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/glowstone_dust.png")
    public Boolean enableGlowstoneDust;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/leather.png")
    public Boolean enableLeather;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/nether_brick.png")
    public Boolean enableNetherBrick;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/prismarine_shard.png")
    public Boolean enablePrismarineShard;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/rabbit_hide.png")
    public Boolean enableRabbitHide;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/slime_ball.png")
    public Boolean enableSlimeBall;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation("minecraft:textures/item/golden_apple.png")
    public Boolean enableEnchantedGoldenApple;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation(value = "silver", effectLookup = true)
    public Boolean enableSilver;
    @BooleanOption(group = "toggles", value = true)
    @TextureLocation(value = "platinum", effectLookup = true)
    public Boolean enablePlatinum;


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

    public static class EnchantedGoldenApple {
        @FloatOption(group = "inherit", value = 1200, max = 12000, min = 1)
        public Float absorptionDelay;
        @FloatOption(group = "inherit", value = 250, max = 12000)
        public Float absorptionDelayReduction;
        @IntOption(group = "inherit", value = 2, max = 20)
        public Integer absorptionAmount;
        @IntOption(group = "inherit", value = 6, max = 30)
        public Integer maxAbsorption;
    }

    public static class Silver {
        @FloatOption(group = "inherit", value = 0.05f, max = 0.5f)
        public Float movementSpeed;
        @FloatOption(group = "inherit", value = 0.05f, max = 0.5f)
        public Float jumpHeight;
        @FloatOption(group = "inherit", value = 0.5f, max = 5)
        public Float attackDamage;
        @FloatOption(group = "inherit", value = 0.3f, max = 3)
        public Float attackSpeed;
        @FloatOption(group = "inherit", value = 0.03f, max = 0.3f)
        public Float damageReduction;
        @FloatOption(group = "inherit", value = 0.25f, max = 1)
        public Float improveVision;
        @BooleanOption(group = "inherit", value = true)
        public Boolean applyInFixedTime;
    }

    public static class SlimeBall {
        @FloatOption(group = "inherit", value = 0.25f, max = 1)
        public Float fallDamageReduction;
        @FloatOption(group = "inherit", value = 1, max = 5f)
        public Float knockbackIncrease;
        @BooleanOption(group = "inherit", value = true)
        public Boolean bounce;
    }

    public static class Coal {
        @FloatOption(group = "inherit", value = 5f, max = 10f)
        public Float playerDetectionRadius;
        @IntOption(group = "inherit", value = 1, max = 10)
        public Integer furnaceSpeedIncrease;
    }

    public static class EnderPearl {
        @FloatOption(group = "inherit", value = 0.25f, max = 1)
        public Float dodgeChance;
        @BooleanOption(group = "inherit", value = true)
        public Boolean waterDamagesUser;
    }

    public static class Platinum {
        @BooleanOption(group = "inherit", value = true)
        public Boolean illagersIgnore;
        @IntOption(group = "inherit", value = 1, min = 1, max = 6)
        public Integer piecesForIllagersIgnore;
    }

    public static class PrismarineShard {
        @BooleanOption(group = "inherit", value = true)
        public Boolean guardiansIgnore;
        @IntOption(group = "inherit", value = 2, min = 1, max = 6)
        public Integer piecesForGuardiansIgnore;
        @BooleanOption(group = "inherit", value = true)
        public Boolean miningFatigueImmunity;
        @IntOption(group = "inherit", value = 4, min = 1, max = 6)
        public Integer piecesForMiningFatigueImmunity;
    }

    public static class Gold {
        @BooleanOption(group = "inherit", value = true)
        public Boolean piglinsIgnore;
        @IntOption(group = "inherit", value = 1, min = 1, max = 6)
        public Integer piecesForPiglinsIgnore;
    }

    public static class NetherBrick {
        @BooleanOption(group = "inherit", value = true)
        public Boolean blazesIgnore;
        @IntOption(group = "inherit", value = 1, min = 1, max = 6)
        public Integer piecesForBlazesIgnore;
        @BooleanOption(group = "inherit", value = true)
        public Boolean witherSkeletonsIgnore;
        @IntOption(group = "inherit", value = 2, min = 1, max = 6)
        public Integer piecesForWitherSkeletonsIgnore;
        @BooleanOption(group = "inherit", value = true)
        public Boolean piglinsEnrage;
        @IntOption(group = "inherit", value = 2, min = 1, max = 6)
        public Integer piecesForPiglinsEnrage;
    }
}
