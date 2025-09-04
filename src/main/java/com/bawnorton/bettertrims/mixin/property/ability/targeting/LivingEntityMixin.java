package com.bawnorton.bettertrims.mixin.property.ability.targeting;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityRunner;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.List;

@MixinEnvironment
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(
            method = "canAttack",
            at = @At("RETURN")
    )
    private boolean doTrimsAllowTargeting(boolean original, LivingEntity target) {
        if (!original) return false;

        for(TrimProperty property : TrimProperties.getProperties(level())) {
            List<TrimAbilityRunner> abilities = property.abilityHolders();
            for(TrimAbilityRunner ability : abilities) {
                if(ability.canTarget(target, (LivingEntity) (Object) this)) continue;

                return false;
            }
        }
        return true;
    }
}
