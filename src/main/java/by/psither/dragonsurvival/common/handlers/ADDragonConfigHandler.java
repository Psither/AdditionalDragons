package by.psither.dragonsurvival.common.handlers;

import java.util.HashMap;
import java.util.List;

import by.dragonsurvivalteam.dragonsurvival.common.handlers.DragonConfigHandler;
import by.dragonsurvivalteam.dragonsurvival.config.ConfigHandler;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.magic.abilities.CaveDragon.active.NetherBreathAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.abilities.ForestDragon.active.ForestBreathAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.abilities.SeaDragon.active.StormBreathAbility;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber
public class ADDragonConfigHandler {

	@SubscribeEvent
	public static void onConfigLoad(final ModConfigEvent event) {
		if (event.getConfig().getSpec() == ConfigHandler.serverSpec) {
			AdditionalDragonsMod.LOGGER.info("Rebuilding configuration...");

			rebuildSpeedupBlocksMap();
			rebuildBreathBlocks();
			rebuildManaBlocks();
		}
	}

	public static void rebuildSpeedupBlocksMap(){
		HashMap<String, List<Block>> speedupMap = new HashMap<>();
		speedupMap.put(ADDragonTypes.TECTONIC.getTypeName(), ConfigHandler.getResourceElements(Block.class, ServerConfig.caveSpeedupBlocks));
		speedupMap.put(ADDragonTypes.DEEPWOODS.getTypeName(), ConfigHandler.getResourceElements(Block.class, ServerConfig.forestSpeedupBlocks));
		speedupMap.put(ADDragonTypes.PRIMORDIAL.getTypeName(), ConfigHandler.getResourceElements(Block.class, ServerConfig.seaSpeedupBlocks));
		DragonConfigHandler.DRAGON_SPEEDUP_BLOCKS = speedupMap;
	}

	public static void rebuildBreathBlocks(){
		HashMap<String, List<Block>> breathMap = new HashMap<>();
		breathMap.put(ADDragonTypes.TECTONIC.getTypeName(), ConfigHandler.getResourceElements(Block.class, NetherBreathAbility.fireBreathBlockBreaks));
		breathMap.put(ADDragonTypes.DEEPWOODS.getTypeName(), ConfigHandler.getResourceElements(Block.class, ForestBreathAbility.forestBreathBlockBreaks));
		breathMap.put(ADDragonTypes.PRIMORDIAL.getTypeName(), ConfigHandler.getResourceElements(Block.class, StormBreathAbility.stormBreathBlockBreaks));
		DragonConfigHandler.DRAGON_BREATH_BLOCKS = breathMap;
	}

	public static void rebuildManaBlocks(){
		//HashMap<String, List<Block>> map = new HashMap<>();
		DragonConfigHandler.DRAGON_MANA_BLOCKS.put(ADDragonTypes.TECTONIC.getTypeName(), ConfigHandler.getResourceElements(Block.class, ServerConfig.caveDragonManaBlocks));
		DragonConfigHandler.DRAGON_MANA_BLOCKS.put(ADDragonTypes.DEEPWOODS.getTypeName(), ConfigHandler.getResourceElements(Block.class, ServerConfig.forestDragonManaBlocks));
		DragonConfigHandler.DRAGON_MANA_BLOCKS.put(ADDragonTypes.PRIMORDIAL.getTypeName(), ConfigHandler.getResourceElements(Block.class, ServerConfig.seaDragonManaBlocks));
	}
}
