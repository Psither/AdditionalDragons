package by.psither.dragonsurvival.common.entity.projectiles;

import by.dragonsurvivalteam.dragonsurvival.client.particles.ForestDragon.LargePoisonParticleData;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.magic.abilities.ForestDragon.active.SpikeAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.DragonEffects;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.MistyBarbAbility;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MistyBarbProjectileEntity extends AbstractArrow {
	public static final EntityDataAccessor<Integer> ARROW_LEVEL = SynchedEntityData.defineId(MistyBarbProjectileEntity.class, EntityDataSerializers.INT);

	public MistyBarbProjectileEntity(Level p_i50172_2_){
		super(ADEntities.MISTY_BARB.get(), p_i50172_2_);
	}

	public MistyBarbProjectileEntity(EntityType<? extends AbstractArrow> type, Level worldIn){
		super(type, worldIn);
	}

	public MistyBarbProjectileEntity(EntityType<? extends AbstractArrow> type, LivingEntity entity, Level world){
		super(type, entity, world);
	}

	@Override
	public void doPostHurtEffects(LivingEntity entity) {
		if (!this.level().isClientSide()) {
			if (!(entity instanceof Player player && !DragonUtils.isDragonType(player, DragonTypes.FOREST)))
				entity.addEffect(new MobEffectInstance(DragonEffects.DRAIN, Functions.secondsToTicks(10), 0));
			makeCloud(entity.getPosition(0));
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (inGroundTime == 50 && !this.level().isClientSide())
			makeCloud(this.getPosition(0));
		else if (inGroundTime >= 200 && !this.level().isClientSide())
			this.remove(Entity.RemovalReason.DISCARDED);
	}

	private void makeCloud(Vec3 pos) {
		this.level().playLocalSound(pos.x, pos.y, pos.z, SoundEvents.CREEPER_HURT, SoundSource.PLAYERS, 0.25F, 0.4F, true);
		AreaEffectCloud cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, this.level());
		cloud.setWaitTime(0);
		cloud.setPos(pos.x, pos.y, pos.z);
		cloud.setPotion(new Potion(new MobEffectInstance(DragonEffects.DRAIN, /* Effect duration is normally divided by 4 */ Functions.secondsToTicks(5) * 4, 0)));
		cloud.setDuration(Functions.secondsToTicks(10));
		double rad = MistyBarbAbility.mistyBarbRadius;
		cloud.setRadius((float) rad * getShotLevel());
		cloud.setParticle(new LargePoisonParticleData(37, false));
		if (this.getOwner() instanceof LivingEntity le)
			cloud.setOwner(le);
		this.level().addFreshEntity(cloud);
	}

	@Override
	public double getBaseDamage(){
		return getShotLevel() * MistyBarbAbility.mistyBarbDamage;
	}

	@Override
	protected void onHitEntity(EntityHitResult p_213868_1_){

		Entity entity = p_213868_1_.getEntity();
		Entity entity1 = getOwner();
		DamageSource damagesource;
		if(entity1 == null){
			damagesource = level().damageSources().arrow(this, this);
		}else{
			damagesource = entity1.damageSources().arrow(this, entity1);
			if(entity1 instanceof LivingEntity){
				((LivingEntity)entity1).setLastHurtMob(entity);
			}
		}
		float damage = (float)getBaseDamage();

		if(TargetingFunctions.attackTargets(getOwner(), ent -> ent.hurt(damagesource, damage), entity)){
			if(entity instanceof LivingEntity livingentity){
				if(!level().isClientSide()){
					livingentity.setArrowCount(livingentity.getArrowCount() + 1);
				}

				if(!level().isClientSide() && entity1 instanceof LivingEntity){
					EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
					EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
				}

				doPostHurtEffects(livingentity);

				if(entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !isSilent()){
					((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
				}
			}

			if(getPierceLevel() <= 0){
				remove(RemovalReason.DISCARDED);
			}
		}else{
			setDeltaMovement(getDeltaMovement().scale(-0.1D));
			setYRot(getYRot() + 180.0F);
			yRotO += 180.0F;

			if(!level().isClientSide() && getDeltaMovement().lengthSqr() < 1.0E-7D){
				remove(RemovalReason.DISCARDED);
			}
		}
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(ARROW_LEVEL, 1);
	}

	public int getShotLevel() {
		return entityData.get(ARROW_LEVEL);
	}
	
	public void setShotLevel(int lvl) {
		entityData.set(ARROW_LEVEL, lvl);
	}

	@Override
	protected ItemStack getPickupItem() {
		return null;
	}

}
