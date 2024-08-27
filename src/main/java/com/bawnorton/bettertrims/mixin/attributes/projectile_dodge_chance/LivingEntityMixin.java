package com.bawnorton.bettertrims.mixin.attributes.dodge_chance;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.extend.LivingEntityExtender;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityExtender {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyReturnValue(
            method = "applyArmorToDamage",
            at = @At("RETURN")
    )
    protected float applyDodgeChance(float original, DamageSource source, float amount) {
        double chance = getAttributeValue(TrimEntityAttributes.DODGE_CHANCE) - 1;
        if(BetterTrims.PROBABILITIES.passes(chance)) {
            ChorusFruitItem chorusFruit = (ChorusFruitItem) Items.CHORUS_FRUIT;
            ItemStack chorusStack = chorusFruit.getDefaultStack();
            chorusStack.remove(DataComponentTypes.FOOD);
            chorusFruit.finishUsing(chorusStack, getWorld(), (LivingEntity) (Object) this);
            if((Object) this instanceof PlayerEntity player) {
                player.getItemCooldownManager().remove(chorusFruit);
            }
            bettertrims$setAvoidedDamage(true);
        } else {
            bettertrims$setAvoidedDamage(false);
        }
        return bettertrims$didAvoidDamage() ? 0 : original;
    }
}
