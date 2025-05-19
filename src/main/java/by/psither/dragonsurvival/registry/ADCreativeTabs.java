package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ADCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AdditionalDragonsMod.MODID);

	@SubscribeEvent
	public static void addItems(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey().isFor(Registries.CREATIVE_MODE_TAB)) {
			event.accept(ADItems.ANCIENT_CATALYST_DEEPWOODS::value);
			event.accept(ADItems.ANCIENT_CATALYST_PRIMORDIAL::value);
			event.accept(ADItems.ANCIENT_CATALYST_TECTONIC::value);
			event.accept(ADBlocks.GLOW_SLIME_ITEM::value);
			event.accept(ADItems.SLIPPERY_SUSHI::value);
			event.accept(ADItems.CURSED_MARROW::value);
		}
	}
}
