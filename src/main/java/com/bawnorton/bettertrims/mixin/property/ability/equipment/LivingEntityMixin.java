package com.bawnorton.bettertrims.mixin.property.ability.equipment;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "stopLocationBasedEffects",
            at = @At("TAIL")
    )
    private void stopTrimLocationBasedEffects(ItemStack stack, EquipmentSlot slot, AttributeMap attributeMap, CallbackInfo ci) {
        for(TrimProperty property : TrimProperties.getProperties(level())) {
            property.abilityHolders().forEach(holder -> holder.stop((LivingEntity) (Object) this, stack, slot));
        }
    }

    @WrapOperation(
            method = "collectEquipmentChanges",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;runLocationChangedEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
            )
    )
    private void startTrimLocationBasedEffects(ServerLevel level, ItemStack stack, LivingEntity entity, EquipmentSlot slot, Operation<Void> original) {
        original.call(level, stack, entity, slot);
        for(TrimProperty property : TrimProperties.getProperties(level())) {
            property.abilityHolders().forEach(holder -> holder.start(entity));
        }
    }

    @WrapOperation(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;tickEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void tickTrimEffects(ServerLevel level, LivingEntity entity, Operation<Void> original) {
        original.call(level, entity);
        for(TrimProperty property : TrimProperties.getProperties(level)) {
            property.abilityHolders().forEach(holder -> holder.tick(entity));
        }
    }
}
