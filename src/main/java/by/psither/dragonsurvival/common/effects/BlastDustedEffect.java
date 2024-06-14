package by.psither.dragonsurvival.common.effects;


import by.dragonsurvivalteam.dragonsurvival.common.capability.EntityStateHandler;
import by.dragonsurvivalteam.dragonsurvival.network.client.ClientProxy;
import by.dragonsurvivalteam.dragonsurvival.registry.DSDamageTypes;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticleData;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.BlastBreathAbility;
import by.psither.dragonsurvival.registry.ADDamageSources;
import by.psither.dragonsurvival.registry.ADDamageTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class BlastDustedEffect extends MobEffect {
	public BlastDustedEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}

	public void detonate(LivingEntity entity, int amp) {
		if (entity.level().isClientSide())
			return;
		boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entity.level(), entity);
		EntityStateHandler cap = DragonUtils.getEntityHandler(entity);
		Player player = cap.lastAfflicted != -1 && entity.level().getEntity(cap.lastAfflicted) instanceof Player ? (Player)entity.level().getEntity(cap.lastAfflicted) : null;
		entity.level().explode(player != null ? player : entity, (player != null) ? ADDamageTypes.entityDamageSource(entity.level(), ADDamageTypes.BLAST_DUST, player) : ADDamageTypes.damageSource(entity.level(), ADDamageTypes.BLAST_DUST), null, entity.getX(), entity.getY() + (entity.getBbHeight() * 0.4), entity.getZ(), (float) BlastBreathAbility.getExplosionPower(amp), flag, (flag ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE));
		if (player != null)
			entity.setLastHurtMob(player);
	}

	public void detonate(Entity entity, int amp) {
		if (entity.level().isClientSide())
			return;
		boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entity.level(), entity);
		entity.level().explode(entity, ADDamageTypes.entityDamageSource(entity.level(), ADDamageTypes.BLAST_DUST, null), null, entity.getX(), entity.getY() + (entity.getBbHeight() * 0.4), entity.getZ(), (float) BlastBreathAbility.getExplosionPower(amp), flag, (flag ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE));
	}
	
	@Override
	public void applyEffectTick(LivingEntity entity, int amp) {
		if (entity.level().isClientSide()) {
			double timeLeft = (((float) entity.getEffect(this).getDuration() / 20.0F) / BlastBreathAbility.blastBreathEffectDuration);
			int color = BlastBreathAbility.getIntColorFromTimeLeft(timeLeft);
			RandomSource random = entity.getRandom();
			for (int i = Functions.secondsToTicks(BlastBreathAbility.blastBreathEffectDuration); i > entity.getEffect(this).getDuration(); i-= 20) {
				if (i % 60 == 20) {
					entity.level().addAlwaysVisibleParticle(ParticleTypes.FLAME, entity.getX() + (random.nextFloat() * 2) - 1.0f, entity.getY() + (random.nextFloat() * entity.getEyeHeight()), entity.getZ() + (random.nextFloat() * 2) - 1.0f, 0.0, 0.0, 0.0);
				} else {
					entity.level().addAlwaysVisibleParticle(new LargeBlastDustParticleData(24, false, color), entity.getX() + (random.nextFloat() * 2) - 1.0f, entity.getY() + (random.nextFloat() * entity.getEyeHeight()), entity.getZ() + (random.nextFloat() * 2) - 1.0f, 0.0, 0.0, 0.0);
				}
			}
		}
	}

	public static void showSmoke(LivingEntity entity, MobEffectInstance instance) {
		Player localPlayer = ClientProxy.getLocalPlayer();
		if (localPlayer != null) {
			if (instance.getEffect() == ADDragonEffects.BLAST_DUSTED) {
				double timeLeft = (((float) instance.getDuration() / 20.0F) / BlastBreathAbility.blastBreathEffectDuration);
				int color = BlastBreathAbility.getIntColorFromTimeLeft(timeLeft);
				RandomSource random = entity.getRandom();
				for (int i = Functions.secondsToTicks(BlastBreathAbility.blastBreathEffectDuration); i > instance.getDuration(); i-= 20) {
					if (i % 100 == 20)
						localPlayer.level().addAlwaysVisibleParticle(ParticleTypes.FLAME, entity.getX() + (random.nextFloat() * 2) - 1.0f, entity.getY() + (random.nextFloat() * entity.getEyeHeight()), entity.getZ() + (random.nextFloat() * 2) - 1.0f, 0.0, 0.0, 0.0);
					else
						localPlayer.level().addAlwaysVisibleParticle(new LargeBlastDustParticleData(24, false, color), entity.getX() + (random.nextFloat() * 2) - 1.0f, entity.getY() + (random.nextFloat() * entity.getEyeHeight()), entity.getZ() + (random.nextFloat() * 2) - 1.0f, 0.0, 0.0, 0.0);
				}
			}
		}
	}
}
