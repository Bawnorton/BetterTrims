package com.bawnorton.bettertrims.mixin.attributes.unbreaking;

//? if >=1.21 {
import com.bawnorton.bettertrims.extend.ItemStackExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.RemoveBinomialEnchantmentEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @ModifyReturnValue(
            method = "getItemDamage",
            at = @At("RETURN")
    )
    private static int applyUnbreaking(int original, @Local(argsOnly = true) ItemStack stack) {
        LivingEntity wearer = ((ItemStackExtender) (Object) stack).bettertrims$getWearer();
        if(wearer == null) return original;

        return (int) new RemoveBinomialEnchantmentEffect(new EnchantmentLevelBasedValue.Fraction(EnchantmentLevelBasedValue.linear(2.0F), EnchantmentLevelBasedValue.linear(10.0F, 5.0F)))
                .apply((int) wearer.getAttributeValue(TrimEntityAttributes.UNBREAKING), wearer.getRandom(), original);
    }
}
//?}
