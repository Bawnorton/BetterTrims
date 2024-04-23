package com.bawnorton.bettertrims.util.mixin;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@SuppressWarnings("unused")
public interface AdvancedConditionChecker {
    MethodHandles.Lookup lookup = MethodHandles.lookup();

    boolean shouldApply();

    static AdvancedConditionChecker create(Type checkerType, AnnotationNode version) {
        try {
            Class<?> clazz = Class.forName(checkerType.getClassName());
            MethodType methodType = MethodType.methodType(void.class, String.class, String.class);
            MethodHandle constructorHandle = lookup.findConstructor(clazz, methodType);
            String min = Annotations.getValue(version, "min", "");
            String max = Annotations.getValue(version, "max", "");
            if (!(constructorHandle.invoke(min, max) instanceof AdvancedConditionChecker checker)) {
                throw new RuntimeException("AdvancedConditionChecker class " + checkerType.getClassName() + " does not implement AdvancedConditionChecker");
            }

            return checker;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
