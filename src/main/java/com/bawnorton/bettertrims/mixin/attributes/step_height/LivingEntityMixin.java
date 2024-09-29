package com.bawnorton.bettertrims.mixin.attributes.step_height;

//? if <1.21 {
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    //$ attribute_shadow
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @ModifyExpressionValue(
            method = "getStepHeight",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getStepHeight()F"
            )
    )
    private float applyStepHeight(float original) {
        return original + (float) getAttributeValue(TrimEntityAttributes.GENERIC_STEP_HEIGHT);
    }
}
//?}
