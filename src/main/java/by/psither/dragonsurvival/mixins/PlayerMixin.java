package by.psither.dragonsurvival.mixins;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public class PlayerMixin {
    @ModifyArg(method="eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;"), index=2)
    private FoodProperties additionalDragons$eat(FoodProperties pFoodProperties) {
        DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler((Player) (Object) this);
        if (handler.getType() instanceof AstralDragonType astralDragonType) {
            if (!((Player) (Object) this).level().isClientSide()) {
                astralDragonType.eatFood(pFoodProperties);
            }
            return new FoodProperties.Builder().alwaysEdible().build();
        }
        return pFoodProperties;
    }
}