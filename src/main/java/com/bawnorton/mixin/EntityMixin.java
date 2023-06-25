package com.bawnorton.mixin;

import com.bawnorton.config.Config;
import com.bawnorton.effect.ArmorTrimEffects;
import com.bawnorton.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(getArmorItems(), stack -> netheriteCount.set(netheriteCount.get() + Config.getInstance().netheriteFireResistance));
        cir.setReturnValue(cir.getReturnValue() || netheriteCount.get() >= 0.99f);
    }

    @ModifyArg(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float modifyDamage(float original) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(getArmorItems(), stack -> netheriteCount.set(netheriteCount.get() + Config.getInstance().netheriteFireResistance));
        return original * (1 - netheriteCount.get());
    }
}
