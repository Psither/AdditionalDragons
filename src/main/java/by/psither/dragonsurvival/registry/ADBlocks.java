package by.psither.dragonsurvival.registry;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

import by.psither.dragonsurvival.common.blocks.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ADBlocks{
	public static DeferredRegister<Block> REGISTRY = DeferredRegister.create(
			BuiltInRegistries.BLOCK,
			MODID
	);

	public static GlowSlimeBlock glowSlime;

	public static final DeferredHolder<Block, GlowSlimeBlock> GLOW_SLIME = REGISTRY.register(
			"glow_slime",
			() -> new GlowSlimeBlock(Block.Properties.of()
					.mapColor(DyeColor.CYAN)
					.strength(0.2F)
					.sound(SoundType.SLIME_BLOCK)
					.lightLevel(GlowSlimeBlock::getLightLevel)
					.noOcclusion())
	);

	public static final Holder<Item> GLOW_SLIME_ITEM = ADItems.REGISTRY.register(
			"glow_slime",
			() -> new BlockItem(GLOW_SLIME.get(), new Item.Properties())
	);
}