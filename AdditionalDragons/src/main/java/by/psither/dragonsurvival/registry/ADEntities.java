package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.entity.CountdownAreaEffectCloud;
import by.psither.dragonsurvival.common.entity.projectiles.FaultLineProjectileEntity;
import by.psither.dragonsurvival.common.entity.projectiles.MistyBarbProjectileEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber( modid = AdditionalDragonsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ADEntities {
	public static EntityType<FaultLineProjectileEntity> FAULT_LINE;
	public static EntityType<CountdownAreaEffectCloud> COUNTDOWN_CLOUD;
	public static EntityType<MistyBarbProjectileEntity> MISTY_BARB;

	@SubscribeEvent
	public static void register(RegisterEvent event)
	{
		//AdditionalDragonsMod.LOGGER.info("Registering entities.");
		ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
		if (key.equals(Registry.ENTITY_TYPE_REGISTRY))
			registerEntities(event);
	}

	@SuppressWarnings("unchecked")
	public static void registerEntities(RegisterEvent event){
		FAULT_LINE = register(event, "fault_line", EntityType.Builder.<FaultLineProjectileEntity>of(FaultLineProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("additionaldragons:fault_line"));
		COUNTDOWN_CLOUD = register(event, "countdown_cloud", EntityType.Builder.<AreaEffectCloud>of(AreaEffectCloud::new, MobCategory.MISC).sized(6.0F, 0.5F).fireImmune().clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("additionaldragons:countdown_cloud"));
		MISTY_BARB = register(event, "misty_barb", EntityType.Builder.<MistyBarbProjectileEntity>of(MistyBarbProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("additionaldragons:misty_barb"));
		AdditionalDragonsMod.LOGGER.info("Registered entities!");
	}

	@SuppressWarnings("rawtypes")
	private static EntityType register(RegisterEvent event, String id, EntityType type){
		ResourceLocation location = new ResourceLocation(AdditionalDragonsMod.MODID, id);
		event.register(Registry.ENTITY_TYPE_REGISTRY, location, ()->type);
		return type;
	}
}