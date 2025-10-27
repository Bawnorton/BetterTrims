package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.partial;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.PredicateTooltip;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.JukeboxPlayablePredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.JukeboxSong;

import java.util.Optional;

public final class JukeboxPlayablePartialAdder implements PartialAdder</*$ jukebox_playable_predicate >>*/ JukeboxPlayablePredicate > {
	@Override
	public void addToBuilder(ClientLevel level, /*$ jukebox_playable_predicate >>*/ JukeboxPlayablePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<HolderSet<JukeboxSong>> song = predicate.song();
		builder.space();
		if (song.isPresent()) {
			builder.translate(key("jukebox_playable"), Styler::condition);
			PredicateTooltip.addRegisteredElementsToBuilder(
					level,
					key("jukebox_playable.song"),
					Registries.JUKEBOX_SONG,
					song.orElseThrow(),
					state,
					builder
			);
		} else {
			builder.translate(key("jukebox_playable.any"), Styler::condition);
		}
	}
}
