package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.effect.TrimEffects;
import com.bawnorton.bettertrims.effect.attribute.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static DefaultAttributeContainer.Builder addTrimAttributes(DefaultAttributeContainer.Builder original) {
        original.add(TrimEntityAttributes.BREWERS_DREAM);
        original.add(TrimEntityAttributes.ELECTRIFYING);
        original.add(TrimEntityAttributes.SUNS_BLESSING);
        original.add(TrimEntityAttributes.ITEM_MAGNET);
        original.add(TrimEntityAttributes.FIRE_RESISTANCE);
        original.add(TrimEntityAttributes.RESISTANCE);
        return original;
    }
 
    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void writeEffectData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound betterTrimsContainer = new NbtCompound();
        nbt.put(BetterTrims.MOD_ID, betterTrimsContainer);
        TrimEffects.forEachTrimEffect((tag, trimEffect) -> betterTrimsContainer.put(tag.id().toString(), trimEffect.writeNbt((LivingEntity) (Object) this, new NbtCompound())));
    }
    
    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    private void readEffectData(NbtCompound nbt, CallbackInfo ci) {
        if(!nbt.contains(BetterTrims.MOD_ID)) return;
        
        NbtCompound betterTrimsContainer = nbt.getCompound(BetterTrims.MOD_ID);
        TrimEffects.forEachTrimEffect((tag, trimEffect) -> {
            String id = tag.id().toString();
            if (betterTrimsContainer.contains(id)) {
                NbtCompound effectNbt = betterTrimsContainer.getCompound(id);
                trimEffect.readNbt((LivingEntity) (Object) this, effectNbt);
            }
        });
    }
}
