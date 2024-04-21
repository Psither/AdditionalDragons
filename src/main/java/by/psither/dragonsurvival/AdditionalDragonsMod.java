package by.psither.dragonsurvival;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;

import by.dragonsurvivalteam.dragonsurvival.client.particles.DSParticles;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.psither.dragonsurvival.client.particles.ADParticles;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.commands.DragonAbilitySwapCommand;
import by.psither.dragonsurvival.commands.DragonSubtypeSwapCommand;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
