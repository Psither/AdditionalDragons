package by.psither.dragonsurvival.common.entity.projectiles;

import by.dragonsurvivalteam.dragonsurvival.registry.DragonEffects;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.FaultLineAbility;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class FaultLineProjectileEntity extends AbstractArrow {
	public static final EntityDataAccessor<Integer> ARROW_LEVEL = SynchedEntityData.defineId(FaultLineProjectileEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<ItemStack> AMMO_TYPE = SynchedEntityData.defineId(FaultLineProjectileEntity.class, EntityDataSerializers.ITEM_STACK);

	public FaultLineProjectileEntity(Level p_i50172_2_){
		super(ADEntities.FAULT_LINE.get(), p_i50172_2_);
	}

	public FaultLineProjectileEntity(EntityType<? extends AbstractArrow> type, Level worldIn){
		super(type, worldIn);
	}

	public FaultLineProjectileEntity(EntityType<? extends AbstractArrow> type, LivingEntity entity, Level world){
		super(type, entity, world);
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(ARROW_LEVEL, 1);
		entityData.define(AMMO_TYPE, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		super.tick();
		if (inGroundTime > 60 && this.pickup == Pickup.DISALLOWED && !this.isRemoved()) {
			if (!this.level().isClientSide())
				this.remove(Entity.RemovalReason.DISCARDED);
			//else if (inGroundTime == 60) {
			//	Player localPlayer = ClientProxy.getLocalPlayer();
			//	localplayer.level().playSound(localPlayer, this.getX(), this.getY(), this.getZ(), this.getHitGroundSoundEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);
			//}
		}
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
	public void doPostHurtEffects(LivingEntity entity) {
		if (this.isOnFire()) {
			entity.setSecondsOnFire(5);
		}
		if (getAmmoType().getItem().equals(Items.MUD)) {
			entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Functions.secondsToTicks(5)));
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Functions.secondsToTicks(5)));
		} else if (getAmmoType().getItem().equals(Items.MAGMA_BLOCK)) {
			entity.addEffect(new MobEffectInstance(DragonEffects.BURN, Functions.secondsToTicks(5)));
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult bhr) {
		SoundEvent sound = this.getHitGroundSoundEvent();
		super.onHitBlock(bhr);
		this.setSoundEvent(sound);
		if (this.getAmmoType().getItem().equals(Items.MAGMA_BLOCK)) {
			BlockPos blockpos = new BlockPos((int) getPosition(1).x, (int) getPosition(1).y, (int) getPosition(1).z);
			//BlockPos blockpos = new BlockPos(getX(), getY(), getZ());
			if (!this.level().isClientSide()) {
				igniteBlock(blockpos);
				igniteBlock(blockpos.below());
			}
		}
	}

	public void igniteBlock(BlockPos blockpos) {
		BlockState blockstate = level().getBlockState(blockpos);
		if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate) && !CandleCakeBlock.canLight(blockstate)) {
			if (BaseFireBlock.canBePlacedAt(level(), blockpos, getDirection().getOpposite()) || BaseFireBlock.canBePlacedAt(level(), blockpos, Direction.UP)) {
				//level.playSound(null, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
				BlockState blockstate1 = BaseFireBlock.getState(level(), blockpos);
				level().setBlock(blockpos, blockstate1, 11);
				level().gameEvent(null, GameEvent.BLOCK_PLACE, blockpos);
			}
		} else {
			//level.playSound(null, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
			level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
			level().gameEvent(null, GameEvent.BLOCK_CHANGE, blockpos);
		}
	}

	@Override
	public double getBaseDamage(){
		return getArrowLevel() * FaultLineAbility.faultLineDamage;
	}

	public int getArrowLevel(){
		return entityData.get(ARROW_LEVEL);
	}

	public void setArrowLevel(int arrowLevel){
		entityData.set(ARROW_LEVEL, arrowLevel);
	}

	@Override
	public boolean isCritArrow(){
		return false;
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent(){
		return SoundEvents.STONE_HIT;
	}

	@Override
	protected ItemStack getPickupItem(){
		ItemStack ammo = getAmmoType();
		return ammo == null ? ItemStack.EMPTY : ammo;
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("level", getArrowLevel());
		tag.putString("ammo", getAmmoTypeName(getAmmoType()));
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setArrowLevel(tag.getInt("level"));
		setAmmoType(new ItemStack(FaultLineAbility.faultLineAmmoTypes.get(tag.getString("ammo"))));
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(){
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public void setAmmoType(ItemStack ammo) {
		entityData.set(AMMO_TYPE, ammo);
	}
	
	public static ItemStack makeItemStackFromName(String name) {
		if (!FaultLineAbility.faultLineAmmoTypes.containsKey(name))
			return ItemStack.EMPTY;
		return new ItemStack(FaultLineAbility.faultLineAmmoTypes.get(name));
	}

	public static String getAmmoTypeName(ItemStack is) {
		if (!FaultLineAbility.faultLineAmmoTypes.containsValue(is.getItem()))
			return "stone";
		String name = is.getItem().toString();
		return name;
	}

	public ItemStack getAmmoType() {
		return entityData.get(AMMO_TYPE);
	}

	public void setShotLevel(int lvl) {
		entityData.set(ARROW_LEVEL, lvl);
	}
}
