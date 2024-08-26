package com.bawnorton.bettertrims.mixin.attributes.electrifying;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(
            method = "isInvulnerableTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z"
            )
    )
    private boolean applyElectrifyingToInvulnerability(boolean isInvulnerable, DamageSource source) {
        return source.isIn(DamageTypeTags.IS_LIGHTNING) && getAttributeValue(TrimEntityAttributes.ELECTRIFYING) > 0;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void damageIfElectrified(CallbackInfo ci) {
        if(getWorld().isClient()) return;

        BlockPos feet = getBlockPos();
        BlockPos eyes = BlockPos.ofFloored(getEyePos());
        TrimEffects.COPPER.whoElectrified(feet)
                .or(() -> TrimEffects.COPPER.whoElectrified(eyes))
                .ifPresent(electrifier -> {
            DamageSource lightningDamage = getWorld().getDamageSources().create(DamageTypes.LIGHTNING_BOLT, electrifier);
            damage(lightningDamage, 2);
        });
    }
}
