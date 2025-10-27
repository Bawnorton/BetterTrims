package com.bawnorton.bettertrims.client.tooltip.vanilla.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface Formatter {
	static MutableComponent decimal(Double value) {
		if (value == null) return Component.literal("null");

		int intValue = (int) (value * 100);
		if (intValue % 100 == 0) {
			return Component.literal(String.valueOf(intValue / 100));
		} else if (intValue % 10 == 0) {
			return Component.literal(String.format("%.1f", value));
		} else {
			return Component.literal(String.format("%.2f", value));
		}
	}

	static MutableComponent decimal(Float value) {
		return decimal(Double.valueOf(value));
	}

	static MutableComponent percentage(Double value) {
		if (value == null) return Component.literal("null");

		return decimal(value * 100).append(Component.literal("%"));
	}

	static MutableComponent percentage(Float value) {
		return percentage(Double.valueOf(value));
	}
}
