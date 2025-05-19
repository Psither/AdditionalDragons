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
	public static final Holder<Item> ANCIENT_CATALYST_DEEPWOODS = REGISTRY.register("ancient_catalyst_deepwoods", () -> new Item(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("misty_barb")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("confounding_breath")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("invigorate")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("seeking_talons"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("forest_dragon"))))),
					false)
			)
	));
	public static final Holder<Item> ANCIENT_CATALYST_PRIMORDIAL = REGISTRY.register("ancient_catalyst_primordial", () -> new Item(
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
	public static final Holder<Item> ANCIENT_CATALYST_TECTONIC = REGISTRY.register("ancient_catalyst_tectonic", () -> new Item(
			new Item.Properties().component(
					DSDataComponents.DRAGON_ABILITIES,
					new DragonAbilityHolder(
							HolderSet.direct(
									DeferredHolder.create(DragonAbility.REGISTRY, res("blast_breath")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("blast_dust_immunity")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("fault_line")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("pyroclastic_roar")),
									DeferredHolder.create(DragonAbility.REGISTRY, res("unstoppable"))
							),
							Optional.of(HolderSet.direct(DeferredHolder.create(ResourceKey.create(DragonSpecies.REGISTRY, DragonSurvival.res("cave_dragon"))))),
							false)
			)
	));
}
