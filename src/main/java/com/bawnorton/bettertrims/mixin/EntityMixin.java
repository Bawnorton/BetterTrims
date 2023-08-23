package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.effect.ArmorTrimEffects;
import com.bawnorton.bettertrims.extend.EntityExtender;
import com.bawnorton.bettertrims.util.NumberWrapper;
import com.bawnorton.bettertrims.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtender {
    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract Iterable<ItemStack> getHandItems();

    @Shadow public abstract World getWorld();

    @Inject(method = "isFireImmune", at = @At("RETURN"), cancellable = true)
    private void checkIfNetheriteTrimmed(CallbackInfoReturnable<Boolean> cir) {
        NumberWrapper netheriteCount = NumberWrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), stack -> netheriteCount.increment(Config.getInstance().netheriteFireResistance));
        cir.setReturnValue(cir.getReturnValue() || netheriteCount.getFloat() >= 0.99f);
    }

    @ModifyArg(method = "setOnFireFromLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float reduceNetheriteTrimDamage(float original) {
        NumberWrapper netheriteCount = NumberWrapper.of(0f);
        ArmorTrimEffects.NETHERITE.apply(betterTrims$getTrimmables(), stack -> netheriteCount.increment(Config.getInstance().netheriteFireResistance));
        return original * (1 - netheriteCount.getFloat());
    }

    @Unique
    public List<ItemStack> betterTrims$getTrimmables() {
        List<ItemStack> equipped = new ArrayList<>();
        for(ItemStack stack: getHandItems()) equipped.add(stack);
        equipped.removeIf(stack -> stack.getItem() instanceof ArmorItem);
        for(ItemStack stack: getArmorItems()) equipped.add(stack);
        equipped.removeIf(ItemStack::isEmpty);
        return equipped;
    }

    @Unique
    public boolean betterTrims$shouldSilverApply() {
        long time = getWorld().getTimeOfDay() % 24000;
        return time >= 13000 && time <= 23000;
    }
}
