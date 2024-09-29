package com.bawnorton.bettertrims.mixin.accessor;

//? if <1.21 {
/*import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.UUID;

@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor("ATTACK_DAMAGE_MODIFIER_ID")
    static UUID getAttackDamageModifierId() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor
    void setFoodComponent(FoodComponent foodComponent);
}
*///?}
