//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.version.VRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.component.DamageResistant;

public final class DamageResistantExactAdder implements ExactAdder<DamageResistant> {
	@Override
	public void addToBuilder(ClientLevel level, DamageResistant resistant, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		TagKey<DamageType> types = resistant.types();
		CompositeContainerComponent.Builder resistantBuilder = CompositeContainerComponent.builder()
				.space()
				.translate(key("damage_resistant"), Styler::condition)
				.space()
				.cycle(cycleBuilder -> {
					Registry<DamageType> registry = VRegistry.get(level, Registries.DAMAGE_TYPE);
					registry.getOrThrow(types).forEach(type -> {
						ResourceLocation damageType = type.unwrap().map(ResourceKey::location, registry::getKey);
						cycleBuilder.literal(damageType.toString(), Styler::name);
					});
				});
		builder.component(resistantBuilder.build());
	}
}
//?}