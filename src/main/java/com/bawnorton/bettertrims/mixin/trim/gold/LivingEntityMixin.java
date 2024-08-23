package com.bawnorton.bettertrims.mixin.trim.gold;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtender {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(
            method = "getMovementSpeed()F",
            at = @At("RETURN")
    )
    protected float applyGoldTrimToMovementSpeed(float original) {
        if(!TrimEffects.GOLD.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.MOVEMENT_SPEED, original);
        return TrimEffects.GOLD.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this);
    }

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    protected float applyGoldTrimToDamage(float original) {
        if(!TrimEffects.GOLD.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.DAMAGE_AMOUNT, original);
        return TrimEffects.GOLD.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this);
    }

    @Unique
    protected float bettertrims$applyGoldTrimToAttackDamage(float original) {
        if(!TrimEffects.GOLD.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.ATTACK_DAMAGE, original);
        return TrimEffects.GOLD.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this);
    }

    @Unique
    public float bettertrims$applyGoldTrimToAttackSpeed(float original) {
        if(!TrimEffects.GOLD.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.ATTACK_SPEED, original);
        return TrimEffects.GOLD.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this);
    }

    @Unique
    public int bettertrims$applyGoldTrimToAttackCooldown(int original) {
        if(!TrimEffects.GOLD.matches(this)) return original;

        TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                .add(TrimContextParameters.ATTACK_COOLDOWN, original);
        return TrimEffects.GOLD.getApplicator().apply(new TrimContext(builder), (LivingEntity) (Object) this).intValue();
    }
}
