package com.bawnorton.bettertrims.mixin.property.item_property.damage_resistant;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyRunner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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

        for(TrimProperty property : TrimProperties.getProperties(level())) {
            List<TrimItemPropertyRunner> itemProperties = property.propertyHolders();
            for(TrimItemPropertyRunner itemProperty : itemProperties) {
                if(itemProperty.isInvulnerableTo(instance, damageSource)) {
                    return false;
                }
            }
        }
        return true;
    }
}
