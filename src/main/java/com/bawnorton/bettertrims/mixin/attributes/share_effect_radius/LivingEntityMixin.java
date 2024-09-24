package com.bawnorton.bettertrims.mixin.attributes.share_effect_radius;

import com.bawnorton.bettertrims.extend.AreaEffectCloudEntityExtender;
import com.bawnorton.bettertrims.mixin.accessor.StatusEffectInstanceAccessor;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract Map<RegistryEntry<StatusEffect>, StatusEffectInstance> getActiveStatusEffects();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "tickStatusEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;",
                    remap = false
            )
    )
    private void applyShareEffectRadius(CallbackInfo ci) {
        if(getWorld().isClient()) return;

        World world = getWorld();
        double shareEffectRadius = getAttributeValue(TrimEntityAttributes.SHARE_EFFECT_RADIUS);
        if(shareEffectRadius <= 0) return;

        AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(world, getX(), getY(), getZ());
        getActiveStatusEffects()
                .values()
                .forEach(effect -> {
                    StatusEffectInstance copy = new StatusEffectInstance(effect);
                    ((StatusEffectInstanceAccessor) copy).setDuration(2);
                    areaEffectCloud.addEffect(copy);
                });
        areaEffectCloud.setOwner((LivingEntity) (Object) this);
        areaEffectCloud.setRadius((float) shareEffectRadius);
        areaEffectCloud.setDuration(1);

        AreaEffectCloudEntityExtender extender = (AreaEffectCloudEntityExtender) areaEffectCloud;
        extender.bettertrims$setTrimmedOwner((LivingEntity) (Object) this);
        extender.bettertrims$updateColor();

        world.spawnEntity(areaEffectCloud);
    }
}
