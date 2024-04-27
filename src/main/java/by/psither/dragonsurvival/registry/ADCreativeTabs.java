package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.registry.DSCreativeTabs;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(modid = AdditionalDragonsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ADCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdditionalDragonsMod.MODID);

	@SubscribeEvent
	public static void addItems(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == DSCreativeTabs.DS_TAB.getKey()) {
			event.accept(() -> ADItems.ancientCatalyst);
			event.accept(() -> ADBlocks.glowSlime);
			event.accept(() -> ADItems.slipperySushi);
			event.accept(() -> ADItems.cursedMarrow);
		}
	}
}
