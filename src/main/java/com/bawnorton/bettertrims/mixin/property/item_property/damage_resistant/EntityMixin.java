package com.bawnorton.bettertrims.mixin.property.item_property.damage_resistant;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.item.TrimItemPropertyRunner;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(Entity.class)
abstract class EntityMixin {
    @Shadow
    private Level level;

    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(
            method = "isInvulnerableToBase",
            at = @At("RETURN")
    )
    private boolean isTrimInvulnerableTo(boolean original, DamageSource damageSource) {
        if(original) return true;
        if (!((Object) this instanceof ItemEntity itemEntity)) return false;

        ItemStack stack = itemEntity.getItem();
        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for(TrimItemPropertyRunner itemProperty : property.propertyHolders()) {
                if(itemProperty.isInvulnerableTo(stack, damageSource)) {
                    return true;
                }
            }
        }
        return false;
    }
}
