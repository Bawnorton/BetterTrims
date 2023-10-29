package com.bawnorton.bettertrims;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public class BetterTrimsMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return mixinClassName.equals("net.fabricmc.loom.nativesupport.mixin.WindowMixin");
    }
}
