//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.mixin;

import com.bawnorton.bettertrims.client.extension.LivingEntityRenderStateExtension;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@MixinEnvironment("client")
@Mixin(LivingEntityRenderState.class)
abstract class LivingEntityRenderStateMixin implements LivingEntityRenderStateExtension {
    @Unique
    private boolean bettertrims$isTrulyInvisible = false;

    @Override
    public boolean bettertrims$isTrulyInvisible() {
        return bettertrims$isTrulyInvisible;
    }

    @Override
    public void bettertrims$setTrulyInvisible(boolean trulyInvisible) {
        this.bettertrims$isTrulyInvisible = trulyInvisible;
    }
}
//?}