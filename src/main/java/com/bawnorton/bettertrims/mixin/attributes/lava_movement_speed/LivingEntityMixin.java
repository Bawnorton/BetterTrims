package com.bawnorton.bettertrims.mixin.attributes.lava_movement_speed;

import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @WrapOperation(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/registry/tag/FluidTags;LAVA:Lnet/minecraft/registry/tag/TagKey;"
                    )
            )
    )
    private Vec3d applyLavaMovementSpeed(Vec3d instance, double x, double y, double z, Operation<Vec3d> original) {
        if(TrimEntityAttributes.LAVA_MOVEMENT_SPEED.isUsingAlias()) return original.call(instance, x, y, z);

        double speed = getAttributeValue(TrimEntityAttributes.LAVA_MOVEMENT_SPEED.get()) - 1;
        return original.call(instance, x * speed, y, z * speed);
    }

    @ModifyArg(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/registry/tag/FluidTags;LAVA:Lnet/minecraft/registry/tag/TagKey;"
                    )
            )
    )
    private double applyLavaMovementSpeed(double original) {
        if(TrimEntityAttributes.LAVA_MOVEMENT_SPEED.isUsingAlias()) return original;

        double speed = getAttributeValue(TrimEntityAttributes.LAVA_MOVEMENT_SPEED.get()) - 1;
        return original * speed;
    }
}
