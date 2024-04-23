package by.psither.dragonsurvival;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.psither.dragonsurvival.client.particles.ADParticles;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod( AdditionalDragonsMod.MODID )
public class AdditionalDragonsMod {
	public static final String MODID = "additionaldragons";
	public static final Logger LOGGER = LogManager.getLogger("Additional Dragons");
	
	public AdditionalDragonsMod () {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ADDragonTypes.registerSubtypes();

		ADParticles.register();
		ADSoundRegistry.register();

		ADSoundRegistry.SOUNDS.register(modEventBus);
		ADParticles.REGISTRY.register(modEventBus);
		ADEntities.ENTITY_TYPES.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(this::serverRegisterCommandsEvent);
	}
	
	@SubscribeEvent
	public void serverRegisterCommandsEvent(RegisterCommandsEvent event){
		//CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
		//DragonAbilitySwapCommand.register(commandDispatcher);
		//DragonSubtypeSwapCommand.register(commandDispatcher);
		//LOGGER.info("Registered ability commands");
	}
}
