package com.bawnorton.bettertrims.mixin.attributes.looting;

//? if >=1.21 {
/*import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantedCountIncreaseLootFunction.class)
public abstract class EnchantedCountIncreaseLootFunctionMixin {
    @ModifyExpressionValue(
            method = "process",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEquipmentLevel(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/entity/LivingEntity;)I"
            )
    )
    private int applyLooting(int original, @Local LivingEntity livingEntity) {
        if(!(livingEntity instanceof PlayerEntity player)) return original;

        return (int) (original + player.getAttributeValue(TrimEntityAttributes.LOOTING));
    }
}
*///?}