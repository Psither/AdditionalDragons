package by.psither.dragonsurvival.common.handlers.magic;

import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticleData;
import by.psither.dragonsurvival.common.effects.BlastDustedEffect;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.ConfoundingBreathAbility;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.InvigorateAbility;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.SeekingTalonsAbility;
import by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active.BubbleShieldAbility;
import by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active.HighVoltageAbility;

import java.util.Collection;
import java.util.UUID;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.magic.ClientMagicHandler;
import by.dragonsurvivalteam.dragonsurvival.client.particles.SeaDragon.LargeLightningParticleData;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.DragonFoodHandler;
import by.psither.dragonsurvival.registry.ADDamageSources;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import by.psither.dragonsurvival.registry.ADItems;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ADMagicHandler {
	private static final UUID INVIGORATE_MOVEMENT_SPEED = UUID.fromString("69501c08-8b19-4d85-a910-1f413fb2d407");
	private static final UUID INVIGORATE_ATTACK_SPEED = UUID.fromString("28e81e47-dbfc-43a8-bb64-e44b4064e837");
	private static final UUID INVIGORATE_LUCK = UUID.fromString("7b47afb3-61f0-41a0-89a4-572243a5abf0");

	public static void changeLightModifiers(LivingEntity entity, int amp, boolean inLight) {
		AttributeInstance moveSpeedAtt = entity.getAttribute(Attributes.MOVEMENT_SPEED);
		AttributeInstance attackSpeedAtt = entity.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance luckAtt = entity.getAttribute(Attributes.LUCK);

		AttributeModifier MOVEMENT_SPEED_BONUS = new AttributeModifier(INVIGORATE_MOVEMENT_SPEED, "INVIGORATE_MOVEMENT_SPEED", InvigorateAbility.invigorateMovementSpeedBonus * (amp + 1), AttributeModifier.Operation.MULTIPLY_TOTAL);
		AttributeModifier ATTACK_SPEED_BONUS = new AttributeModifier(INVIGORATE_ATTACK_SPEED, "INVIGORATE_ATTACK_SPEED", InvigorateAbility.invigorateAttackSpeedBonus * (amp + 1), AttributeModifier.Operation.ADDITION);
		AttributeModifier LUCK_BONUS = new AttributeModifier(INVIGORATE_LUCK, "INVIGORATE_LUCK", InvigorateAbility.invigorateLuckBonus * (amp + 1), AttributeModifier.Operation.ADDITION);

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

	/*@SubscribeEvent
	public static void onEat(LivingEntityUseItemEvent.Finish event) {
		LivingEntity livingentity = event.getEntity();
		if (event.getItem().is(ADItems.revolvingHearts)) {
			for (int i = 0; i < 5; i++) {
				float randX = (livingentity.getRandom().nextFloat() - 0.5f) * 0.7f;
				float randY = (livingentity.getRandom().nextFloat() - 0.5f) * 0.7f;
				float randZ = (livingentity.getRandom().nextFloat() - 0.5f) * 0.7f;
				livingentity.level.addAlwaysVisibleParticle(ParticleTypes.HEART, livingentity.getX() + randX, livingentity.getY() + livingentity.getEyeHeight() + randY, livingentity.getZ() + randZ, 0.0, 0.0, 0.0);
			}
		}
	}*/

	@SubscribeEvent
	public static void showParticles(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();

		if (!entity.level.isClientSide) {
			return;
		}
		if (!ClientMagicHandler.particlesOnDragons && DragonUtils.isDragon(entity)) {
			return;
		}

		if (entity.tickCount % 5 == 0) {
			if (entity.hasEffect(ADDragonEffects.CONFOUNDED)) {
				ParticleOptions data = new SmallConfoundParticleData(37F, false);
				for (int i = 0; i < 4; i++) {
					ClientMagicHandler.renderEffectParticle(entity, data);
				}
			}
			if (entity.hasEffect(ADDragonEffects.BUBBLE_SHIELD)) {
				BubbleShieldAbility.produceBubbles(entity);
			}
			if (entity.hasEffect(ADDragonEffects.HIGH_VOLTAGE)) {
				HighVoltageAbility.producePassiveParticles(entity, entity.getEffect(ADDragonEffects.HIGH_VOLTAGE).getAmplifier());
				ClientMagicHandler.renderEffectParticle(entity, new LargeLightningParticleData(37, false));
			}
			if (entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
				BlastDustedEffect.showSmoke(entity, entity.getEffect(ADDragonEffects.BLAST_DUSTED));
			}
		}
	}

	@SubscribeEvent
	public static void livingTick(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();

		if(entity.tickCount % 5 == 0) {
			// 4 times per second
			if(entity.hasEffect(ADDragonEffects.BLAST_DUSTED)){
				if (entity.isInWaterRainOrBubble()) {
					if (!entity.level.isClientSide())
						entity.removeEffect(ADDragonEffects.BLAST_DUSTED);
					else
						entity.getLevel().playLocalSound(entity.position().x, entity.position().y + 0.5, entity.position().z, SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0F, 1.3F, true);
				}
				if (entity.isOnFire()) {
					MobEffectInstance instance = entity.getEffect(ADDragonEffects.BLAST_DUSTED);
					((BlastDustedEffect) instance.getEffect()).detonate(event.getEntity(), instance.getAmplifier());
					event.getEntity().removeEffect(instance.getEffect());
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
				MobEffectInstance bde = entity.getEffect(ADDragonEffects.BLAST_DUSTED);
				if (entity instanceof Player player) {
					if (DragonUtils.isDragonType(player, DragonTypes.CAVE))
						entity.removeEffect(ADDragonEffects.BLAST_DUSTED);
				}
				if (bde != null) {
					if (bde.getDuration() > 0) ;
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
			if(entity.hasEffect(ADDragonEffects.INVIGORATE)) {
				if (!entity.level.isClientSide) {
					int amp = entity.getEffect(ADDragonEffects.INVIGORATE).getAmplifier();
					LevelLightEngine lightManager = entity.level.getChunkSource().getLightEngine();
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

	@SubscribeEvent
	public static void dropsEvent(LivingDropsEvent event) {
		LivingEntity entity = event.getEntity();
		Entity source = event.getSource().getEntity();
		if (entity == null || entity.level.isClientSide())
			return;
		Collection<ItemEntity> drops = event.getDrops();
		if (entity.hasEffect(ADDragonEffects.CONFOUNDED) && !(DragonUtils.isDragon(event.getEntity()) && DragonUtils.isDragonType(event.getEntity(), DragonTypes.FOREST))) {
			// Look through their drops and see if we find anything forest dragons can eat.
			boolean isEdible = false;
			int bones = 0;
			for (ItemEntity ie : drops) {
				ItemStack is = ie.getItem();
				if (is.getItem().equals(Items.BONE)) {
					// Curse any bones that drop.
					bones = is.getCount();
					is.setCount(0);
				}
				if (DragonFoodHandler.isDragonEdible(is.getItem(), DragonTypes.FOREST)) {
					isEdible = true;
				}
			}
			if (isEdible) {
				int res = 0;
				try {
					if (event.getLootingLevel() + 1 >= 1 && source != null) {
						res = (int) (entity.getRandom().nextFloat() * (event.getLootingLevel() + 1));
					}
				} catch (IllegalArgumentException e) {
					//System.out.println(e.getMessage());
				}
				if (source instanceof LivingEntity src) {
					if (src.hasEffect(ADDragonEffects.SEEKING_TALONS))
						res += (int) (SeekingTalonsAbility.seekingTalonsBonusLoot * (src.getEffect(ADDragonEffects.SEEKING_TALONS).getAmplifier() + 1));
				}
				drops.add(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ADItems.cursedMarrow, res)));
			}
			if (bones > 0) {
				//System.out.println("Dropping " + bones + " cursed bones.");
				drops.add(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ADItems.cursedMarrow, bones)));
			}
		}
	}

	@SubscribeEvent
	public static void livingDamage(LivingDamageEvent event) {
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
		if (event.getEntity() != null && !event.getEntity().level.isClientSide()) {
			LivingEntity entity = event.getEntity();
			DamageSource src = event.getSource();
			if (src.getEntity() instanceof LivingEntity en && !en.equals(entity) && en.hasEffect(ADDragonEffects.CONFOUNDED)) {
				ConfoundingBreathAbility.reflectDamage(en, en.getEffect(ADDragonEffects.CONFOUNDED).getAmplifier(), event.getAmount());
			} else if (src.getDirectEntity() instanceof LivingEntity en && !en.equals(entity) && en.hasEffect(ADDragonEffects.CONFOUNDED)) {
				ConfoundingBreathAbility.reflectDamage(en, en.getEffect(ADDragonEffects.CONFOUNDED).getAmplifier(), event.getAmount());
			}
			if (entity.hasEffect(ADDragonEffects.BUBBLE_SHIELD) && entity.level instanceof ServerLevel) {
				if (event.getEntity().getAbsorptionAmount() <= 0)
					event.getEntity().removeEffect(ADDragonEffects.BUBBLE_SHIELD);
			}

			if (event.getSource().isFire() && entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
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
				float critboost = (float) SeekingTalonsAbility.seekingTalonsCritBonus * (1 - (target.getHealth() / target.getMaxHealth()));
				critboost *= (entity.getEffect(ADDragonEffects.SEEKING_TALONS).getAmplifier() + 1);
				event.setDamageModifier((float) event.getDamageModifier() + critboost);
			}
		}
	}

	@SubscribeEvent
	public static void lootingEvent(LootingLevelEvent event) {
		if (event.getDamageSource().getEntity() instanceof LivingEntity source && source != null) {
			if (source.hasEffect(ADDragonEffects.SEEKING_TALONS)) {
				int bonus = (int) (SeekingTalonsAbility.seekingTalonsBonusLoot * (source.getEffect(ADDragonEffects.SEEKING_TALONS).getAmplifier() + 1));
				event.setLootingLevel(event.getLootingLevel() + bonus);
			}
		}
	}

	@SubscribeEvent
	public static void livingHurt(LivingHurtEvent event) {
		// Cave dragons are immune to their own blast dust damage.
		if (event.getSource().equals(ADDamageSources.BLAST_DUST) && DragonUtils.isDragonType(event.getEntity(), DragonTypes.CAVE)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void effectRemoved(MobEffectEvent.Remove event) {
		if (event.getEntity().level.isClientSide()) {
			LivingEntity entity = event.getEntity();
			if (!(DragonUtils.isDragon(entity) && DragonUtils.isDragonType(entity, DragonTypes.CAVE))) 
			{
				if (event.getEffect() == ADDragonEffects.BLAST_DUSTED) {
					entity.getLevel().playLocalSound(entity.position().x, entity.position().y + 0.5, entity.position().z, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.3F, 1.3F, true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void effectExpired(MobEffectEvent.Expired event) {
		if (!(DragonUtils.isDragon(event.getEntity()) && DragonUtils.isDragonType(event.getEntity(), DragonTypes.CAVE))) {
			if (event.getEffectInstance().getEffect() instanceof BlastDustedEffect effect)
				effect.detonate(event.getEntity(), event.getEffectInstance().getAmplifier());
		}
	}
}
