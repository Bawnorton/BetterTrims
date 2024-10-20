package com.bawnorton.bettertrims.effect;

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
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Configurable(value = "aquarium", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class AquariumTrimEffect extends TrimEffect {
    @Configurable
    public static boolean enabled = true;
    @Configurable(value = "submerged_mining_speed", max = 10, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float submergedMiningSpeed = 1;
    @Configurable(value = "swim_speed", max = 10, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float swimSpeed = 0.3f;
    @Configurable(value = "oxygen_bonus", max = 15)
    public static int oxygenBonus = 3;

    public AquariumTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        //? if >=1.21 {
        adder.accept(TrimAttribute.adding(() -> EntityAttributes.GENERIC_OXYGEN_BONUS, oxygenBonus).forSlot(EquipmentSlot.HEAD));
        adder.accept(TrimAttribute.multiplyTotal(() -> EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED, submergedMiningSpeed));
        //?} else {
        /*adder.accept(TrimAttribute.adding(() -> TrimEntityAttributes.GENERIC_OXYGEN_BONUS, oxygenBonus).forSlot(EquipmentSlot.HEAD));
        adder.accept(TrimAttribute.multiplyTotal(() -> TrimEntityAttributes.PLAYER_SUBMERGED_MINING_SPEED, submergedMiningSpeed));
        *///?}
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.SWIM_SPEED, swimSpeed));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.AQUARIUM_INGOTS);
    }
}
