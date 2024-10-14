package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.entity.CountdownAreaEffectCloud;
import by.psither.dragonsurvival.common.entity.projectiles.FaultLineProjectileEntity;
import by.psither.dragonsurvival.common.entity.projectiles.MistyBarbProjectileEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ADEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, AdditionalDragonsMod.MODID);
	
	public static DeferredHolder<EntityType<?>, EntityType<FaultLineProjectileEntity>> FAULT_LINE = ENTITY_TYPES.register("fault_line", () -> EntityType.Builder.<FaultLineProjectileEntity>of(FaultLineProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("fault_line"));
	public static DeferredHolder<EntityType<?>, EntityType<CountdownAreaEffectCloud>> COUNTDOWN_CLOUD = ENTITY_TYPES.register("countdown_cloud", () -> EntityType.Builder.<CountdownAreaEffectCloud>of(CountdownAreaEffectCloud::new, MobCategory.MISC).sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("countdown_cloud"));
	public static DeferredHolder<EntityType<?>, EntityType<MistyBarbProjectileEntity>> MISTY_BARB = ENTITY_TYPES.register("misty_barb", () -> EntityType.Builder.<MistyBarbProjectileEntity>of(MistyBarbProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("misty_barb"));

	@SuppressWarnings("rawtypes")
	private static EntityType register(RegisterEvent event, String id, EntityType type){
		ResourceLocation location = ResourceLocation.fromNamespaceAndPath(AdditionalDragonsMod.MODID, id);
		//event.register(Registry.ENTITY_TYPE, location, ()->type);
		return type;
	}
}