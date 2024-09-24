package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.client.compat.Compat;
import com.bawnorton.bettertrims.client.compat.mythicmetals.MythicMetalsCompat;
import com.bawnorton.bettertrims.client.compat.yacl.CyclingItemImageRenderer;
import com.bawnorton.bettertrims.client.compat.yacl.ItemImageRenderer;
import com.bawnorton.bettertrims.data.tag.AdditionalItemTags;
import com.bawnorton.bettertrims.effect.attribute.TrimAttribute;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Configurable(value = "aquarium", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class AquariumTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "submerged_mining_speed", min = 0, max = 16)
    public static double submergedMiningSpeed = 4;
    @Configurable(value = "swim_speed", min = 0, max = 10)
    public static double swimSpeed = 0.4;

    public AquariumTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyTotal(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED, submergedMiningSpeed).forSlot(EquipmentSlot.HEAD));
        adder.accept(TrimAttribute.multiplyBase(TrimEntityAttributes.SWIM_SPEED, swimSpeed));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return CompletableFuture.completedFuture(
                Registries.ITEM.getEntryList(AdditionalItemTags.AQUARIUM_INGOTS)
                        .map(named -> named.stream()
                                .map(itemEntry -> itemEntry.value().getDefaultStack())
                                .toList())
                        .map(CyclingItemImageRenderer::new)
        );
    }
}
