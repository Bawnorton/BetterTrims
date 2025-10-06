//? if >=1.21.8 {
package com.bawnorton.bettertrims.client.tooltip.condition.predicate.data.exact;

import com.bawnorton.bettertrims.client.tooltip.component.CompositeContainerComponent;
import com.bawnorton.bettertrims.client.tooltip.condition.LootConditionTooltips;
import com.bawnorton.bettertrims.client.tooltip.util.Styler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.component.Weapon;

public final class WeaponExactAdder implements ExactAdder<Weapon> {
	@Override
	public void addToBuilder(ClientLevel level, Weapon weapon, LootConditionTooltips.State state, CompositeContainerComponent.Builder builder) {
		int itemDamagePerAttack = weapon.itemDamagePerAttack();
		float disableBlockingForSeconds = weapon.disableBlockingForSeconds();
		CompositeContainerComponent.Builder weaponBuilder = CompositeContainerComponent.builder()
				.space()
				.translate(key("weapon"), Styler::condition)
				.space()
				.translate(
						key("weapon.item_damage_per_attack"),
						Styler::condition,
						Styler.number(itemDamagePerAttack)
				)
				.space()
				.translate(
						key("weapon.disable_blocking_for_seconds"),
						Styler::condition,
						Styler.number(disableBlockingForSeconds)
				);
		builder.component(weaponBuilder.build());
	}
}
//?}