package by.psither.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.psither.dragonsurvival.registry.*;
import by.psither.dragonsurvival.registry.datagen.loot.MarrowLootModifier;
import by.psither.dragonsurvival.registry.datagen.loot.AttributeBonusLootModifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod( AdditionalDragonsMod.MODID )
public class AdditionalDragonsMod {
	public static final String MODID = "additionaldragons";
	public static final Logger LOGGER = LogManager.getLogger("Additional Dragons");

	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<MarrowLootModifier>> MARROW_LOOT = GLM.register("marrow", MarrowLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AttributeBonusLootModifier>> ATTRIBUTE_BONUS_LOOT = GLM.register("attribute_bonus_loot", AttributeBonusLootModifier.CODEC);

	public AdditionalDragonsMod(IEventBus modEventBus, ModContainer modContainer) {
		ADBlocks.REGISTRY.register(modEventBus);
		ADItems.REGISTRY.register(modEventBus);
		ADParticles.REGISTRY.register(modEventBus);
		ADSounds.REGISTRY.register(modEventBus);
		ADAttributes.REGISTRY.register(modEventBus);
		ADEffects.REGISTRY.register(modEventBus);
		ADSounds.register();
		GLM.register(modEventBus);
	}

	public static ResourceLocation res(final String path) {
		return DragonSurvival.location(AdditionalDragonsMod.MODID, path);
	}
}
