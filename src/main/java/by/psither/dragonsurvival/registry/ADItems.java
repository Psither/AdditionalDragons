package by.psither.dragonsurvival.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.common.items.food.DragonFoodItem;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.items.AncientCatalystItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber( modid = AdditionalDragonsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ADItems {
	public static HashMap<String, Item> AD_ITEMS = new HashMap<>();
	public static Item slipperySushi, cursedMarrow;
	public static Item ancientCatalyst;
	
	@SubscribeEvent
	public static void register(final RegisterEvent event){
		if (!Objects.equals(event.getForgeRegistry(), ForgeRegistries.ITEMS)) {
			return;
		}
		Properties defaultProperties = new Item.Properties();
		slipperySushi = registerItem(event, new DragonFoodItem(defaultProperties, DragonTypes.SEA, () -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, Functions.secondsToTicks(20))), "slippery_sushi");
		cursedMarrow = registerItem(event, new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.8f).alwaysEat().fast().effect(new MobEffectInstance(ADDragonEffects.CONFOUNDED, Functions.secondsToTicks(20), 1), 1f).build())), "cursed_marrow");
		ancientCatalyst = registerItem(event, new AncientCatalystItem(defaultProperties.stacksTo(1).rarity(Rarity.EPIC)), "ancient_catalyst");
	}

	public static Item registerItem(RegisterEvent event, String name, String description){
		Item item = new Item(new Item.Properties()){
			@Override
			public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag){
				super.appendHoverText(stack, world, list, tooltipFlag);
				list.add(Component.translatable(description));
			}
		};
		event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(AdditionalDragonsMod.MODID, name), ()->item);
		AD_ITEMS.put(name, item);
		return item;
	}

	public static Item registerItem(RegisterEvent event, Item item, String name){
		event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(AdditionalDragonsMod.MODID, name), ()->item);
		AD_ITEMS.put(name, item);
		return item;
	}
}
