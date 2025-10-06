package com.bawnorton.bettertrims.registry;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class BetterTrimsAttributes {
	public static final Holder<Attribute> TRUE_INVISIBILITY = register(
			"true_invisibility",
			new RangedAttribute("attribute.name.bettertrims.true_invisibility", 0, 0, 1).setSyncable(true)
	);

	private static Holder<Attribute> register(String name, Attribute attribute) {
		return Registry.registerForHolder(
				BuiltInRegistries.ATTRIBUTE,
				BetterTrims.rl(name),
				attribute
		);
	}

	public static void init() {
		// NO-OP
	}
}
