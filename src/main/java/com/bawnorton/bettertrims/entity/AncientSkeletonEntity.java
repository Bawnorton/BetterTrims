package com.bawnorton.bettertrims.entity;

import com.bawnorton.bettertrims.effect.attribute.AttributeSettings;
import com.bawnorton.bettertrims.util.Memoized;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

//? if >=1.21 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;

public final class AncientSkeletonEntity extends TameableEntity {
    public static final Memoized<List<Item>> WEAPONS = Memoized.of(() -> Registries.ITEM.stream()
            .filter(item -> {
                ItemStack stack = item.getDefaultStack();
                AttributeModifiersComponent attributeModifiers = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
                if (attributeModifiers == null) {
                    return false;
                }

                AttributeModifiersComponent.Entry attackDamage = attributeModifiers.modifiers()
                        .stream()
                        .filter(entry -> entry.attribute() == EntityAttributes.GENERIC_ATTACK_DAMAGE)
                        .findFirst()
                        .orElse(null);
                if (attackDamage == null) {
                    return false;
                }

                double amount = attackDamage.modifier().value();
                return amount > AttributeSettings.WarriorsOfOld.minWeaponDamage && amount <= AttributeSettings.WarriorsOfOld.maxWeaponDamage;
            }).toList());

    public static final Memoized<List<ArmorMaterial>> ARMOUR_MATERIALS = Memoized.of(() -> Registries.ARMOR_MATERIAL.streamEntries()
            .filter(armorMaterial -> armorMaterial.value()
                                             .defense()
                                             .values()
                                             .stream()
                                             .mapToInt(Integer::intValue)
                                             .sum() < AttributeSettings.WarriorsOfOld.armourStrength)
            .map(RegistryEntry.Reference::value)
            .toList());
    public static final Function<ArmorMaterial, List<ItemStack>> ARMOUR_ITEMS = Util.memoize(armourMaterial -> Registries.ITEM.stream()
            .filter(item -> item instanceof ArmorItem)
            .map(ArmorItem.class::cast)
            .filter(armorItem -> armorItem.getMaterial().value().equals(armourMaterial))
            .map(Item::getDefaultStack)
            .toList());

    private int ticksAlive = 0;

    public AncientSkeletonEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAncientSkeletonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.6, false));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 2, 10.0F, 2.0F));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, 10, true, false, null));
        this.targetSelector.add(4, new RevengeGoal(this).setGroupRevenge());
    }

    @Override
    public void tick() {
        ticksAlive++;
        if(AttributeSettings.WarriorsOfOld.decayRate != 0) {
            if (ticksAlive % getWorld().getTickManager().getTickRate() * (100 - AttributeSettings.WarriorsOfOld.decayRate) == 0) {
                setHealth(getHealth() - 1);
            }
        }
        if(isDead()) {
            deathTime = 20;
        }
        super.tick();
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);

        ItemStack weapon = WEAPONS.get().get(random.nextInt(WEAPONS.get().size())).getDefaultStack();
        EnchantmentHelper.enchant(random, weapon, AttributeSettings.WarriorsOfOld.weaponEnchantLevel, getWorld().getRegistryManager()
                .get(RegistryKeys.ENCHANTMENT)
                .streamEntries()
                .map(Function.identity()));
        equipStack(EquipmentSlot.MAINHAND, weapon);

        ArmorMaterial armourMaterial = ARMOUR_MATERIALS.get().get(random.nextInt(ARMOUR_MATERIALS.get().size()));
        List<ItemStack> armourItems = new ArrayList<>(ARMOUR_ITEMS.apply(armourMaterial));
        armourItems.removeIf(stack -> armourItems.size() > 1 && random.nextBoolean());
        armourItems.forEach(stack -> EnchantmentHelper.enchant(random, stack, AttributeSettings.WarriorsOfOld.armourEnchantLevel, getWorld().getRegistryManager()
                .get(RegistryKeys.ENCHANTMENT)
                .streamEntries()
                .map(Function.identity())));
        armourItems.forEach(stack -> equipStack(((Equipment) stack.getItem()).getSlotType(), stack));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        initEquipment(world.getRandom(), difficulty);
        return entityData;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        ticksAlive = nbt.getInt("ticksAlive");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ticksAlive", ticksAlive);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
//?} else {
/*import com.bawnorton.bettertrims.mixin.accessor.ItemAccessor;

public final class AncientSkeletonEntity extends TameableEntity {
    @SuppressWarnings("ConstantValue")
    public static final Memoized<List<Item>> WEAPONS = Memoized.of(() -> Registries.ITEM.stream()
            .filter(item -> {
                ItemStack stack = item.getDefaultStack();
                Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                if (attributeModifiers == null) return false;

                Collection<EntityAttributeModifier> attackDamageModifiers = attributeModifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                EntityAttributeModifier attackDamage = attackDamageModifiers.stream()
                        .filter(modifier -> modifier.getId().equals(ItemAccessor.getAttackDamageModifierId()))
                        .findFirst()
                        .orElse(null);
                if(attackDamage == null) return false;

                double amount = attackDamage.getValue();
                return amount > AttributeSettings.WarriorsOfOld.minWeaponDamage && amount <= AttributeSettings.WarriorsOfOld.maxWeaponDamage;
            }).toList());

    public static final Memoized<Map<ArmorMaterial, List<ItemStack>>> ARMOUR_ITEMS = Memoized.of(() -> {
        List<ArmorItem> armourItems = Registries.ITEM.stream()
                .filter(item -> item instanceof ArmorItem)
                .map(ArmorItem.class::cast)
                .toList();
        Map<ArmorMaterial, List<ItemStack>> armourMaterialLookup = new HashMap<>();
        armourItems.forEach(item -> {
            ArmorMaterial material = item.getMaterial();
            boolean suitable = Arrays.stream(ArmorItem.Type.values())
                            .mapToInt(material::getProtection)
                            .sum() < AttributeSettings.WarriorsOfOld.armourStrength;
            if (!suitable) return;

            armourMaterialLookup.computeIfAbsent(material, k -> new ArrayList<>()).add(item.getDefaultStack());
        });
        return armourMaterialLookup;
    });

    private int ticksAlive = 0;

    public AncientSkeletonEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAncientSkeletonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.6, false));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 2, 10.0F, 2.0F, false));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, 10, true, false, null));
        this.targetSelector.add(4, new RevengeGoal(this).setGroupRevenge());
    }

    @Override
    public void tick() {
        ticksAlive++;
        if(AttributeSettings.WarriorsOfOld.decayRate != 0) {
            if (ticksAlive % 20 * (100 - AttributeSettings.WarriorsOfOld.decayRate) == 0) {
                setHealth(getHealth() - 1);
            }
        }
        if(isDead()) {
            deathTime = 20;
        }
        super.tick();
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);

        ItemStack weapon = WEAPONS.get().get(random.nextInt(WEAPONS.get().size())).getDefaultStack();
        EnchantmentHelper.enchant(random, weapon, AttributeSettings.WarriorsOfOld.weaponEnchantLevel, false);
        equipStack(EquipmentSlot.MAINHAND, weapon);
        List<List<ItemStack>> armourSets = new ArrayList<>(ARMOUR_ITEMS.get().values());
        List<ItemStack> armourItems = armourSets.get(random.nextInt(armourSets.size()));
        armourItems.removeIf(stack -> armourItems.size() > 1 && random.nextBoolean());
        armourItems.forEach(stack -> EnchantmentHelper.enchant(random, stack, AttributeSettings.WarriorsOfOld.armourEnchantLevel, false));
        armourItems.forEach(stack -> equipStack(((Equipment) stack.getItem()).getSlotType(), stack));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        initEquipment(world.getRandom(), difficulty);
        return entityData;

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        ticksAlive = nbt.getInt("ticksAlive");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ticksAlive", ticksAlive);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }
}
*///?}
