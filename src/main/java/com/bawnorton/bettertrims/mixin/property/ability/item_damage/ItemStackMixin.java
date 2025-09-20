package com.bawnorton.bettertrims.mixin.property.ability.item_damage;

import com.bawnorton.bettertrims.property.TrimProperties;
import com.bawnorton.bettertrims.property.TrimProperty;
import com.bawnorton.bettertrims.property.ability.TrimAbilityComponents;
import com.bawnorton.bettertrims.property.ability.runner.TrimValueAbilityRunner;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment
@Mixin(ItemStack.class)
abstract class ItemStackMixin {
    @ModifyExpressionValue(
        //? if 1.21.8 {
        method = "processDurabilityChange",
        //?} elif 1.21.1 {
        /*method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
        *///?}
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processDurabilityChange(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;I)I"
        )
    )
    private int applyTrimToItemDamage(int original, @Local(argsOnly = true) ServerLevel level, @Local(argsOnly = true) @Nullable ServerPlayer player) {
        if (player == null) return original;

        for(TrimProperty property : TrimProperties.getProperties(level)) {
            for (TrimValueAbilityRunner<?> ability : property.getValueAbilityRunners(TrimAbilityComponents.ITEM_DAMAGE)) {
                original = (int) ability.runEquipment(level, player, (ItemStack) (Object) this, original);
            }
        }
        return original;
    }
}
