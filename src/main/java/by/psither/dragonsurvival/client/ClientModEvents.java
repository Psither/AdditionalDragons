package by.psither.dragonsurvival.client;

import by.dragonsurvivalteam.dragonsurvival.client.particles.BeaconParticle;
import org.jetbrains.annotations.NotNull;

import by.psither.dragonsurvival.client.particles.ADParticles;
import by.psither.dragonsurvival.client.particles.ForestDragon.QuestionMarkParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.DragonBubbleParticle;
import by.psither.dragonsurvival.client.render.projectiles.FaultLineProjectileRenderer;
import by.psither.dragonsurvival.client.render.projectiles.MistyBarbProjectileRenderer;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ClientModEvents {
	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event)
	{
		EntityRenderers.register(ADEntities.FAULT_LINE.get(), FaultLineProjectileRenderer::new);
		EntityRenderers.register(ADEntities.COUNTDOWN_CLOUD.get(), NoopRenderer::new);
		EntityRenderers.register(ADEntities.MISTY_BARB.get(), MistyBarbProjectileRenderer::new);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event){
		event.registerSpriteSet(ADParticles.dragonBubbleParticle, spriteSet -> (particleType, clientWorld, x, y, z, speedX, speedY, speedZ) -> {
			BeaconParticle beaconParticle = new BeaconParticle(clientWorld, x, y, z, speedX, speedY, speedZ);
			beaconParticle.pickSprite(spriteSet);
			return beaconParticle;
		});
	}
}