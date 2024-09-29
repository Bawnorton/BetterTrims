package com.bawnorton.bettertrims.mixin.attributes.celestial;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "getMovementSpeed()F",
            at = @At("RETURN")
    )
    protected float applyCelestialToMovementSpeed(float original) {
        return super.applyCelestialToMovementSpeed(original);
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.21 {
                    /*target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D",
                    *///?} else {
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D",
                    //?}
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            //? if >=1.21 {
                            /*target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_DAMAGE:Lnet/minecraft/registry/entry/RegistryEntry;",
                            *///?} else {
                            target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_DAMAGE:Lnet/minecraft/entity/attribute/EntityAttribute;",
                            //?}
                            opcode = Opcodes.GETSTATIC
                    )
            )
    )
    private double applyCelestialToAttackDamage(double original) {
        return super.bettertrims$applyCelestialToAttackDamage((float) original);
    }

    @ModifyExpressionValue(
            method = "getAttackCooldownProgressPerTick",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.21 {
                    /*target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"
                    *///?} else {
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"
                    //?}
            )
    )
    private double applyCelestialToAttackSpeed(double original) {
        return super.bettertrims$applyCelestialToAttackSpeed((float) original);
    }
}
