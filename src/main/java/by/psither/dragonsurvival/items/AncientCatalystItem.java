package by.psither.dragonsurvival.items;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.tags.DSDragonSpeciesTags;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.DragonSpecies;
import by.psither.dragonsurvival.registry.ADItems;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AncientCatalystItem extends Item {
    public AncientCatalystItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player && player.getInventory().getItem(slotId) == stack) {
            Holder<DragonSpecies> species = DragonStateProvider.getData(player).species();
            if (species != null) {
                if (species.is(DSDragonSpeciesTags.FOREST_DRAGONS)) {
                    if (!stack.is(ADItems.ANCIENT_CATALYST_DEEPWOODS)) {
                        player.getInventory().setItem(slotId, new ItemStack(ADItems.ANCIENT_CATALYST_DEEPWOODS));
                    }
                } else if (species.is(DSDragonSpeciesTags.SEA_DRAGONS)) {
                    if (!stack.is(ADItems.ANCIENT_CATALYST_PRIMORDIAL)) {
                        player.getInventory().setItem(slotId, new ItemStack(ADItems.ANCIENT_CATALYST_PRIMORDIAL));
                    }
                } else if (species.is(DSDragonSpeciesTags.CAVE_DRAGONS)) {
                    if (!stack.is(ADItems.ANCIENT_CATALYST_TECTONIC)) {
                        player.getInventory().setItem(slotId, new ItemStack(ADItems.ANCIENT_CATALYST_TECTONIC));
                    }
                } else if (species.is(DragonSurvival.res("astral_dragon"))) {
                    if (!stack.is(ADItems.ANCIENT_CATALYST_ASTRAL)) {
                        player.getInventory().setItem(slotId, new ItemStack(ADItems.ANCIENT_CATALYST_ASTRAL));
                    }
                } else if (!stack.is(ADItems.ANCIENT_CATALYST_EMPTY)) {
                    player.getInventory().setItem(slotId, new ItemStack(ADItems.ANCIENT_CATALYST_EMPTY));
                }
            } else if (!stack.is(ADItems.ANCIENT_CATALYST_HUMAN)) {
                player.getInventory().setItem(slotId, new ItemStack(ADItems.ANCIENT_CATALYST_HUMAN));
            }
        }

        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
