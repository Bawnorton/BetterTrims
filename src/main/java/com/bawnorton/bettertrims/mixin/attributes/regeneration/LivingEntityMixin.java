package com.bawnorton.bettertrims.mixin.attributes.regeneration;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract void heal(float amount);

    @Shadow public abstract float getHealth();

    @Shadow public abstract float getMaxHealth();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;tickStatusEffects()V"
            )
    )
    private void applyRegeneration(CallbackInfo ci) {
        double regen = getAttributeValue(TrimEntityAttributes.REGENERATION);
        if(regen <= 0) return;

        int delay = (int) (50 / Math.pow(2, regen));
        if (getWorld().getTimeOfDay() % delay == 0 && getHealth() < getMaxHealth()) {
            heal(1);
        }
    }
}
