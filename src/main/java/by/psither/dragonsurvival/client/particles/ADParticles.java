package by.psither.dragonsurvival.client.particles;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticle;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticleData;
import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticle;
import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticleData;
import by.psither.dragonsurvival.client.particles.SeaDragon.DragonBubbleParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.DragonBubbleParticleData;
import by.psither.dragonsurvival.client.particles.SeaDragon.LargeGlowSlimeParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.LargeGlowSlimeParticleData;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ADParticles{
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AdditionalDragonsMod.MODID);

	public static SimpleParticleType dragonBubbleParticle, questionMarkParticle;

	public static void register()
	{
		dragonBubbleParticle = new SimpleParticleType(false);
		//questionMarkParticle = new SimpleParticleType(false);
		REGISTRY.register("dragon_bubble", ()->dragonBubbleParticle);
		//REGISTRY.register("question_mark", ()->questionMarkParticle);
	}

	//Insecure modifications
	@SubscribeEvent( priority = EventPriority.LOWEST)
	public static void registerParticles(RegisterParticleProvidersEvent event){
		event.registerSpriteSet(ADParticles.LARGE_GLOWSLIME.get(), LargeGlowSlimeParticle.ParticleFactory::new);
		event.registerSpriteSet(ADParticles.LARGE_BLAST_DUST.get(), LargeBlastDustParticle.ParticleFactory::new);
		event.registerSpriteSet(ADParticles.SMALL_CONFOUND.get(), SmallConfoundParticle.ParticleFactory::new);
	}

	public static final RegistryObject<ParticleType<LargeGlowSlimeParticleData>> LARGE_GLOWSLIME = REGISTRY.register("large_glowslime", () -> new ParticleType<>(false, LargeGlowSlimeParticleData.DESERIALIZER){
		@Override
		public Codec<LargeGlowSlimeParticleData> codec(){
			return LargeGlowSlimeParticleData.CODEC(LARGE_GLOWSLIME.get());
		}
	});
	
	public static final RegistryObject<ParticleType<LargeBlastDustParticleData>> LARGE_BLAST_DUST = REGISTRY.register("large_blast_dust", () -> new ParticleType<>(false, LargeBlastDustParticleData.DESERIALIZER){
		@Override
		public Codec<LargeBlastDustParticleData> codec(){
			return LargeBlastDustParticleData.CODEC(LARGE_BLAST_DUST.get());
		}
	});

	public static final RegistryObject<ParticleType<SmallConfoundParticleData>> SMALL_CONFOUND = REGISTRY.register("small_confound", () -> new ParticleType<>(false, SmallConfoundParticleData.DESERIALIZER) {
		@Override
		public Codec<SmallConfoundParticleData> codec(){
			return SmallConfoundParticleData.CODEC(SMALL_CONFOUND.get());
		}
	});
}