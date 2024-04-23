package by.psither.dragonsurvival.common.entity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import org.joml.Vector3f;

import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticleData;
import by.psither.dragonsurvival.common.effects.BlastDustedEffect;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.BlastBreathAbility;
import by.psither.dragonsurvival.utils.MathUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public class CountdownAreaEffectCloud extends AreaEffectCloud {
	private static final int TIME_BETWEEN_APPLICATIONS = 5;
	private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(CountdownAreaEffectCloud.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(CountdownAreaEffectCloud.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(CountdownAreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(CountdownAreaEffectCloud.class, EntityDataSerializers.PARTICLE);
	private static final float MAX_RADIUS = 32.0F;
	private Potion potion = Potions.EMPTY;
	private final List<MobEffectInstance> effects = Lists.newArrayList();
	private final Map<Entity, Integer> victims = Maps.newHashMap();
	private int duration = 600;
	private int waitTime = 20;
	private int reapplicationDelay = 20;
	private boolean fixedColor;
	private int durationOnUse;
	private float radiusOnUse;
	private float radiusPerTick;
	private Level level;
	@Nullable
	private LivingEntity owner;
	@Nullable
	private UUID ownerUUID;

	public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType((p_103941_) -> {
		return Component.translatable("particle.notFound", p_103941_);
	});

	public CountdownAreaEffectCloud(EntityType<? extends CountdownAreaEffectCloud> cloud, Level level) {
		super(cloud, level);
		this.level = level;
	}
	
	public void setPotion(Potion potion) {
		this.potion = potion;
	}
	
	public void setDuration(int dur) {
		this.duration = dur;
	}

	@Override
	public void tick() {
		if (this.isInWaterRainOrBubble()) {
			this.remove(RemovalReason.DISCARDED);
			return;
		}
		
		if (this.tickCount % 5 == 0) {
			this.setParticle(new LargeBlastDustParticleData(16, false, BlastBreathAbility.getIntColorFromTimeLeft(Math.min((1f - ((float) this.tickCount) / ((float) this.duration)), 1.0f))));
		}

		boolean flag = this.isWaiting();
		float f = this.getRadius();
		if (this.level.isClientSide) {
			if (flag && this.random.nextBoolean()) {
				return;
			}

			ParticleOptions particleoptions = this.getParticle();
			int i;
			//float f1;
			if (flag) {
				i = 2;
				//f1 = 0.2F;
			} else {
				i = Mth.ceil((float)Math.PI * f * f);
				//f1 = f;
			}

			for(int j = 0; j < i; ++j) {
				// Make it a sphere!
				Vector3f loc = MathUtils.randomPointInSphere(this.getRadius(), this.random);
				//float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
				//float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
				//double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
				//double d2 = this.getY();
				//double d4 = this.getZ() + (double)(Mth.sin(f2) * f3);
				double d5;
				double d6;
				double d7;
				if (particleoptions.getType() != ParticleTypes.ENTITY_EFFECT) {
					if (flag) {
						d5 = 0.0D;
						d6 = 0.0D;
						d7 = 0.0D;
					} else {
						d5 = (0.5D - this.random.nextDouble()) * 0.15D;
						d6 = (double)0.01F;
						d7 = (0.5D - this.random.nextDouble()) * 0.15D;
					}
				} else {
					int k = flag && this.random.nextBoolean() ? 16777215 : this.getColor();
					d5 = (double)((float)(k >> 16 & 255) / 255.0F);
					d6 = (double)((float)(k >> 8 & 255) / 255.0F);
					d7 = (double)((float)(k & 255) / 255.0F);
				}

				this.level().addAlwaysVisibleParticle(particleoptions, loc.x() + this.getX(), loc.y() + this.getY(), loc.z() + this.getZ(), d5, d6, d7);
			}
		} else {
			if (this.tickCount >= this.waitTime + this.duration) {
				for (MobEffectInstance instance : this.potion.getEffects()) {
					if (instance.getEffect() instanceof BlastDustedEffect bde) {
						bde.detonate(this, instance.getAmplifier());
					}
				}
				this.discard();
				return;
			}

			boolean flag1 = this.tickCount < this.waitTime;
			if (flag != flag1) {
				this.setWaiting(flag1);
			}

			if (flag1) {
				return;
			}

			if (this.radiusPerTick != 0.0F) {
				f += this.radiusPerTick;
				if (f < 0.5F) {
					this.discard();
					return;
				}

				this.setRadius(f);
			}

			if (this.tickCount % 5 == 0) {
				this.victims.entrySet().removeIf((p_146784_) -> {
					return this.tickCount >= p_146784_.getValue();
				});
				List<MobEffectInstance> list = Lists.newArrayList();

				for(MobEffectInstance mobeffectinstance : this.potion.getEffects()) {
					list.add(new MobEffectInstance(mobeffectinstance.getEffect(), mobeffectinstance.getDuration() / 4, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()));
				}

				list.addAll(this.effects);
				if (list.isEmpty()) {
					this.victims.clear();
				} else {
					List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
					if (!list1.isEmpty()) {
						for(LivingEntity livingentity : list1) {
							if (!this.victims.containsKey(livingentity) && livingentity.isAffectedByPotions()) {
								double d8 = livingentity.getX() - this.getX();
								double d1 = livingentity.getZ() - this.getZ();
								double d3 = d8 * d8 + d1 * d1;
								if (d3 <= (double)(f * f)) {
									this.victims.put(livingentity, this.tickCount + this.reapplicationDelay);

									for(MobEffectInstance mobeffectinstance1 : list) {
										if (mobeffectinstance1.getEffect().isInstantenous()) {
											mobeffectinstance1.getEffect().applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectinstance1.getAmplifier(), 0.5D);
										} else {
											if (!livingentity.hasEffect(mobeffectinstance1.getEffect())) {
												livingentity.addEffect(new MobEffectInstance(mobeffectinstance1), this);
											} else {
												mobeffectinstance1.tick(livingentity, null);
											}
										}
									}

									if (this.radiusOnUse != 0.0F) {
										f += this.radiusOnUse;
										if (f < 0.5F) {
											this.discard();
											return;
										}

										this.setRadius(f);
									}

									if (this.durationOnUse != 0) {
										this.duration += this.durationOnUse;
										if (this.duration <= 0) {
											this.discard();
											return;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
