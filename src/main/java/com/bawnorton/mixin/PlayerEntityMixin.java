package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int modifyExperience(int experience) {
        if(experience <= 0) return experience;
        float increase = 1;
        for(ItemStack stack: getArmorItems()) {
            if(ArmorTrimEffects.QUARTZ.apply(stack)) {
                increase += 0.05;
            }
        }
        return (int) (experience * increase);
    }
}
