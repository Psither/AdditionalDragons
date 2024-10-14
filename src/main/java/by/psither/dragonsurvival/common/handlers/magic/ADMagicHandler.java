package by.psither.dragonsurvival.common.handlers.magic;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.magic.ClientMagicHandler;
import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.SeaDragon.LargeLightningParticle;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticle;
import by.psither.dragonsurvival.common.effects.BlastDustedEffect;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.ConfoundingBreathAbility;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.InvigorateAbility;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.SeekingTalonsAbility;
import by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active.BubbleShieldAbility;
import by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active.HighVoltageAbility;
import by.psither.dragonsurvival.registry.ADDamageTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@SuppressWarnings("unused")
@EventBusSubscriber
public class ADMagicHandler {
	private static final ResourceLocation INVIGORATE_MOVEMENT_SPEED = ResourceLocation.fromNamespaceAndPath(MODID, "invigorate_movement_speed");
	private static final ResourceLocation INVIGORATE_ATTACK_SPEED = ResourceLocation.fromNamespaceAndPath(MODID, "invigorate_attack_speed");
	private static final ResourceLocation INVIGORATE_LUCK = ResourceLocation.fromNamespaceAndPath(MODID, "invigorate_luck");

	public static void changeLightModifiers(LivingEntity entity, int amp, boolean inLight) {
		AttributeInstance moveSpeedAtt = entity.getAttribute(Attributes.MOVEMENT_SPEED);
		AttributeInstance attackSpeedAtt = entity.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance luckAtt = entity.getAttribute(Attributes.LUCK);

		AttributeModifier MOVEMENT_SPEED_BONUS = new AttributeModifier(INVIGORATE_MOVEMENT_SPEED, InvigorateAbility.invigorateMovementSpeedBonus * (amp + 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		AttributeModifier ATTACK_SPEED_BONUS = new AttributeModifier(INVIGORATE_ATTACK_SPEED, InvigorateAbility.invigorateAttackSpeedBonus * (amp + 1), AttributeModifier.Operation.ADD_VALUE);
		AttributeModifier LUCK_BONUS = new AttributeModifier(INVIGORATE_LUCK, InvigorateAbility.invigorateLuckBonus * (amp + 1), AttributeModifier.Operation.ADD_VALUE);

		if (inLight) {
			if (moveSpeedAtt != null && moveSpeedAtt.getModifier(INVIGORATE_MOVEMENT_SPEED) == null)
				moveSpeedAtt.addTransientModifier(MOVEMENT_SPEED_BONUS);
			if (attackSpeedAtt != null && attackSpeedAtt.getModifier(INVIGORATE_ATTACK_SPEED) == null)
				attackSpeedAtt.addTransientModifier(ATTACK_SPEED_BONUS);
			if (luckAtt != null && luckAtt.getModifier(INVIGORATE_LUCK) == null)
				luckAtt.addTransientModifier(LUCK_BONUS);
		} else {
			if (moveSpeedAtt != null && moveSpeedAtt.getModifier(INVIGORATE_MOVEMENT_SPEED) != null)
				moveSpeedAtt.removeModifier(MOVEMENT_SPEED_BONUS);
			if (attackSpeedAtt != null && attackSpeedAtt.getModifier(INVIGORATE_ATTACK_SPEED) != null)
				attackSpeedAtt.removeModifier(ATTACK_SPEED_BONUS);
			if (luckAtt != null && luckAtt.getModifier(INVIGORATE_LUCK) != null)
				luckAtt.removeModifier(LUCK_BONUS);
		}
	}

	@SubscribeEvent
	public static void showParticles(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity entity) {

			if (!entity.level().isClientSide()) {
				return;
			}
			if (!ClientMagicHandler.particlesOnDragons && DragonStateProvider.isDragon(entity)) {
				return;
			}

			if (entity.tickCount % 5 == 0) {
				if (entity.hasEffect(ADDragonEffects.CONFOUNDED)) {
					ParticleOptions data = new SmallConfoundParticle.Data(37F, false);
					for (int i = 0; i < 4; i++) {
						ClientMagicHandler.renderEffectParticle(entity, data);
					}
				}
				if (entity.hasEffect(ADDragonEffects.BUBBLE_SHIELD)) {
					BubbleShieldAbility.produceBubbles(entity);
				}
				if (entity.hasEffect(ADDragonEffects.HIGH_VOLTAGE)) {
					HighVoltageAbility.producePassiveParticles(entity, entity.getEffect(ADDragonEffects.HIGH_VOLTAGE).getAmplifier());
					ClientMagicHandler.renderEffectParticle(entity, new LargeLightningParticle.Data(37, false));
				}
				if (entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
					BlastDustedEffect.showSmoke(entity, entity.getEffect(ADDragonEffects.BLAST_DUSTED));
				}
			}

			if (entity.tickCount % 5 == 0) {
				// 4 times per second
				if (entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
					if (entity.isInWaterRainOrBubble()) {
						if (!entity.level().isClientSide())
							entity.removeEffect(ADDragonEffects.BLAST_DUSTED);
						else
							entity.level().playLocalSound(entity.position().x, entity.position().y + 0.5, entity.position().z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.3F, true);
					}
					if (entity.isOnFire()) {
						MobEffectInstance instance = entity.getEffect(ADDragonEffects.BLAST_DUSTED);
						((BlastDustedEffect) instance.getEffect()).detonate(event.getEntity(), instance.getAmplifier());
						entity.removeEffect(instance.getEffect());
					}
				}
				if (entity.hasEffect(ADDragonEffects.BUBBLE_SHIELD)) {
					if (entity instanceof Player player)
						BubbleShieldAbility.restoreHydrationAndAir(player);
				}
				if (entity.hasEffect(ADDragonEffects.HIGH_VOLTAGE)) {
					int amp = entity.getEffect(ADDragonEffects.HIGH_VOLTAGE).getAmplifier();
					HighVoltageAbility.attackNearbyTargets(entity, amp);
				}
				if (entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
					if (entity instanceof Player player) {
						if (DragonUtils.isDragonType(player, DragonTypes.CAVE))
							entity.removeEffect(ADDragonEffects.BLAST_DUSTED);
					}
				}
				if (entity.hasEffect(ADDragonEffects.UNSTOPPABLE)) {
					float healthFrac = (entity.getHealth() / entity.getMaxHealth());
					if (healthFrac < 0.5) {
						entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 3));
					} else if (healthFrac < 0.6) {
						entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 2));
					} else if (healthFrac < 0.8) {
						entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));
					} else if (healthFrac < 0.9) {
						entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 0));
					}
				}
				if (entity.hasEffect(ADDragonEffects.CONFOUNDED)) {
					if (entity instanceof Player player) {
						ConfoundingBreathAbility.confoundPlayer(player, player.getEffect(ADDragonEffects.CONFOUNDED).getAmplifier());
					}
				}
			}
			if (entity.tickCount % 20 == 0) {
				// Every second
				if (entity.hasEffect(ADDragonEffects.INVIGORATE)) {
					if (!entity.level().isClientSide()) {
						int amp = entity.getEffect(ADDragonEffects.INVIGORATE).getAmplifier();
						LevelLightEngine lightManager = entity.level().getChunkSource().getLightEngine();
						changeLightModifiers(entity, amp, lightManager.getLayerListener(LightLayer.BLOCK).getLightValue(entity.blockPosition()) < 3 && lightManager.getLayerListener(LightLayer.SKY).getLightValue(entity.blockPosition()) < 3 && lightManager.getLayerListener(LightLayer.SKY).getLightValue(entity.blockPosition().above()) < 3);
					}
				} else {
					changeLightModifiers(entity, 0, false);
				}
			}
			if (entity.tickCount % 100 == 0) {
				// Every 5 seconds
				if (entity.hasEffect(ADDragonEffects.CONFOUNDED)) {
					if (entity instanceof Mob mob) {
						ConfoundingBreathAbility.changeTargetToRandomMob(mob);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void livingDamage(LivingDamageEvent.Post event) {
		if (event.getSource().getEntity() != null) {
			LivingEntity damagedEntity = event.getEntity();
			Entity damageSource = event.getSource().getEntity();
			if (damagedEntity.hasEffect(ADDragonEffects.HIGH_VOLTAGE)) {
				MobEffectInstance hvEffect = event.getEntity().getEffect(ADDragonEffects.HIGH_VOLTAGE);
				float range = (float) HighVoltageAbility.getActiveRange(hvEffect.getAmplifier());
				if (damageSource.distanceTo(damagedEntity) < range) {
					// If the aggressor is in range of the ability's effect when attacking... Zap em.
					HighVoltageAbility.zapTarget(damagedEntity, damageSource, hvEffect.getAmplifier());
				}
			}
		}

        if (!event.getEntity().level().isClientSide()) {
			LivingEntity entity = event.getEntity();
			DamageSource src = event.getSource();
			if (src.getEntity() instanceof LivingEntity en && !en.equals(entity) && en.hasEffect(ADDragonEffects.CONFOUNDED)) {
				ConfoundingBreathAbility.reflectDamage(en, en.getEffect(ADDragonEffects.CONFOUNDED).getAmplifier(), event.getNewDamage());
			} else if (src.getDirectEntity() instanceof LivingEntity en && !en.equals(entity) && en.hasEffect(ADDragonEffects.CONFOUNDED)) {
				ConfoundingBreathAbility.reflectDamage(en, en.getEffect(ADDragonEffects.CONFOUNDED).getAmplifier(), event.getNewDamage());
			}
			if (entity.hasEffect(ADDragonEffects.BUBBLE_SHIELD) && entity.level() instanceof ServerLevel) {
				if (event.getEntity().getAbsorptionAmount() <= 0)
					event.getEntity().removeEffect(ADDragonEffects.BUBBLE_SHIELD);
			}

			if (event.getSource().is(DamageTypeTags.IS_FIRE) && entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
				if (event.getEntity() instanceof Player player)
					if (DragonUtils.isDragonType(player, DragonTypes.CAVE))
						player.removeEffect(ADDragonEffects.BLAST_DUSTED);
				MobEffectInstance instance = event.getEntity().getEffect(ADDragonEffects.BLAST_DUSTED);
				((BlastDustedEffect) instance.getEffect()).detonate(event.getEntity(), instance.getAmplifier());
				event.getEntity().removeEffect(instance.getEffect());
			}
		}
	}

	@SubscribeEvent
	public static void criticalHit(CriticalHitEvent event) {
		if (event.getTarget() instanceof LivingEntity target) {
			if (target.getHealth() <= 0) return;
			LivingEntity entity = event.getEntity();
			if (entity.hasEffect(ADDragonEffects.SEEKING_TALONS)) {
				double critboost = SeekingTalonsAbility.seekingTalonsCritBonus * (1 - (target.getHealth() / target.getMaxHealth()));
				critboost *= (entity.getEffect(ADDragonEffects.SEEKING_TALONS).getAmplifier() + 1);
				event.setDamageMultiplier((float) (event.getDamageMultiplier() + critboost));
			}
		}
	}

	/*@SubscribeEvent
	public static void lootingEvent(LootingLevelEvent event) {
		if (event.getDamageSource() != null && event.getDamageSource().getEntity() instanceof LivingEntity source) {
			if (source.hasEffect(ADDragonEffects.SEEKING_TALONS)) {
				int bonus = (int) (SeekingTalonsAbility.seekingTalonsBonusLoot * (source.getEffect(ADDragonEffects.SEEKING_TALONS).getAmplifier() + 1));
				event.setLootingLevel(event.getLootingLevel() + bonus);
			}
		}
	}*/

	@SubscribeEvent
	public static void livingHurt(LivingDamageEvent.Pre event) {
		// Cave dragons are immune to their own blast dust damage.
		if (event.getSource().is(ADDamageTypes.BLAST_DUST) && (DragonStateProvider.isDragon(event.getEntity()) && DragonUtils.isDragonType(event.getEntity(), DragonTypes.CAVE))) {
			event.setNewDamage(0);
		}
	}

	@SubscribeEvent
	public static void effectRemoved(MobEffectEvent.Remove event) {
		if (event.getEntity().level().isClientSide()) {
			LivingEntity entity = event.getEntity();
			if (!(DragonStateProvider.isDragon(entity) && DragonUtils.isDragonType(entity, DragonTypes.CAVE)))
			{
				if (event.getEffect() == ADDragonEffects.BLAST_DUSTED) {
					entity.level().playLocalSound(entity.position().x, entity.position().y + 0.5, entity.position().z, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.3F, 1.3F, true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void effectExpired(MobEffectEvent.Expired event) {
		if (!(DragonStateProvider.isDragon(event.getEntity()) && DragonUtils.isDragonType(event.getEntity(), DragonTypes.CAVE))) {
			if (event.getEffectInstance() != null && event.getEffectInstance().getEffect() instanceof BlastDustedEffect effect)
				effect.detonate(event.getEntity(), event.getEffectInstance().getAmplifier());
		}
	}
}
