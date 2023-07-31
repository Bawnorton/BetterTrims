package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.IterHelper;
import com.bawnorton.bettertrims.util.Wrapper;
import com.bawnorton.bettertrims.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtender {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), stack -> netheriteCount.set(netheriteCount.get() + Config.getInstance().netheriteFireResistance));
        cir.setReturnValue(cir.getReturnValue() || netheriteCount.get() >= 0.99f);
    }

    @ModifyArg(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float modifyDamage(float original) {
        Wrapper<Float> netheriteCount = Wrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), stack -> netheriteCount.set(netheriteCount.get() + Config.getInstance().netheriteFireResistance));
        return original * (1 - netheriteCount.get());
    }

    @Unique
    public Iterable<ItemStack> betterTrims$getTrimmables() {
        List<ItemStack> handItems = IterHelper.toList(getHandItems());
        handItems.removeIf(stack -> stack.getItem() instanceof ArmorItem);
        return IterHelper.combine(getArmorItems(), handItems);
    }
}
