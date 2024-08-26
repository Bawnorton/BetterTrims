package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.applicator.ConsumingTrimEffectApplicator;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public final class CopperTrimEffect extends TrimEffect {
    private final Object2ObjectMap<LivingEntity, Set<BlockPos>> electrified = new Object2ObjectOpenHashMap<>();

    public CopperTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.leveled(TrimEntityAttributes.ELECTRIFYING));
    }

    public Object2ObjectMap<LivingEntity, Set<BlockPos>> getElectrified() {
        return electrified;
    }

    public ConsumingTrimEffectApplicator getApplicator() {
        return (context, entity) -> {

        };
    }

    public Optional<LivingEntity> whoElectrified(BlockPos pos) {
        for (Object2ObjectMap.Entry<LivingEntity, Set<BlockPos>> entry : electrified.object2ObjectEntrySet()) {
            LivingEntity entity = entry.getKey();
            Set<BlockPos> electrified = entry.getValue();
            if(electrified.contains(pos)) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public void clearElectrified(Entity entity) {
        if(entity instanceof LivingEntity) {
            electrified.remove(entity);
        }
    }
}
