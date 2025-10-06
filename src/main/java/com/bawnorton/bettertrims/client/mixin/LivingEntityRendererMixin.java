package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.extension.LivingEntityRenderStateExtension;
import com.bawnorton.bettertrims.registry.BetterTrimsAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.8 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
 //?}

@MixinEnvironment("client")
@Mixin(LivingEntityRenderer.class)
//? if >=1.21.8 {
abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @WrapOperation(
        method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;shouldRenderLayers(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z"
        )
    )
    private boolean dontRenderLayersOnTrueInvisible(LivingEntityRenderer<T, S, M> instance, S renderState, Operation<Boolean> original) {
        if (renderState instanceof LivingEntityRenderStateExtension extension && extension.bettertrims$isTrulyInvisible()) {
            return false;
        }
        return original.call(instance, renderState);
    }

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void extractExtendedState(T livingEntity, S renderState, float f, CallbackInfo ci) {
        if(renderState instanceof LivingEntityRenderStateExtension extension) {
            AttributeInstance attribute = livingEntity.getAttribute(BetterTrimsAttributes.TRUE_INVISIBILITY);
            if (attribute == null) {
                extension.bettertrims$setTrulyInvisible(false);
            } else {
                extension.bettertrims$setTrulyInvisible(attribute.getValue() >= 1);
            }
        }
    }
}
//?} else {
/*abstract class LivingEntityRendererMixin {
	@WrapOperation(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"
			)
	)
	private boolean dontRenderLayersOnTrueInvisible(LivingEntity instance, Operation<Boolean> original) {
		if (original.call(instance)) return true;

		AttributeInstance attribute = instance.getAttribute(BetterTrimsAttributes.TRUE_INVISIBILITY);
		return attribute != null && attribute.getValue() >= 1;
	}
}
*///?}