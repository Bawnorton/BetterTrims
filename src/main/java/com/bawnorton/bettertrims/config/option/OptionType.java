package com.bawnorton.bettertrims.config.option;

public enum OptionType {
    GAME,
    VANILLA,
    ADDED_VANILLA,
    MODDED;

    public boolean isGame() {
        return this == OptionType.GAME;
    }

    public boolean isVanilla() {
        return this == OptionType.VANILLA;
    }

    public boolean isAddedVanilla() {
        return this == OptionType.ADDED_VANILLA;
    }

    public boolean isModded() {
        return this == OptionType.MODDED;
    }
}
