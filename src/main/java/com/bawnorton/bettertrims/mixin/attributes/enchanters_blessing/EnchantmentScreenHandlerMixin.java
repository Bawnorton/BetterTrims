package com.bawnorton.bettertrims.mixin.attributes.enchanters_blessing;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.mixin.accessor.PlayerEntityAccessor;
import com.bawnorton.bettertrims.registry.content.TrimCriteria;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.21
import com.bawnorton.bettertrims.registry.content.TrimComponentTypes;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
    @Shadow @Final private Inventory inventory;

    @Shadow public abstract void onContentChanged(Inventory inventory);

    @Shadow @Final private ScreenHandlerContext context;

    @Shadow @Final private Property seed;

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @WrapOperation(
            method = "onButtonClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;error(Ljava/lang/String;)V"
            )
    )
    private void handleRerollClick(String message, Operation<Void> original, PlayerEntity player, int buttonId, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        if (buttonId != 67) {
            original.call(message);
            return;
        }

        cir.setReturnValue(true);
        this.context.run((world, pos) -> {
            ItemStack enchanting = inventory.getStack(0);
            //? if >=1.21 {
            int usedBlessings = enchanting.getOrDefault(TrimComponentTypes.USED_BLESSINGS, 0);
            //?} else {
            /*int usedBlessings = 0;
            NbtCompound nbt = enchanting.getNbt();
            if(nbt != null) {
                usedBlessings = nbt.getInt("bettertrims$used_blessings");
            }
            *///?}
            usedBlessings++;
            if(usedBlessings >= 4 && player instanceof ServerPlayerEntity serverPlayer) {
                TrimCriteria.ENCHANTERS_FAVOUR_MAX_REROLLS.trigger(serverPlayer);
            }
            if (usedBlessings > player.getAttributeValue(TrimEntityAttributes.ENCHANTERS_FAVOUR) * AttributeSettings.EnchantersFavour.rerolls) {
                cir.setReturnValue(false);
                return;
            }

            //? if >=1.21 {
            enchanting.set(TrimComponentTypes.USED_BLESSINGS, usedBlessings);
            //?} else {
            /*if(nbt == null) {
                nbt = new NbtCompound();
            }
            nbt.putInt("bettertrims$used_blessings", usedBlessings);
            enchanting.setNbt(nbt);
            *///?}
            inventory.setStack(0, enchanting);
            ((PlayerEntityAccessor) player).setEnchantmentTableSeed(player.getRandom().nextInt());
            seed.set(player.getEnchantmentTableSeed());
            inventory.markDirty();
            world.playSound(null, pos, SoundEvents.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
        });
    }
}
