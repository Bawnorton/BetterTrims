//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.vanilla.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.vanilla.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.component.ItemComponent;
import com.bawnorton.bettertrims.client.tooltip.vanilla.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.vanilla.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseRemainder;

public final class UseRemainderExactAdder implements ExactAdder<UseRemainder> {
	@Override
	public void addToBuilder(ClientLevel level, UseRemainder remainder, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		ItemStack itemStack = remainder.convertInto();
		CompositeContainerComponent.Builder remainderBuilder = CompositeContainerComponent.builder()
				.centred(true)
				.space()
				.translate(key("use_remainder"), Styler::condition)
				.space()
				.component(new ItemComponent(itemStack));
		builder.component(remainderBuilder.build());
	}
}
//?}