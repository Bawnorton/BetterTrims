package com.bawnorton.bettertrims.property.element;

public interface TrimElement {
	default boolean usesCount() {
		return false;
	}
}
