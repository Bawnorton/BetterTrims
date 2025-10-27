package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

//? if <1.21.8 {
/*import com.bawnorton.bettertrims.client.mixin.accessor.PotionAccessor;
*///?}

public final class PotionsPredicatePartialAdder implements PartialAdder</*$ potions_predicate >>*/ PotionsPredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ potions_predicate >>*/ PotionsPredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		HolderSet<Potion> potions = predicate.potions();
		if (potions.size() == 0) {
			builder.translate(key("potions.any"), Styler::condition);
		} else {
			PredicateTooltip.addRegisteredElementsToBuilder(
					level,
					key("potions.matches"),
					Registries.POTION,
					potions,
					(potion, appender) -> {
						List<MobEffectInstance> effects = potion.getEffects();
						if (effects.isEmpty()) {
							//? if >=1.21.8 {
							return Component.literal(StringUtils.capitalize(potion.name()));
							 //?} else {
							/*return Component.literal(StringUtils.capitalize(((PotionAccessor) potions).bettertrims$name()));
							*///?}
						} else {
							MobEffectInstance effect = effects.getFirst();
							ChatFormatting formatting = effect.getEffect().value().getCategory().getTooltipFormatting();
							MutableComponent description = Component.translatable(effect.getEffect().value().getDescriptionId()).withStyle(formatting);
							appender.textComponent(effect.getAmplifier() > 0 ? Component.translatable("potion.withAmplifier", description, Component.translatable("potion.potency." + level)) : description);
							for (int i = 1; i < effects.size(); i++) {
								effect = effects.get(i);
								description = Component.translatable(effect.getEffect().value().getDescriptionId());
								appender.literal(", ", Styler::condition)
										.textComponent(effect.getAmplifier() > 0 ? Component.translatable("potion.withAmplifier", description, Component.translatable("potion.potency." + level)) : description);
							}
							return Component.literal("");
						}
					},
					state,
					builder
			);
		}
	}
}
