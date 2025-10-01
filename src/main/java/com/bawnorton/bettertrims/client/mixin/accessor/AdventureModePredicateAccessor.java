package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.world.item.AdventureModePredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.List;

@MixinEnvironment("client")
@Mixin(AdventureModePredicate.class)
public interface AdventureModePredicateAccessor {
    @Accessor("predicates")
    List<BlockPredicate> bettertrims$predicates();
}
