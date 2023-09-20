package com.bawnorton.bettertrims.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends MobEntityMixin {
    @Override
    protected boolean shouldHit(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (didDodgeAttack(instance)) return false;
        return super.shouldHit(instance, source, amount, original);
    }
}
