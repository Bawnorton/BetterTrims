package com.bawnorton.bettertrims.registry.content;

import com.bawnorton.bettertrims.mixin.accessor.DefaultAttributeContainerAccessor;
import com.bawnorton.bettertrims.mixin.accessor.DefaultAttributeRegistryAccessor;
import com.bawnorton.bettertrims.util.Aliasable;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;

public final class TrimEntityAttributes {
    //? if >=1.21 {
    /*public static RegistryEntry<EntityAttribute> ATTACK_DEFLECT_CHANCE;
    public static RegistryEntry<EntityAttribute> BLAST_MINING;
    public static RegistryEntry<EntityAttribute> BLAST_RESISTANCE;
    public static RegistryEntry<EntityAttribute> BONUS_XP;
    public static RegistryEntry<EntityAttribute> BOUNCY;
    public static RegistryEntry<EntityAttribute> BREWERS_DREAM;
    public static Aliasable<RegistryEntry<EntityAttribute>> CARMOT_SHIELD;
    public static RegistryEntry<EntityAttribute> CLEAVING;
    public static RegistryEntry<EntityAttribute> DENSE;
    public static RegistryEntry<EntityAttribute> DODGE_CHANCE;
    public static RegistryEntry<EntityAttribute> ECHOING;
    public static RegistryEntry<EntityAttribute> ELECTRIFYING;
    public static Aliasable<RegistryEntry<EntityAttribute>> ELYTRA_ROCKET_SPEED;
    public static RegistryEntry<EntityAttribute> ENCHANTERS_FAVOUR;
    public static RegistryEntry<EntityAttribute> ENDS_BLESSING;
    public static RegistryEntry<EntityAttribute> FIRE_ASPECT;
    public static RegistryEntry<EntityAttribute> FIRE_RESISTANCE;
    public static RegistryEntry<EntityAttribute> FIREY_THORNS;
    public static RegistryEntry<EntityAttribute> FORTUNE;
    public static RegistryEntry<EntityAttribute> GLOWING;
    public static RegistryEntry<EntityAttribute> HELLS_BLESSING;
    public static RegistryEntry<EntityAttribute> HOLY;
    public static RegistryEntry<EntityAttribute> HYDROPHOBIC;
    public static RegistryEntry<EntityAttribute> ITEM_MAGNET;
    public static Aliasable<RegistryEntry<EntityAttribute>> LAVA_MOVEMENT_SPEED;
    public static Aliasable<RegistryEntry<EntityAttribute>> LAVA_VISIBILITY;
    public static RegistryEntry<EntityAttribute> LIGHT_FOOTED;
    public static RegistryEntry<EntityAttribute> LOOTING;
    public static Aliasable<RegistryEntry<EntityAttribute>> MAGIC_PROTECTION;
    public static RegistryEntry<EntityAttribute> MIDAS_TOUCH;
    public static RegistryEntry<EntityAttribute> MINERS_RUSH;
    public static RegistryEntry<EntityAttribute> MOONS_BLESSING;
    public static RegistryEntry<EntityAttribute> OVERGROWN;
    public static RegistryEntry<EntityAttribute> PROJECTILE_DAMAGE;
    public static RegistryEntry<EntityAttribute> PROJECTILE_DEFLECT_CHANCE;
    public static RegistryEntry<EntityAttribute> PROJECTILE_DODGE_CHANCE;
    public static RegistryEntry<EntityAttribute> PROJECTILE_SPEED;
    public static RegistryEntry<EntityAttribute> REGENERATION;
    public static RegistryEntry<EntityAttribute> RESISTANCE;
    public static RegistryEntry<EntityAttribute> SHARE_EFFECT_RADIUS;
    public static RegistryEntry<EntityAttribute> SUNS_BLESSING;
    public static RegistryEntry<EntityAttribute> SWIM_SPEED;
    public static RegistryEntry<EntityAttribute> THORNS;
    public static RegistryEntry<EntityAttribute> TRADE_DISCOUNT;
    public static RegistryEntry<EntityAttribute> UNBREAKING;
    public static RegistryEntry<EntityAttribute> WALKING_FURNACE;
    public static RegistryEntry<EntityAttribute> WARRIORS_OF_OLD;

    @SafeVarargs
    public static void lateAddAttributes(EntityType<?> entityType, Aliasable<RegistryEntry<EntityAttribute>>... aliasables) {
        if(!DefaultAttributeRegistry.hasDefinitionFor(entityType)) return;

        DefaultAttributeContainerAccessor accessor = (DefaultAttributeContainerAccessor) DefaultAttributeRegistryAccessor.getRegistry().get(entityType);
        var builder = ImmutableMap.<RegistryEntry<EntityAttribute>, EntityAttributeInstance>builder()
                        .putAll(accessor.getInstances());
        for(Aliasable<RegistryEntry<EntityAttribute>> aliasable : aliasables) {
            if(aliasable.isUsingAlias()) return;
            if(accessor.getInstances().containsKey(aliasable.get())) return;

            RegistryEntry<EntityAttribute> registryEntry = aliasable.get();
            builder.put(registryEntry, new EntityAttributeInstance(registryEntry, instance -> {}));
        }
        accessor.setInstances(builder.build());
    }
    *///?} else {
    public static EntityAttribute ATTACK_DEFLECT_CHANCE;
    public static EntityAttribute BLAST_MINING;
    public static EntityAttribute BLAST_RESISTANCE;
    public static EntityAttribute BONUS_XP;
    public static EntityAttribute BOUNCY;
    public static EntityAttribute BREWERS_DREAM;
    public static Aliasable<EntityAttribute> CARMOT_SHIELD;
    public static EntityAttribute CLEAVING;
    public static EntityAttribute DENSE;
    public static EntityAttribute DODGE_CHANCE;
    public static EntityAttribute ECHOING;
    public static EntityAttribute ELECTRIFYING;
    public static Aliasable<EntityAttribute> ELYTRA_ROCKET_SPEED;
    public static EntityAttribute ENCHANTERS_FAVOUR;
    public static EntityAttribute ENDS_BLESSING;
    public static EntityAttribute FIREY_THORNS;
    public static EntityAttribute FIRE_ASPECT;
    public static EntityAttribute FIRE_RESISTANCE;
    public static EntityAttribute FORTUNE;
    public static EntityAttribute GLOWING;
    public static EntityAttribute HELLS_BLESSING;
    public static EntityAttribute HOLY;
    public static EntityAttribute HYDROPHOBIC;
    public static EntityAttribute ITEM_MAGNET;
    public static Aliasable<EntityAttribute> LAVA_MOVEMENT_SPEED;
    public static Aliasable<EntityAttribute> LAVA_VISIBILITY;
    public static EntityAttribute LIGHT_FOOTED;
    public static EntityAttribute LOOTING;
    public static Aliasable<EntityAttribute> MAGIC_PROTECTION;
    public static EntityAttribute MIDAS_TOUCH;
    public static EntityAttribute MINERS_RUSH;
    public static EntityAttribute MOONS_BLESSING;
    public static EntityAttribute OVERGROWN;
    public static EntityAttribute PROJECTILE_DAMAGE;
    public static EntityAttribute PROJECTILE_DEFLECT_CHANCE;
    public static EntityAttribute PROJECTILE_DODGE_CHANCE;
    public static EntityAttribute PROJECTILE_SPEED;
    public static EntityAttribute REGENERATION;
    public static EntityAttribute RESISTANCE;
    public static EntityAttribute SHARE_EFFECT_RADIUS;
    public static EntityAttribute SUNS_BLESSING;
    public static EntityAttribute SWIM_SPEED;
    public static EntityAttribute THORNS;
    public static EntityAttribute TRADE_DISCOUNT;
    public static EntityAttribute UNBREAKING;
    public static EntityAttribute WALKING_FURNACE;
    public static EntityAttribute WARRIORS_OF_OLD;

    public static EntityAttribute PLAYER_BLOCK_BREAK_SPEED;
    public static EntityAttribute PLAYER_SUBMERGED_MINING_SPEED;
    public static EntityAttribute GENERIC_STEP_HEIGHT;
    public static EntityAttribute GENERIC_OXYGEN_BONUS;

    @SafeVarargs
    public static void lateAddAttributes(EntityType<?> entityType, Aliasable<EntityAttribute>... aliasables) {
        if(!DefaultAttributeRegistry.hasDefinitionFor(entityType)) return;

        DefaultAttributeContainerAccessor accessor = (DefaultAttributeContainerAccessor) DefaultAttributeRegistryAccessor.getRegistry().get(entityType);
        var builder = ImmutableMap.<EntityAttribute, EntityAttributeInstance>builder()
                .putAll(accessor.getInstances());
        for(Aliasable<EntityAttribute> aliasable : aliasables) {
            if(aliasable.isUsingAlias()) return;
            if(accessor.getInstances().containsKey(aliasable.get())) return;

            EntityAttribute registryEntry = aliasable.get();
            builder.put(registryEntry, new EntityAttributeInstance(registryEntry, instance -> {}));
        }
        accessor.setInstances(builder.build());
    }
    //?}
}
