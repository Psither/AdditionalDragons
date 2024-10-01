package by.psither.dragonsurvival.mixins;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.DragonFoodHandler;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DragonFoodHandler.class)
public class DragonFoodHandlerMixin {
    @Inject(method= "getDragonFoodProperties(Lnet/minecraft/world/item/Item;Lby/dragonsurvivalteam/dragonsurvival/common/dragon_types/AbstractDragonType;)Lnet/minecraft/world/food/FoodProperties;", at=@At(value="HEAD"), cancellable = true)
    private static void additionalDragons$getDragonFoodProperties(Item item, AbstractDragonType type, CallbackInfoReturnable<FoodProperties> cir) {
        if (type instanceof AstralDragonType) {
            cir.setReturnValue(new ItemStack(item).get(DataComponents.FOOD));
        }
    }
}