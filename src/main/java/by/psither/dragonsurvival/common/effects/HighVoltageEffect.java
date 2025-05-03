package by.psither.dragonsurvival.common.effects;

import by.dragonsurvivalteam.dragonsurvival.common.effects.ChargedEffect;
import by.dragonsurvivalteam.dragonsurvival.common.particles.LargeLightningParticleOption;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import by.psither.dragonsurvival.registry.ADEffects;
import by.psither.dragonsurvival.registry.ADSounds;
import by.psither.dragonsurvival.utils.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.joml.Vector3f;

@EventBusSubscriber
public class HighVoltageEffect extends ChargedEffect {
    public HighVoltageEffect(MobEffectCategory type, int color, boolean incurable) {
        super(type, color, incurable);
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity().hasEffect(ADEffects.HIGH_VOLTAGE)) {
            zapTarget(event.getEntity(), event.getSource().getEntity(), event.getEntity().getEffect(ADEffects.HIGH_VOLTAGE).getAmplifier());
        }
    }

    public static void zapTarget(LivingEntity source, Entity target, int amp) {
        ClipContext cc = new ClipContext(source.getPosition(0), target.getPosition(0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target);
        if (target.level().clip(cc).getType() == HitResult.Type.BLOCK) {
            return;
        }
        float damage = (float) (amp + 1) * ChargedEffect.damage;
        if(source.level() instanceof ClientLevel clientLevel){
            // Creates a trail of particles between the entity and target(s)
            int steps = 20;
            for (int i = 0; i < steps; i++) {
                Vec3 distV = new Vec3(target.getX() - source.getX(), target.getY() - source.getY(), target.getZ() - source.getZ());
                double distFrac = (steps - (double)(i)) / steps;
                // the current entity coordinate + ((the distance between it and the target) * (the fraction of the total))
                double stepX = source.getX() + (distV.x * distFrac);
                double stepY = source.getY() + (source.getEyeHeight() / 2) + (distV.y * distFrac);
                double stepZ = source.getZ() + (distV.z * distFrac);
                clientLevel.addParticle(new LargeLightningParticleOption(16F, false), stepX, stepY, stepZ, 0.0, 0.0, 0.0);
            }
        } else {
            if (target instanceof LivingEntity livingtarget) {
                if (TargetingFunctions.attackTargets(source, entity -> entity.hurt(source.damageSources().lightningBolt(), damage), target)) {
                    livingtarget.setDeltaMovement(livingtarget.getDeltaMovement().multiply(0.25, 1, 0.25));
                }
            }
        }
        source.level().playLocalSound(target.position().x, target.position().y + 0.5, target.position().z, ADSounds.bugZapper, SoundSource.PLAYERS, 4F, 1F, false);
    }

    public static void producePassiveParticles(LivingEntity entity, int amp) {
        if (entity.level() instanceof ClientLevel clientLevel) {
            // Create particles sometimes because it's pretty
            if (/* 40% 4/sec */ entity.getRandom().nextInt(100) < 40) {
                for (int i = 0; i < (5 * (amp + 1)); i++) {
                    Vector3f loc = MathUtils.randomPointInSphere((float) 3, entity.getRandom());
                    float randX = (entity.getRandom().nextFloat() * 3f) - 1.5f;
                    float randY = (entity.getRandom().nextFloat()) - 0.5f;
                    float randZ = (entity.getRandom().nextFloat() * 3f) - 1.5f;
                    clientLevel.addParticle(new LargeLightningParticleOption(15, false), entity.getX() + loc.x(), entity.getY() + entity.getEyeHeight() + loc.y(), entity.getZ() + loc.z(), randX * 0.1, randY * 0.1, randZ * 0.1);
                }
            }
        }
    }
}
