package com.bawnorton.bettertrims.client.tooltip.condition.predicate;

import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Optional;

public interface DamageSourcePredicateTooltip {
	static String key(String key) {
		return PredicateTooltip.key("damage_source.%s".formatted(key));
	}

	static void addToBuilder(ClientLevel level, DamageSourcePredicate predicate, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		Optional<EntityPredicate> directEntity = predicate.directEntity();
		if (directEntity.isPresent()) {
			EntityPredicateTooltip.addEntityPredicateToBuilder(level, "damage_source.direct", directEntity.orElseThrow(), state, builder);
		}

		Optional<EntityPredicate> sourceEntity = predicate.sourceEntity();
		if (sourceEntity.isPresent()) {
			EntityPredicateTooltip.addEntityPredicateToBuilder(level, "damage_source.source", sourceEntity.orElseThrow(), state, builder);
		}

		Optional<Boolean> direct = predicate.isDirect();
		if (direct.isPresent()) {
			addDirectToBuilder(level, direct.orElseThrow(), state, builder);
		}

		List<TagPredicate<DamageType>> projectile = predicate.tags();
		if (!projectile.isEmpty()) {
			addTagsToBuilder(level, projectile, state, builder);
		}
	}

	static void addDirectToBuilder(ClientLevel level, Boolean direct, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		builder.component(CompositeContainerComponent.builder()
				.space()
				.translate(key("is_direct.%s".formatted(direct ? "true" : "false")), Styler::value)
				.build());
	}

	static void addTagsToBuilder(ClientLevel level, List<TagPredicate<DamageType>> tags, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		PredicateTooltip.addTagsToBuilder(key("tags"), tags, state, builder);
	}
}
