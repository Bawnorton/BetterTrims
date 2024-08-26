package com.bawnorton.bettertrims.mixin.registry;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.registry.content.TrimSoundEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundEvents.class)
public abstract class SoundEventsMixin {
    @Shadow
    private static SoundEvent register(Identifier id) {
        throw new AssertionError();
    }

    static {
        TrimSoundEvents.ECHO_REWIND = register(BetterTrims.id("echo_rewind"));
    }
}
