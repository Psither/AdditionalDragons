package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.common.codecs.DragonAbilityHolder;
import by.dragonsurvivalteam.dragonsurvival.registry.data_components.DSDataComponents;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.DragonSpecies;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbility;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

import static by.psither.dragonsurvival.AdditionalDragonsMod.*;


public class ADItems {
	public static DeferredRegister<Item> REGISTRY = DeferredRegister.create(
			BuiltInRegistries.ITEM,
			MODID
	);

	private static final Properties defaultProperties = new Item.Properties();
	public static final Holder<Item> SLIPPERY_SUSHI = REGISTRY.register("slippery_sushi", () -> new Item(defaultProperties));
	public static final Holder<Item> CURSED_MARROW = REGISTRY.register("cursed_marrow", () -> new Item(defaultProperties));
	public static final Holder<Item> ANCIENT_CATALYST_SEA = REGISTRY.register("ancient_catalyst_sea", () -> new Item(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("luminous_breath")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("group_heal")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("high_voltage")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("bubble_shield"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("sea_dragon"))))),
					false)
			)
	));
}
