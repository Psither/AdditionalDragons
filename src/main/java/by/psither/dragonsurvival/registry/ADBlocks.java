package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvivalMod;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.blocks.*;
import by.dragonsurvivalteam.dragonsurvival.util.ResourceHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ADBlocks{
	public static HashMap<String, Block> AD_BLOCKS = new HashMap<>();
	public static HashMap<String, BlockItem> AD_BLOCK_ITEMS = new HashMap<>();

	public static GlowSlimeBlock glowSlime;


	@SubscribeEvent
	public static void registerEvent(final RegisterEvent event){
		ResourceKey<? extends Registry<?>> registerKey = event.getRegistryKey();
		if (registerKey.equals(Registry.BLOCK_REGISTRY)) {
			registerBlocks(event);
		} else if (registerKey.equals(Registry.ITEM_REGISTRY)) {
			registerBlockItems(event);
		}
	}
	protected static void registerBlocks(final RegisterEvent event){
		glowSlime = registerBlock(new GlowSlimeBlock(Block.Properties.of(Material.DIRT).strength(0.2F).noOcclusion().sound(SoundType.SLIME_BLOCK).lightLevel(GlowSlimeBlock::getLightLevel)), "glow_slime", event);
	}

	private static <B extends Block> B registerBlock(B block, String identifier, RegisterEvent event){
		event.register(Registry.BLOCK_REGISTRY, new ResourceLocation(AdditionalDragonsMod.MODID,identifier), ()->block);
		AD_BLOCKS.put(identifier, block);
		return block;
	}
	
	public static void registerBlockItems(final RegisterEvent event){
		ADBlocks.AD_BLOCKS.forEach((key, value) -> {
			registerItem(value, new Item.Properties().tab(DragonSurvivalMod.items), event);
		});
	}

	@SuppressWarnings( "ConstantConditions" )
	private static void registerItem(Block block, Item.Properties itemProperties, RegisterEvent event){
		BlockItem itm = new BlockItem(block, itemProperties.tab(DragonSurvivalMod.items));
		event.register(Registry.ITEM_REGISTRY, new ResourceLocation(ResourceHelper.getKey(block).toString()), ()->itm);
		AD_BLOCK_ITEMS.put(ResourceHelper.getKey(block).toString(), itm);
	}
}