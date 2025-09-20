package com.bawnorton.bettertrims.mixin.property.ability.hit_block;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimEntityAbilityRunner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
    @Inject(
        method = "onHitBlock",
        at = @At("TAIL")
    )
    private static void doOnHitBlockTrimAbilites(ServerLevel level, ItemStack stack, LivingEntity owner, Entity entity, EquipmentSlot slot, Vec3 pos, BlockState state, Consumer<Item> onBreak, CallbackInfo ci) {
        if(owner == null) return;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (TrimEntityAbilityRunner<?> ability : property.getEntityAbilityRunners(TrimAbilityComponents.HIT_BLOCK)) {
                ability.runHitBlock(level, owner, entity, pos, state, stack);
            }
        }
    }
}
