package com.bawnorton.bettertrims.mixin.property.ability.damage;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
    @ModifyReturnValue(
        method = "modifyDamage",
        at = @At("RETURN")
    )
    private static float applyTrimToDamage(float original, ServerLevel level, ItemStack tool, Entity entity, DamageSource source) {
        if(!(source.getEntity() instanceof LivingEntity wearer)) return original;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.DAMAGE)) {
                original = ability.runDamage(level, wearer, source, original);
            }
        }
        return original;
    }
}
