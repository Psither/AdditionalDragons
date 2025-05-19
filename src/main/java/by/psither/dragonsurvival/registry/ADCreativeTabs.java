package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.registry.DSCreativeTabs;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = AdditionalDragonsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ADCreativeTabs {

	@SubscribeEvent
	public static void addItems(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == DSCreativeTabs.DS_TAB.getKey()) {
			event.accept(ADItems.ANCIENT_CATALYST_EMPTY::value);
			event.accept(ADBlocks.GLOW_SLIME_ITEM::value);
			event.accept(ADItems.SLIPPERY_SUSHI::value);
			event.accept(ADItems.CURSED_MARROW::value);
		}
	}
}
