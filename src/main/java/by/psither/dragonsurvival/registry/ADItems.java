package by.psither.dragonsurvival.registry;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ADItems {
	public static DeferredRegister<Item> REGISTRY = DeferredRegister.create(
			BuiltInRegistries.ITEM,
			MODID
	);

	private static final Properties defaultProperties = new Item.Properties();
	public static final Holder<Item> SLIPPERY_SUSHI = REGISTRY.register("slippery_sushi", () -> new Item(defaultProperties));
	public static final Holder<Item> CURSED_MARROW = REGISTRY.register("cursed_marrow", () -> new Item(defaultProperties));
	public static final Holder<Item> ANCIENT_CATALYST = REGISTRY.register("ancient_catalyst", () -> new Item(defaultProperties));
}
