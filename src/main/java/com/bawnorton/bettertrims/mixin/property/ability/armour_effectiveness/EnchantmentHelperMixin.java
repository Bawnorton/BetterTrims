package com.bawnorton.bettertrims.mixin.property.ability.armour_effectiveness;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
    @ModifyReturnValue(
        method = "modifyArmorEffectiveness",
        at = @At("RETURN")
    )
    private static float applyTrimToArmorEffectiveness(float original, ServerLevel level, ItemStack tool, Entity entity, DamageSource damageSource) {
        if(!(entity instanceof LivingEntity wearer)) return original;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.ARMOUR_EFFECTIVENESS)) {
                original = ability.runDamage(level, wearer, damageSource, original);
            }
        }
        return original;
    }
}
