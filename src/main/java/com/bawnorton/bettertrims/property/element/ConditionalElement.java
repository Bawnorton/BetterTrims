package com.bawnorton.bettertrims.property.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Optional;

public record ConditionalElement<T>(T element, Optional<LootItemCondition> requirements) {
    public static Codec<LootItemCondition> conditionCodec(ContextKeySet contextKeySet) {
        return LootItemCondition.DIRECT_CODEC
            .validate(
                lootItemCondition -> {
                    ProblemReporter.Collector collector = new ProblemReporter.Collector();
                    ValidationContext validationContext = new ValidationContext(collector, contextKeySet);
                    lootItemCondition.validate(validationContext);
                    //? if 1.21.8 {
                    return collector.isEmpty() ?
                        DataResult.success(lootItemCondition) :
                        DataResult.error(() -> "Validation error in trim element condition: " + collector.getReport());
                    //?} elif 1.21.1 {
                    /*return collector.getReport()
                        .map(string -> DataResult.<LootItemCondition>error(() -> "Validation error in trim element condition: " + string))
                        .orElseGet(() -> DataResult.success(lootItemCondition));
                    *///?}
                }
            );
    }

    public static <T> Codec<ConditionalElement<T>> ability(Codec<T> codec, ContextKeySet contextKeySet) {
        return RecordCodecBuilder.create(instance -> instance.group(
            codec.fieldOf("ability").forGetter(ConditionalElement::element),
            conditionCodec(contextKeySet).optionalFieldOf("requirements").forGetter(ConditionalElement::requirements)
        ).apply(instance, ConditionalElement::new));
    }

    public static <T> Codec<ConditionalElement<T>> itemProperty(Codec<T> codec, ContextKeySet contextKeySet) {
        return RecordCodecBuilder.create(instance -> instance.group(
            codec.fieldOf("item_property").forGetter(ConditionalElement::element),
            conditionCodec(contextKeySet).optionalFieldOf("requirements").forGetter(ConditionalElement::requirements)
        ).apply(instance, ConditionalElement::new));
    }

    public boolean matches(LootContext context) {
        return this.requirements.isEmpty() || this.requirements.get().test(context);
    }
}
