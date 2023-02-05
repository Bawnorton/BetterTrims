package com.bawnorton.mixin;

import com.bawnorton.BetterTrims;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    private boolean isFireImmune(boolean original) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(getArmorItems(), stack -> netheriteCount.set(netheriteCount.get() + BetterTrims.CONFIG.netheriteFireResistance));
        return original || netheriteCount.get() >= 0.99f;
    }

    @ModifyArg(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float setOnFireFromLava(float original) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(getArmorItems(), stack -> netheriteCount.set(netheriteCount.get() + BetterTrims.CONFIG.netheriteFireResistance));
        return original * (1 - netheriteCount.get());
    }

    @ModifyArg(method = "setOnFireFor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setFireTicks(I)V"))
    private int setFireTicks(int original) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(getArmorItems(), stack -> netheriteCount.set(netheriteCount.get() + BetterTrims.CONFIG.netheriteFireResistance));
        return (int) (original * (1 - netheriteCount.get()));
    }
}
