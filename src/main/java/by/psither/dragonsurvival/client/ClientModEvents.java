package by.psither.dragonsurvival.client;

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
		EntityRenderers.register(ADEntities.FAULT_LINE, FaultLineProjectileRenderer::new);
		EntityRenderers.register(ADEntities.COUNTDOWN_CLOUD, NoopRenderer::new);
		EntityRenderers.register(ADEntities.MISTY_BARB, MistyBarbProjectileRenderer::new);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event){
		event.register(ADParticles.dragonBubbleParticle, p_create_1_ -> new ParticleProvider<SimpleParticleType>(){
			@Override
			public @NotNull Particle createParticle(@NotNull SimpleParticleType p_199234_1_, @NotNull ClientLevel clientWorld, double v, double v1, double v2, double v3, double v4, double v5) {
				DragonBubbleParticle dragonBubbleParticle = new DragonBubbleParticle(clientWorld, v, v1, v2, v3, v4, v5);
				dragonBubbleParticle.pickSprite(p_create_1_);
				return dragonBubbleParticle;
			}
		});
		event.register(ADParticles.questionMarkParticle, p_create_1_ -> new ParticleProvider<SimpleParticleType>(){
			@Override
			public @NotNull Particle createParticle(@NotNull SimpleParticleType p_199234_1_, @NotNull ClientLevel clientWorld, double v, double v1, double v2, double v3, double v4, double v5) {
				QuestionMarkParticle questionMarkParticle = new QuestionMarkParticle(clientWorld, v, v1, v2, v3, v4, v5);
				questionMarkParticle.pickSprite(p_create_1_);
				return questionMarkParticle;
			}
		});
	}
}