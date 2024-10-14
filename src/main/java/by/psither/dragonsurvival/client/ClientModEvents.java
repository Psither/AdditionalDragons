package by.psither.dragonsurvival.client;

import by.psither.dragonsurvival.client.render.projectiles.FaultLineProjectileRenderer;
import by.psither.dragonsurvival.client.render.projectiles.MistyBarbProjectileRenderer;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber( bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ClientModEvents {
	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event)
	{
		EntityRenderers.register(ADEntities.FAULT_LINE.get(), FaultLineProjectileRenderer::new);
		EntityRenderers.register(ADEntities.COUNTDOWN_CLOUD.get(), NoopRenderer::new);
		EntityRenderers.register(ADEntities.MISTY_BARB.get(), MistyBarbProjectileRenderer::new);
	}
}