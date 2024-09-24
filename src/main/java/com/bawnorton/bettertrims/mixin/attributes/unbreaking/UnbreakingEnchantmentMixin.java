package com.bawnorton.bettertrims.mixin.attributes.unbreaking;

//? if <1.21 {
/*import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(UnbreakingEnchantment.class)
public abstract class UnbreakingEnchantmentMixin {
    @ModifyVariable(
            method = "shouldPreventDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static int applyUnbreaking(int original, @Local(argsOnly = true) ItemStack stack) {
        LivingEntity wearer = ((ItemStackExtender) (Object) stack).bettertrims$getWearer();
        if(wearer == null) return original;

        int level = (int) wearer.getAttributeValue(TrimEntityAttributes.UNBREAKING);
        return original + level;
    }
}
*///?}
