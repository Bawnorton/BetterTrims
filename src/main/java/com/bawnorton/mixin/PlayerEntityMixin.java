package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int modifyExperience(int experience) {
        if (experience <= 0) return experience;
        Wrapper<Float> increase = Wrapper.of(1F);
        ArmorTrimEffects.QUARTZ.apply(getArmorItems(), stack -> increase.set(increase.get() + BetterTrims.CONFIG.quartzExperienceBonus));
        return (int) (experience * increase.get());
    }

    @WrapOperation(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F"))
    private float modifyMiningSpeed(PlayerInventory instance, BlockState block, Operation<Float> original) {
        Wrapper<Float> increase = Wrapper.of(original.call(instance, block));
        ArmorTrimEffects.IRON.apply(getArmorItems(), stack -> {
            if(instance.getMainHandStack().isSuitableFor(block)) {
                increase.set(increase.get() + BetterTrims.CONFIG.ironMiningSpeedIncrease);
            }
        });
        return increase.get();
    }

    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    private void modifyMovementSpeed(CallbackInfoReturnable<Float> cir) {
        Wrapper<Float> increase = Wrapper.of(1f);
        ArmorTrimEffects.REDSTONE.apply(getArmorItems(), stack -> increase.set(increase.get() + BetterTrims.CONFIG.redstoneMovementSpeedIncrease));
        cir.setReturnValue(cir.getReturnValue() * increase.get());
    }
}
