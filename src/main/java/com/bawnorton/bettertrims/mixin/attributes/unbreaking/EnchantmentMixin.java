package com.bawnorton.bettertrims.mixin.attributes.unbreaking;

//? if >=1.21 {
import com.bawnorton.bettertrims.extend.ItemStackExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @ModifyVariable(
            method = "modifyItemDamage",
            at = @At("HEAD"),
            argsOnly = true
    )
    private int applyUnbreaking(int original, @Local(argsOnly = true) ItemStack stack) {
        LivingEntity wearer = ((ItemStackExtender) (Object) stack).bettertrims$getWearer();
        if(wearer == null) return original;

        int level = (int) wearer.getAttributeValue(TrimEntityAttributes.UNBREAKING);
        return original + level;
    }
}
//?}
