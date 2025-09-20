package com.bawnorton.bettertrims.mixin.property.item_property.damage_immunity;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.context.TrimContexts;
import com.bawnorton.bettertrims.property.element.ConditionalElementMatcher;
import com.bawnorton.bettertrims.property.element.ElementMatcher;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyComponents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(
        method = "doHurtEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;canBeHurtBy(Lnet/minecraft/world/damagesource/DamageSource;)Z"
        )
    )
    private boolean isTrimInvulnerableTo(ItemStack instance, DamageSource damageSource, Operation<Boolean> original) {
        boolean canBeHurt = original.call(instance, damageSource);
        if (!canBeHurt) return false;
        if (!(level() instanceof ServerLevel level)) return false;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (ElementMatcher<?> elementMatcher : property.getItemPropertyElements(TrimItemPropertyComponents.DAMAGE_IMMUNITY)) {
                if (elementMatcher.matches(instance, TrimContexts.damageItem(level, instance, null, damageSource))) {
                    return true;
                }
            }
        }
        return true;
    }
}
