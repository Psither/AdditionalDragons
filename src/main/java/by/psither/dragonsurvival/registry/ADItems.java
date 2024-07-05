package by.psither.dragonsurvival.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.common.items.food.DragonFoodItem;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.items.AncientCatalystItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

public class ADItems {
	public static DeferredRegister<Item> AD_ITEMS = DeferredRegister.create(
			BuiltInRegistries.ITEM,
			MODID
	);

	private static final Properties defaultProperties = new Item.Properties();
	public static final Holder<Item> SLIPPERY_SUSHI = AD_ITEMS.register("slippery_sushi", () -> new DragonFoodItem(defaultProperties, DragonTypes.SEA, () -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, Functions.secondsToTicks(20))));
	public static final Holder<Item> CURSED_MARROW = AD_ITEMS.register("cursed_marrow", () -> new Item(new Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.8f).alwaysEdible().fast().effect(() -> new MobEffectInstance(ADDragonEffects.CONFOUNDED, Functions.secondsToTicks(20), 1), 1f).build())));
	public static final Holder<Item> ANCIENT_CATALYST = AD_ITEMS.register("ancient_catalyst", () -> new AncientCatalystItem(defaultProperties.stacksTo(1).rarity(Rarity.EPIC)));
}
