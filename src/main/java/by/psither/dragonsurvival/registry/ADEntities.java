package by.psither.dragonsurvival.registry;

import org.apache.http.client.entity.EntityBuilder;

import com.google.common.collect.ImmutableSet;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvivalMod;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.entity.CountdownAreaEffectCloud;
import by.psither.dragonsurvival.common.entity.projectiles.FaultLineProjectileEntity;
import by.psither.dragonsurvival.common.entity.projectiles.MistyBarbProjectileEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber( modid = AdditionalDragonsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ADEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AdditionalDragonsMod.MODID);
	
	public static RegistryObject<EntityType<FaultLineProjectileEntity>> FAULT_LINE = ENTITY_TYPES.register("fault_line", () -> EntityType.Builder.<FaultLineProjectileEntity>of(FaultLineProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("fault_line"));
	public static RegistryObject<EntityType<CountdownAreaEffectCloud>> COUNTDOWN_CLOUD = ENTITY_TYPES.register("counterdown_cloud", () -> EntityType.Builder.<CountdownAreaEffectCloud>of(CountdownAreaEffectCloud::new, MobCategory.MISC).sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("countdown_cloud"));
	public static RegistryObject<EntityType<MistyBarbProjectileEntity>> MISTY_BARB = ENTITY_TYPES.register("misty_barb", () -> EntityType.Builder.<MistyBarbProjectileEntity>of(MistyBarbProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("misty_barb"));

	@SuppressWarnings("rawtypes")
	private static EntityType register(RegisterEvent event, String id, EntityType type){
		ResourceLocation location = new ResourceLocation(AdditionalDragonsMod.MODID, id);
		//event.register(Registry.ENTITY_TYPE_REGISTRY, location, ()->type);
		return type;
	}
}