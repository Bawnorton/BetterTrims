package com.bawnorton.bettertrims.mixin.attributes.item_magnet;

import com.bawnorton.bettertrims.registry.content.TrimEffects;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;updateLeaningPitch()V"
            )
    )
    private void applyItemMagnet(CallbackInfo ci) {
        if (!TrimEffects.IRON.isEnabledFor((LivingEntity) (Object) this)) return;

        int itemMagnetLevel = (int) getAttributeValue(TrimEntityAttributes.ITEM_MAGNET);
        if(itemMagnetLevel <= 0) return;

        World world = getWorld();
        Box area = new Box(getBlockPos());
        area = area.expand(itemMagnetLevel * 1.5);
        List<ItemEntity> nearbyItems = world.getEntitiesByClass(ItemEntity.class, area, Predicates.alwaysTrue());
        Vec3d entityPos = getPos();
        for (ItemEntity itemEntity : nearbyItems) {
            Vec3d itemPos = itemEntity.getPos();
            if (itemPos.isInRange(entityPos, 0.5)) {
                itemEntity.resetPickupDelay();
                itemEntity.setPos(entityPos.x, entityPos.y + 0.02, entityPos.z);
            } else {
                Vec3d difference = entityPos.subtract(itemPos).normalize().multiply(itemMagnetLevel / 10f);
                itemEntity.setVelocity(difference);
            }
        }
    }
}