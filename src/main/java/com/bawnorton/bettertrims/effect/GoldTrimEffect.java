package com.bawnorton.bettertrims.effect;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.registry.content.TrimEntityAttributes;
import com.bawnorton.configurable.Configurable;
import com.bawnorton.configurable.Image;
import com.bawnorton.configurable.Yacl;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

@Configurable(value = "gold", yacl = @Yacl(image = @Image("minecraft:textures/item/gold_ingot.png"), collapsed = true))
public final class GoldTrimEffect extends CelestialEffect {
    @Configurable
    public static boolean enabled = true;

    public GoldTrimEffect(TagKey<Item> materials) {
        super(materials);
    }

    @Override
    public RegistryEntry<EntityAttribute> getEntityAttribute() {
        return TrimEntityAttributes.SUNS_BLESSING;
    }

    @Override
    public float getMovementSpeed() {
        return AttributeSettings.SunsBlessing.movementSpeed;
    }

    @Override
    public float getDamageResistance() {
        return AttributeSettings.SunsBlessing.resistance;
    }

    @Override
    public float getAttackDamage() {
        return AttributeSettings.SunsBlessing.attackDamage;
    }

    @Override
    public float getAttackSpeed() {
        return AttributeSettings.SunsBlessing.attackSpeed;
    }

    @Override
    protected boolean getEnabled() {
        return enabled;
    }
}
