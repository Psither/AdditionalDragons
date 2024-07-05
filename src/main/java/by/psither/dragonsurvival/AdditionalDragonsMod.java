package by.psither.dragonsurvival;

import by.psither.dragonsurvival.client.particles.ADParticles;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.registry.datagen.loot.MarrowLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.psither.dragonsurvival.client.particles.ADParticles.AD_PARTICLES;
import static by.psither.dragonsurvival.client.sounds.ADSoundRegistry.SOUNDS;
import static by.psither.dragonsurvival.registry.ADEntities.ENTITY_TYPES;
import static by.psither.dragonsurvival.registry.ADBlocks.AD_BLOCKS;
import static by.psither.dragonsurvival.registry.ADItems.AD_ITEMS;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADCreativeTabs;

@Mod( AdditionalDragonsMod.MODID )
public class AdditionalDragonsMod {
	public static final String MODID = "additionaldragons";
	public static final Logger LOGGER = LogManager.getLogger("Additional Dragons");

	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM =
			DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<MarrowLootModifier>>
			marrowLoot = AdditionalDragonsMod.GLM.register("marrow_loot", () -> MarrowLootModifier.CODEC);
	
	public AdditionalDragonsMod(IEventBus modEventBus, ModContainer modContainer) {
		ADDragonTypes.registerSubtypes();

		AD_BLOCKS.register(modEventBus);
		AD_ITEMS.register(modEventBus);
		AD_PARTICLES.register(modEventBus);
		SOUNDS.register(modEventBus);
		ENTITY_TYPES.register(modEventBus);

		ADSoundRegistry.register();
		
		ADCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

		//NeoForge.EVENT_BUS.register(this);
	}
}
