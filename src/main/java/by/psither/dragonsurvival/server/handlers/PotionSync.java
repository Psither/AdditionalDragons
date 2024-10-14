package by.psither.dragonsurvival.server.handlers;

import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncPotionAddedEffect;
import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncPotionRemovedEffect;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class PotionSync{
	@SubscribeEvent
	public static void potionAdded(MobEffectEvent.Added event){
		List<Holder<MobEffect>> effects = List.of(ADDragonEffects.BLAST_DUSTED, ADDragonEffects.BUBBLE_SHIELD, ADDragonEffects.CONFOUNDED, ADDragonEffects.HIGH_VOLTAGE, ADDragonEffects.INVIGORATE, ADDragonEffects.SEEKING_TALONS, ADDragonEffects.UNSTOPPABLE, ADDragonEffects.VOLCANIC_RAGE, ADDragonEffects.PHASE_OUT, ADDragonEffects.UNREALITY);
		if(!effects.contains(event.getEffectInstance().getEffect())) {
			return;
		}
		LivingEntity entity = event.getEntity();

		if(!entity.level().isClientSide()){
			PacketDistributor.sendToPlayersNear((ServerLevel)entity.level(), null, entity.position().x, entity.position().y, entity.position().z, 64, new SyncPotionAddedEffect.Data(entity.getId(), BuiltInRegistries.MOB_EFFECT.getId(event.getEffectInstance().getEffect().value()), event.getEffectInstance().getDuration(), event.getEffectInstance().getAmplifier()));
		}
	}

	@SubscribeEvent
	public static void potionRemoved(MobEffectEvent.Expired event){
		List<Holder<MobEffect>> effects = List.of(ADDragonEffects.BLAST_DUSTED, ADDragonEffects.BUBBLE_SHIELD, ADDragonEffects.CONFOUNDED, ADDragonEffects.HIGH_VOLTAGE, ADDragonEffects.INVIGORATE, ADDragonEffects.SEEKING_TALONS, ADDragonEffects.UNSTOPPABLE, ADDragonEffects.VOLCANIC_RAGE, ADDragonEffects.PHASE_OUT, ADDragonEffects.UNREALITY);
		if(event.getEffectInstance() == null || !effects.contains(event.getEffectInstance().getEffect())) {
			return;
		}
		LivingEntity entity = event.getEntity();

		if(!entity.level().isClientSide()){
			PacketDistributor.sendToPlayersNear((ServerLevel)entity.level(), null, entity.position().x, entity.position().y, entity.position().z, 64, new SyncPotionRemovedEffect.Data(entity.getId(), BuiltInRegistries.MOB_EFFECT.getId(event.getEffectInstance().getEffect().value())));
		}
	}
}
