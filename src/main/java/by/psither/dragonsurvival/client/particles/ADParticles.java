package by.psither.dragonsurvival.client.particles;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticle;
import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.DragonBubbleParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.LargeGlowSlimeParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.SmallGlowSlimeParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber( bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ADParticles{
	public static final DeferredRegister<ParticleType<?>> AD_PARTICLES = DeferredRegister.create(
			BuiltInRegistries.PARTICLE_TYPE, AdditionalDragonsMod.MODID);

	public static final DeferredHolder<ParticleType<?>, ParticleType<LargeGlowSlimeParticle.Data>> LARGE_GLOWSLIME = AD_PARTICLES.register(
			"large_glowslime",
			() -> LargeGlowSlimeParticle.Data.TYPE
	);

	public static final DeferredHolder<ParticleType<?>, ParticleType<SmallGlowSlimeParticle.Data>> SMALL_GLOWSLIME = AD_PARTICLES.register(
			"small_glowslime",
			() -> SmallGlowSlimeParticle.Data.TYPE
	);
	
	public static final DeferredHolder<ParticleType<?>, ParticleType<LargeBlastDustParticle.Data>> LARGE_BLAST_DUST = AD_PARTICLES.register(
			"large_blast_dust",
			() -> LargeBlastDustParticle.Data.TYPE
	);

	public static final DeferredHolder<ParticleType<?>, ParticleType<SmallConfoundParticle.Data>> SMALL_CONFOUND = AD_PARTICLES.register(
			"small_confound",
			() -> SmallConfoundParticle.Data.TYPE
	);

	public static final DeferredHolder<ParticleType<?>, ParticleType<DragonBubbleParticle.Data>> DRAGON_BUBBLE = AD_PARTICLES.register(
			"dragon_bubble",
			() -> DragonBubbleParticle.Data.TYPE
	);

	//Insecure modifications
	@SubscribeEvent( priority = EventPriority.LOWEST)
	public static void registerParticles(RegisterParticleProvidersEvent event){
		event.registerSpriteSet(ADParticles.SMALL_GLOWSLIME.get(), SmallGlowSlimeParticle.Factory::new);
		event.registerSpriteSet(ADParticles.LARGE_GLOWSLIME.get(), LargeGlowSlimeParticle.Factory::new);
		event.registerSpriteSet(ADParticles.LARGE_BLAST_DUST.get(), LargeBlastDustParticle.Factory::new);
		event.registerSpriteSet(ADParticles.SMALL_CONFOUND.get(), SmallConfoundParticle.Factory::new);
		event.registerSpriteSet(ADParticles.DRAGON_BUBBLE.get(), DragonBubbleParticle.Factory::new);
	}
}