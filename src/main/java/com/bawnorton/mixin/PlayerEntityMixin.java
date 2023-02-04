package com.bawnorton.mixin;

import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int modifyExperience(int experience) {
        if(experience <= 0) return experience;
        Wrapper<Float> increase = Wrapper.of(1F);
        ArmorTrimEffects.QUARTZ.apply(getArmorItems(), stack -> increase.set(increase.get() + 0.05F));
        return (int) (experience * increase.get());
    }

    @Redirect(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F"))
    private float modifyMiningSpeed(PlayerInventory instance, BlockState block) {
        Wrapper<Float> increase = Wrapper.of(instance.getBlockBreakingSpeed(block));
        ArmorTrimEffects.IRON.apply(getArmorItems(), stack -> increase.set(increase.get() + 8));
        return increase.get();
    }

    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    private void modifyMovementSpeed(CallbackInfoReturnable<Float> cir) {
        Wrapper<Float> increase = Wrapper.of(1F);
        ArmorTrimEffects.REDSTONE.apply(getArmorItems(), stack -> increase.set(increase.get() + 0.1F));
        cir.setReturnValue(cir.getReturnValue() * increase.get());
    }
}
