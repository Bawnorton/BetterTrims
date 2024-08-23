package com.bawnorton.bettertrims.mixin.trim.lapis;

import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.context.TrimContext;
import com.bawnorton.bettertrims.effect.context.TrimContextParameterSet;
import com.bawnorton.bettertrims.effect.context.TrimContextParameters;
import com.bawnorton.bettertrims.mixin.accessor.PlayerEntityAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
            TrimContextParameterSet.Builder builder = TrimContextParameterSet.builder()
                    .add(TrimContextParameters.ITEM_STACK, enchanting);
            ItemStack result = TrimEffects.LAPIS.getApplicator().apply(new TrimContext(builder), player);
            if(result == null) {
                cir.setReturnValue(false);
                return;
            }
            inventory.setStack(0, result);
            ((PlayerEntityAccessor) player).setEnchantmentTableSeed(player.getRandom().nextInt());
            seed.set(player.getEnchantmentTableSeed());
            inventory.markDirty();
            world.playSound(null, pos, SoundEvents.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
        });
    }
}
