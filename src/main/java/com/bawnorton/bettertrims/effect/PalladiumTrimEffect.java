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
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Configurable(value = "palladium", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class PalladiumTrimEffect extends TrimEffect {
	@Configurable
	public static boolean enabled = true;
    @Configurable(value = "fire_resistance", min = 0, max = 1, yacl = @Yacl(formatter = "com.bawnorton.bettertrims.client.BetterTrimsClient#twoDpFormatter"))
    public static float fireResistance = 0.25f;
    @Configurable(value = "lava_visibility", min = 0, max = 24)
    public static int lavaVisibility = 6;
    @Configurable(value = "lava_movement_speed", min = 0, max = 10)
    public static float lavaMovementSpeed = 0.7f;

    public PalladiumTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    protected void addAttributes(Consumer<TrimAttribute> adder) {
        adder.accept(TrimAttribute.multiplyBase(() -> TrimEntityAttributes.FIRE_RESISTANCE, fireResistance));
        adder.accept(TrimAttribute.addingAliased(() -> TrimEntityAttributes.LAVA_VISIBILITY, lavaVisibility).forSlot(EquipmentSlot.HEAD));
        adder.accept(TrimAttribute.multiplyBaseAliased(() -> TrimEntityAttributes.LAVA_MOVEMENT_SPEED, lavaMovementSpeed));
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.PALLADIUM_INGOTS);
    }
}
