package by.psither.dragonsurvival.server.handlers;

import java.util.List;

import by.dragonsurvivalteam.dragonsurvival.network.NetworkHandler;
import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncPotionAddedEffect;
import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncPotionRemovedEffect;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;


@EventBusSubscriber
public class PotionSync{
	@SubscribeEvent
	public static void potionAdded(MobEffectEvent.Added event){
		List<MobEffect> effects = List.of(ADDragonEffects.BLAST_DUSTED, ADDragonEffects.BUBBLE_SHIELD, ADDragonEffects.CONFOUNDED, ADDragonEffects.HIGH_VOLTAGE, ADDragonEffects.INVIGORATE, ADDragonEffects.SEEKING_TALONS, ADDragonEffects.UNSTOPPABLE, ADDragonEffects.VOLCANIC_RAGE);
		if(!effects.contains(event.getEffectInstance().getEffect())) {
			return;
		}
		LivingEntity entity = event.getEntity();

		if(!entity.level().isClientSide){
			TargetPoint point = new TargetPoint(entity.position().x, entity.position().y, entity.position().z, 64, entity.level().dimension());
			NetworkHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> point), new SyncPotionAddedEffect(entity.getId(), MobEffect.getId(event.getEffectInstance().getEffect()), event.getEffectInstance().getDuration(), event.getEffectInstance().getAmplifier()));
		}
	}

	@SubscribeEvent
	public static void potionRemoved(MobEffectEvent.Expired event){
		List<MobEffect> effects = List.of(ADDragonEffects.BLAST_DUSTED, ADDragonEffects.BUBBLE_SHIELD, ADDragonEffects.CONFOUNDED, ADDragonEffects.HIGH_VOLTAGE, ADDragonEffects.INVIGORATE, ADDragonEffects.SEEKING_TALONS, ADDragonEffects.UNSTOPPABLE, ADDragonEffects.VOLCANIC_RAGE);
		if(event.getEffectInstance() == null || !effects.contains(event.getEffectInstance().getEffect())) {
			return;
		}
		LivingEntity entity = event.getEntity();

		if(!entity.level().isClientSide){
			TargetPoint point = new TargetPoint(entity.position().x, entity.position().y, entity.position().z, 64, entity.level().dimension());
			NetworkHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> point), new SyncPotionRemovedEffect(entity.getId(), MobEffect.getId(event.getEffectInstance().getEffect())));
		}
	}
}
