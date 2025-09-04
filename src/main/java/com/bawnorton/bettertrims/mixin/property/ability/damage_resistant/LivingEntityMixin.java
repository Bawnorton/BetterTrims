package com.bawnorton.bettertrims.mixin.property.ability.damage_resistant;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityRunner;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(
            method = "isInvulnerableTo",
            at = @At("RETURN")
    )
    private boolean isTrimInvulnerableTo(boolean original, ServerLevel level, DamageSource damageSource) {
        if(original) return true;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for(TrimAbilityRunner ability : property.abilityHolders()) {
                if(ability.isInvulnerableTo((LivingEntity) (Object) (this), damageSource)) {
                    return true;
                }
            }
        }
        return false;
    }
}
