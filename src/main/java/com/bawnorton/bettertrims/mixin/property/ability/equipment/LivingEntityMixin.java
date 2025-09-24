package com.bawnorton.bettertrims.mixin.property.ability.equipment;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import com.bawnorton.bettertrims.property.ability.runner.TrimToggleAbilityRunner;
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

    //? if 1.21.8 {
    @Inject(
        method = "stopLocationBasedEffects",
        at = @At("TAIL")
    )
    private void stopTrimEquipmentBasedEffects(ItemStack stack, EquipmentSlot slot, AttributeMap attributeMap, CallbackInfo ci) {
    //?} elif 1.21.1 {
    /*@WrapOperation(
        method = "collectEquipmentChanges",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
        )
    )
    private void stopTrimEquipmentBasedEffects(ItemStack stack, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> action, Operation<Void> original) {
        original.call(stack, slot, action);
    *///?}
        if (!(level() instanceof ServerLevel level)) return;

        LivingEntity self = (LivingEntity) (Object) this;
        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for(TrimToggleAbilityRunner<?> ability : property.getToggleAbilityRunners(TrimAbilityComponents.EQUIPPED)) {
                ability.runStop(level, self, stack, slot);
            }
        }
    }

    //? if 1.21.8 {
    @WrapOperation(
        method = "collectEquipmentChanges",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;runLocationChangedEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
        )
    )
    private void startTrimLocationBasedEffects(ServerLevel level, ItemStack stack, LivingEntity entity, EquipmentSlot slot, Operation<Void> original) {
        original.call(level, stack, entity, slot);
    //?} elif 1.21.1 {
    /*@WrapOperation(
        method = "collectEquipmentChanges",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
        )
    )
    private void startTrimLocationBasedEffects(ItemStack stack, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> action, Operation<Void> original) {
        original.call(stack, slot, action);
        if (!(level() instanceof ServerLevel level)) return;
    *///?}
        LivingEntity self = (LivingEntity) (Object) this;
        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for(TrimToggleAbilityRunner<?> ability : property.getToggleAbilityRunners(TrimAbilityComponents.EQUIPPED)) {
                ability.runStart(level, self);
            }
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
            for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.TICK)) {
                ability.runTick(level, entity, entity, position());
            }
            for (TrimToggleAbilityRunner<?> ability : property.getToggleAbilityRunners(TrimAbilityComponents.EQUIPPED)) {
                ability.update(level, entity);
            }
            if(tickCount % (int) level.tickRateManager().tickrate() == 0) {
                for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.SECOND)) {
                    ability.runTick(level, entity, entity, position());
                }
            }
        }
    }
}
