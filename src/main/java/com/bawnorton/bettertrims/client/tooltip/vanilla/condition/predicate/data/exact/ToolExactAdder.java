package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public final class ToolExactAdder implements ExactAdder<Tool> {
	@Override
	public void addToBuilder(ClientLevel level, Tool tool, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		//? if >=1.21.8 {
		boolean canDestroyBlocksInCreative = tool.canDestroyBlocksInCreative();
		//?}
		int damagePerBlock = tool.damagePerBlock();
		float defaultMiningSpeed = tool.defaultMiningSpeed();
		List<Tool.Rule> rules = tool.rules();
		CompositeContainerComponent.Builder toolBuilder = CompositeContainerComponent.builder()
				.space()
				.translate(key("tool"), Styler::condition)
				.space()
				.translate(
						key("tool.damage_per_block"),
						Styler::condition,
						Styler.number(damagePerBlock)
				)
				.space()
				.translate(
						key("tool.default_mining_speed"),
						Styler::condition,
						Styler.number(defaultMiningSpeed)
				);
		//? if >=1.21.8 {
		if (canDestroyBlocksInCreative) {
			toolBuilder.space().translate(key("tool.can_destroy_blocks_in_creative"), Styler::condition);
		}
		//?}
		if (!rules.isEmpty()) {
			toolBuilder.space()
					.translate(key("tool.rules"), Styler::condition)
					.space()
					.cycle(cycleBuilder -> {
						for (Tool.Rule rule : rules) {
							HolderSet<Block> blocks = rule.blocks();
							Optional<TagKey<Block>> blockTagKey = blocks.unwrapKey();
							Optional<Float> speed = rule.speed();
							Optional<Boolean> correctForDrops = rule.correctForDrops();
							CompositeContainerComponent.Builder ruleBuilder = CompositeContainerComponent.builder()
									.translate(key("tool.rule"), Styler::condition)
									.spaced();
							if (blockTagKey.isPresent()) {
								ResourceLocation tag = blockTagKey.orElseThrow().location();
								ruleBuilder.translate(
										key("tool.rule.tag"),
										Styler::condition,
										Styler.name(Component.literal(tag.toString()))
								);
							}
							if (speed.isPresent()) {
								ruleBuilder.space().translate(
										key("tool.rule.speed"),
										Styler::condition,
										Styler.number(speed.orElseThrow())
								);
							}
							if (correctForDrops.isPresent() && correctForDrops.orElse(false)) {
								ruleBuilder.space().translate(key("tool.rule.correct_for_drops"), Styler::condition);
							}
							if (!ruleBuilder.build().isEmpty()) {
								cycleBuilder.component(ruleBuilder.build());
							}
						}
					});
		}
	}
}
