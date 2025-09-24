package com.bawnorton.bettertrims.client.mixin.accessor;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;

@MixinEnvironment("client")
@Mixin(EnchantmentsPredicate.class)
public interface EnchantmentsPredicateAccessor {
    @Invoker("enchantments")
    List<EnchantmentPredicate> bettertrims$enchantments();
}
