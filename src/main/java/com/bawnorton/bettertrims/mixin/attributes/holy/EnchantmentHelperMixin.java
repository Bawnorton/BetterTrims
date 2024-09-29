package com.bawnorton.bettertrims.mixin.attributes.holy;

//? if >=1.21 {
/*import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @ModifyReturnValue(
            method = "getDamage",
            at = @At("RETURN")
    )
    private static float applyHoly(float original, ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseDamage) {
        if(!target.getType().isIn(EntityTypeTags.UNDEAD)) return original;
        if(!(damageSource.getAttacker() instanceof LivingEntity attacker)) return original;

        return original + (float) attacker.getAttributeValue(TrimEntityAttributes.HOLY) * AttributeSettings.Holy.damage;
    }
}
*///?}
