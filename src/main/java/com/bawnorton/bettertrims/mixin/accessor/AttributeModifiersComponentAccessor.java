package com.bawnorton.bettertrims.mixin.accessor;

import net.minecraft.component.type.AttributeModifiersComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.List;

@Mixin(AttributeModifiersComponent.class)
public interface AttributeModifiersComponentAccessor {
    @Mutable
    @Accessor
    void setModifiers(List<AttributeModifiersComponent.Entry> modifiers);
}
