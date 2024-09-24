package com.bawnorton.bettertrims.mixin.attributes.electrifying;

import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract boolean isDead();

    protected boolean applyElectrifyingToInvulnerability(boolean original, DamageSource source) {
        return original || source.isIn(DamageTypeTags.IS_LIGHTNING) && getAttributeValue(TrimEntityAttributes.ELECTRIFYING) > 0;
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
            if(isDead() && electrifier instanceof ServerPlayerEntity player) {
                TrimCriteria.KILLED_WITH_ELECTRICITY.trigger(player);
            }
        });
    }
}
