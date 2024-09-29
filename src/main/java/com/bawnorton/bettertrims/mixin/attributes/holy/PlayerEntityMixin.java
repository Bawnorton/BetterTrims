package com.bawnorton.bettertrims.mixin.attributes.holy;

//? if <1.21 {
/*import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F",
                    ordinal = 0
            )
    )
    private float applyHoly(float original, Entity target) {
        if(!(target instanceof LivingEntity livingEntity)) return original;
        if(livingEntity.getGroup() != EntityGroup.UNDEAD) return original;

        return original + (float) getAttributeValue(TrimEntityAttributes.HOLY) * AttributeSettings.Holy.damage;
    }
}
*///?}