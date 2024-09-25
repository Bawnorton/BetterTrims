package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.data.tag.AdditionalItemTags;
import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.OptionType;
import com.bawnorton.configurable.Yacl;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Configurable(value = "silver", yacl = @Yacl(type = OptionType.GAME_RESTART, image = @Image(custom = "getImage"), collapsed = true))
public final class SilverTrimEffect extends CelestialEffect {
	@Configurable
	public static boolean enabled = true;

    public SilverTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    //? if >=1.21 {
    public RegistryEntry<EntityAttribute> getEntityAttribute() {
    //?} else {
    /*public EntityAttribute getEntityAttribute() {
    *///?}
        return TrimEntityAttributes.MOONS_BLESSING;
    }

    @Override
    public float getMovementSpeed() {
        return AttributeSettings.MoonsBlessing.movementSpeed;
    }

    @Override
    public float getDamageResistance() {
        return AttributeSettings.MoonsBlessing.resistance;
    }

    @Override
    public float getAttackDamage() {
        return AttributeSettings.MoonsBlessing.attackDamage;
    }

    @Override
    public float getAttackSpeed() {
        return AttributeSettings.MoonsBlessing.attackSpeed;
    }

    @Override
    protected boolean isEnabled() {
        return enabled;
    }

    public static CompletableFuture<Optional<ImageRenderer>> getImage() {
        return getImageFor(AdditionalItemTags.SILVER_INGOTS);
    }
}
