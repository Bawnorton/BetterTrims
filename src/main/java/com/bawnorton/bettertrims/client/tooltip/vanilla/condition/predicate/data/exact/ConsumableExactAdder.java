//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import com.bawnorton.bettertrims.property.ability.type.entity.PlaySoundAbility;
import com.bawnorton.bettertrims.version.VRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import org.apache.commons.lang3.StringUtils;

public final class ConsumableExactAdder implements ExactAdder<Consumable> {
	@Override
	public void addToBuilder(ClientLevel level, Consumable consumable, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Holder<SoundEvent> soundHolder = consumable.sound();
		ItemUseAnimation animation = consumable.animation();
		float consumeSeconds = consumable.consumeSeconds();
		boolean hasConsumeParticles = consumable.hasConsumeParticles();

		CompositeContainerComponent.Builder consumableBuilder = CompositeContainerComponent.builder()
				.space()
				.translate(key("consumable"), Styler::condition)
				.space()
				.translate(
						key("consumable.animation"),
						Styler::condition,
						Styler.name(Component.literal(StringUtils.capitalize(animation.getSerializedName())))
				)
				.space()
				.translate(
						key("consumable.consume_seconds"),
						Styler::condition,
						Styler.number(consumeSeconds)
				);

		Registry<SoundEvent> registry = VRegistry.get(level, Registries.SOUND_EVENT);
		Component soundName = PlaySoundAbility.getSoundName(registry, soundHolder);
		consumableBuilder.space()
				.translate(
						key("consumable.sound"),
						Styler::condition,
						Styler.name(soundName.copy())
				);

		if (hasConsumeParticles) {
			consumableBuilder.space().translate(key("consumable.has_consume_particles"), Styler::condition);
		}
	}
}
//?}