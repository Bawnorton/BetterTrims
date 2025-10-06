//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.UseCooldown;

import java.util.Optional;

public final class UseCooldownExactAdder implements ExactAdder<UseCooldown> {
	@Override
	public void addToBuilder(ClientLevel level, UseCooldown cooldown, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		float seconds = cooldown.seconds();
		Optional<ResourceLocation> resourceLocation = cooldown.cooldownGroup();
		CompositeContainerComponent.Builder cooldownBuilder = CompositeContainerComponent.builder()
				.space()
				.translate(
						key("use_cooldown"),
						Styler::condition,
						Styler.number(seconds)
				);
		resourceLocation.ifPresent(location -> cooldownBuilder.space().translate(
				key("use_cooldown.group"),
				Styler::condition,
				Styler.name(Component.literal(location.toString()))
		));
		builder.component(cooldownBuilder.build());
	}
}
//?}