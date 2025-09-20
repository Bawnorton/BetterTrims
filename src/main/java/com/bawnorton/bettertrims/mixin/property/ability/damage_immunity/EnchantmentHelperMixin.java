package com.bawnorton.bettertrims.mixin.property.ability.damage_immunity;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
    @ModifyReturnValue(
        method = "isImmuneToDamage",
        at = @At("RETURN")
    )
    private static boolean isTrimInvulnerableTo(boolean original, ServerLevel level, LivingEntity entity, DamageSource damageSource) {
        if (original) return true;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (ElementMatcher<?> ability : property.getAbilityElements(TrimAbilityComponents.DAMAGE_IMMUNITY)) {
                return ability.matches(entity, TrimContexts.damage(level, ability.getMatchingItems(entity), entity, damageSource));
            }
        }
        return false;
    }
}
