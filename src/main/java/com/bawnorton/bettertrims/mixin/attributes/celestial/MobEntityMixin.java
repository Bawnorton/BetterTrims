package com.bawnorton.bettertrims.mixin.attributes.celestial;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntityMixin {
    public MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(
            method = "tryAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_DAMAGE:Lnet/minecraft/registry/entry/RegistryEntry;",
                            opcode = Opcodes.GETSTATIC
                    )
            )
    )
    private double applyGoldTrimToAttackDamage(double original) {
        return super.bettertrims$applyCelestialToAttackDamage((float) original);
    }
}
