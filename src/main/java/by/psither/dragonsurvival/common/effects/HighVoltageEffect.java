package by.psither.dragonsurvival.common.effects;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.capability.EntityStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.effects.ChargedEffect;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.magic.EffectHandler;
import by.dragonsurvivalteam.dragonsurvival.common.particles.LargeLightningParticleOption;
import by.dragonsurvivalteam.dragonsurvival.common.particles.SmallLightningParticleOption;
import by.dragonsurvivalteam.dragonsurvival.registry.DSDamageTypes;
import by.dragonsurvivalteam.dragonsurvival.registry.DSEffects;
import by.dragonsurvivalteam.dragonsurvival.registry.attachments.DSDataAttachments;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.tags.DSEntityTypeTags;
import by.dragonsurvivalteam.dragonsurvival.util.AdditionalEffectData;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import by.psither.dragonsurvival.registry.ADEffects;
import by.psither.dragonsurvival.registry.ADSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class HighVoltageEffect extends ChargedEffect {
    public HighVoltageEffect(MobEffectCategory type, int color, boolean incurable) {
        super(type, color, incurable);
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity().hasEffect(ADEffects.HIGH_VOLTAGE) && event.getSource().getEntity() != null && !event.getEntity().equals(event.getSource().getEntity())) {
            zapTarget(event.getEntity(), event.getSource().getEntity(), event.getEntity().getEffect(ADEffects.HIGH_VOLTAGE).getAmplifier());
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(new DamageSource(DSDamageTypes.get(entity.level(), DSDamageTypes.ELECTRIC)), damage);
        if (!DragonStateProvider.isDragon(entity)) {
            ParticleOptions particle = new SmallLightningParticleOption(37.0F, false);

            for(int i = 0; i < 4; ++i) {
                EffectHandler.renderEffectParticle(entity, particle);
            }
        }

        chargedEffectChain(entity, damage);
        return true;
    }

    public static void chargedEffectChain(LivingEntity source, float damage) {
        List<LivingEntity> secondaryTargets = source.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), source, source.getBoundingBox().inflate((double)spreadRadius));
        secondaryTargets.sort((c1, c2) -> Boolean.compare(c1.hasEffect(DSEffects.CHARGED), c2.hasEffect(DSEffects.CHARGED)));
        if (secondaryTargets.size() > maxChainTargets) {
            secondaryTargets = secondaryTargets.subList(0, maxChainTargets);
        }

        Iterator<LivingEntity> var3 = secondaryTargets.iterator();

        while(true) {
            LivingEntity target;
            Entity effectApplier;
            EntityStateHandler targetData;
            do {
                do {
                    do {
                        if (!var3.hasNext()) {
                            return;
                        }

                        target = (LivingEntity)var3.next();
                        effectApplier = null;
                        Level var7 = source.level();
                        if (var7 instanceof ServerLevel serverLevel) {
                            AdditionalEffectData data = ((AdditionalEffectData)source.getEffect(ADEffects.HIGH_VOLTAGE));
                            if (data != null) {
                                effectApplier = data.dragonSurvival$getApplier(serverLevel);
                            }
                        }

                        target.hurt(new DamageSource(DSDamageTypes.get(target.level(), DSDamageTypes.ELECTRIC), effectApplier), damage);
                        drawParticleLine(source, target);
                        if (target.level().isClientSide()) {
                            return;
                        }
                    } while(target == source);
                } while(target.getType().is(DSEntityTypeTags.CHARGED_SPREAD_BLACKLIST));

                EntityStateHandler sourceData = source.getData(DSDataAttachments.ENTITY_HANDLER);
                targetData = target.getData(DSDataAttachments.ENTITY_HANDLER);
                targetData.chainCount = sourceData.chainCount + 1;
            } while(targetData.chainCount >= maxChain && maxChain != -1);

            if (Functions.chance(target.getRandom(), 40)) {
                target.addEffect(new MobEffectInstance(DSEffects.CHARGED, Functions.secondsToTicks(10.0), 0, false, false), effectApplier);
            }
        }
    }

    public static void zapTarget(LivingEntity source, Entity target, int amp) {
        ClipContext cc = new ClipContext(source.getPosition(0), target.getPosition(0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target);
        if (target.level().clip(cc).getType() == HitResult.Type.BLOCK) {
            return;
        }
        float damage = (float) (amp + 1) * ChargedEffect.damage;
        if (source.level() instanceof ServerLevel) {
            if (target instanceof LivingEntity livingTarget) {
                if (TargetingFunctions.attackTargets(source, entity -> entity.hurt(source.damageSources().lightningBolt(), damage), target)) {
                    livingTarget.setDeltaMovement(livingTarget.getDeltaMovement().multiply(0.25, 1, 0.25));
                }
            }
        } else {
            // Creates a trail of particles between the entity and target(s)
            int steps = 20;
            for (int i = 0; i < steps; i++) {
                Vec3 distV = new Vec3(target.getX() - source.getX(), target.getY() - source.getY(), target.getZ() - source.getZ());
                double distFrac = (steps - (double)(i)) / steps;
                // the current entity coordinate + ((the distance between it and the target) * (the fraction of the total))
                double stepX = source.getX() + (distV.x * distFrac);
                double stepY = source.getY() + (source.getEyeHeight() / 2) + (distV.y * distFrac);
                double stepZ = source.getZ() + (distV.z * distFrac);
                source.level().addParticle(new LargeLightningParticleOption(16F, false), stepX, stepY, stepZ, 0.0, 0.0, 0.0);
            }
        }
        source.level().playLocalSound(target.position().x, target.position().y + 0.5, target.position().z, ADSounds.bugZapper, SoundSource.PLAYERS, 4F, 1F, false);
    }
}
