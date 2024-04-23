package com.bawnorton.bettertrims;

import com.bawnorton.bettertrims.util.mixin.AdvancedConditionChecker;
import com.bawnorton.bettertrims.util.mixin.annotation.AdvancedConditionalMixin;
import com.bawnorton.bettertrims.util.mixin.annotation.ConditionalMixin;
import com.bawnorton.bettertrims.util.mixin.annotation.VersionPredicate;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BetterTrimsMixinPlugin implements IMixinConfigPlugin {
    public static boolean testClass(String mixinClassName) {
        try {
            List<AnnotationNode> annotationNodes = MixinService.getService()
                                                               .getBytecodeProvider()
                                                               .getClassNode(mixinClassName).visibleAnnotations;
            if (annotationNodes == null) return true;

            for (AnnotationNode node : annotationNodes) {
                if (node.desc.equals(Type.getDescriptor(ConditionalMixin.class))) {
                    String modid = Annotations.getValue(node, "modid");
                    boolean applyIfPresent = Annotations.getValue(node, "applyIfPresent", Boolean.TRUE);
                    if (isModLoaded(modid)) {
                        BetterTrims.LOGGER.debug("BetterTrimsMixinPlugin: " + mixinClassName + " is" + (applyIfPresent ? " " : " not ") + "being applied because " + modid + " is loaded");
                        return applyIfPresent;
                    } else {
                        BetterTrims.LOGGER.debug("BetterTrimsMixinPlugin: " + mixinClassName + " is" + (!applyIfPresent ? " " : " not ") + "being applied because " + modid + " is not loaded");
                        return !applyIfPresent;
                    }
                } else if (node.desc.equals(Type.getDescriptor(AdvancedConditionalMixin.class))) {
                    Type checkerType = Annotations.getValue(node, "checker");
                    boolean invert = Annotations.getValue(node, "invert", Boolean.FALSE);
                    AnnotationNode version = Annotations.getValue(node, "version", VersionPredicate.class);
                    AdvancedConditionChecker checker = AdvancedConditionChecker.create(checkerType, version);
                    boolean shouldApply = checker.shouldApply();
                    if (invert) shouldApply = !shouldApply;
                    BetterTrims.LOGGER.debug("BetterTrimsMixinPlugin: " + mixinClassName + " is" + (shouldApply ? " " : " not ") + "being applied because " + checkerType.getClassName() + " returned " + shouldApply);
                    return shouldApply;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            BetterTrims.LOGGER.error("BetterTrimsMixinPlugin: Failed to load class " + mixinClassName + ", it will not be applied", e);
            return false;
        }
        return true;
    }

    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return testClass(mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
