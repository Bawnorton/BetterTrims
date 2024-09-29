package com.bawnorton.bettertrims.mixin.attributes.oxygen_bonus;

//? if <1.21 {
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @ModifyExpressionValue(
            method = "getNextAirUnderwater",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getRespiration(Lnet/minecraft/entity/LivingEntity;)I"
            )
    )
    private int applyOxygenBonus(int original) {
        return original + (int) getAttributeValue(TrimEntityAttributes.GENERIC_OXYGEN_BONUS);
    }
}
//?}
