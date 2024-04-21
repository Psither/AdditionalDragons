package by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.LargeFireParticleData;
import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.SmallFireParticleData;
import by.dragonsurvivalteam.dragonsurvival.client.sounds.SoundRegistry;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.DragonAbilities;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.BreathAbility;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticleData;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.client.sounds.BlastBreathSound;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.common.entity.CountdownAreaEffectCloud;
import by.psither.dragonsurvival.registry.ADDamageSources;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;

@RegisterDragonAbility
public class BlastBreathAbility extends BreathAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreath", comment = "Whether the blast breath ability should be enabled" )
	public static Boolean blastBreath = true;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathDamage", comment = "The amount of damage the blast breath ability deals on contact. This value is multiplied by the ability level." )
	public static Double blastBreathDamage = 0.0;

	@ConfigRange ( min = 0.0, max = 100.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathExplosionPower", comment = "The power of the explosion targets will suffer after their blast dust detonates.")
	public static Double blastBreathExplosionPower = 0.4;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathInitialMana", comment = "The mana cost for starting the blast breath ability" )
	public static Integer blastBreathInitialMana = 1;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathCooldown", comment = "The cooldown in seconds of the blast breath ability" )
	public static Double blastBreathCooldown = 10.0;

	@ConfigRange( min = 0.05, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathCasttime", comment = "The cast time in seconds of the blast breath ability" )
	public static Double blastBreathCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathOvertimeMana", comment = "The mana cost of sustaining the blast breath ability" )
	public static Integer blastBreathOvertimeMana = 1;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathManaTicks", comment = "How often in seconds, mana is consumed while using blast breath" )
	public static Double blastBreathManaTicks = 2.0;

	@ConfigRange ( min = 0.0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathEffectDuration", comment = "How long the target has to enter water or explode..." )
	public static Double blastBreathEffectDuration = 10.0;
	
	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "blast_breath"}, key = "blastBreathEffectSpeedupTicks", comment = "How much faster the target will explode if continuously exposed" )
	public static Integer blastBreathEffectSpeedupTicks = 1;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !blastBreath;
	}

	public static float getTimeLeftFraction(float duration) {
		return (float) ((blastBreathEffectDuration - Mth.clamp(blastBreathEffectDuration - duration, 0, blastBreathEffectDuration)) / blastBreathEffectDuration);
	}

	public static int getIntColorFromTimeLeft(double timeLeft) {
		timeLeft = Math.max(Math.min(timeLeft, 1.0), 0.0);
		int green = (int) ((timeLeft * 255.0) * 0.5);
		int red = (int) (255.0 - green);
		int blue = 0;
		double res = (red + (green * 255) + (blue * 65025));
		return (int) res;
	}

	public double getExplosionPower() {
		return blastBreathExplosionPower * getLevel();
	}
	
	public static double getExplosionPower(int amp) {
		return (double) blastBreathExplosionPower * (amp + 1);
	}

	@Override
	public boolean canHitEntity(LivingEntity entity) {
		return !(entity instanceof Player) || player.canHarmPlayer((Player)entity);
	}
	
	@Override
	public void onEntityHit(LivingEntity entity) {
		if (!entity.level.isClientSide() && !DragonUtils.isDragonType(entity, DragonTypes.CAVE)) {
			if (getDamage() > 0) {
				hurtTarget(entity);
			}
			onDamage(entity);
		}
	}

	@Override
	public void onDamage(LivingEntity entity) {
		if (!entity.level.isClientSide()) {
			if (!entity.hasEffect(ADDragonEffects.BLAST_DUSTED)) {
				DragonUtils.getEntityHandler(entity).lastAfflicted = player != null ? player.getId() : -1;
				entity.addEffect(new MobEffectInstance(ADDragonEffects.BLAST_DUSTED, Functions.secondsToTicks(blastBreathEffectDuration), getLevel() - 1));
			} else {
				for (int i = 0; i < blastBreathEffectSpeedupTicks; i++)
					entity.getEffect(ADDragonEffects.BLAST_DUSTED).tick(entity, null);
			}
		}
	}

	public void hurtTarget(LivingEntity entity) {
		TargetingFunctions.attackTargets(getPlayer(), e -> e.hurt(ADDamageSources.BLAST_DUST, getDamage()), entity);
	}

	public float getDamage() {
		return (float) (blastBreathDamage * getLevel());
	}

	public static float getDamage(int amp) {
		return (float) (blastBreathDamage * (amp + 1));
	}

	@Override
	public void onBlock(BlockPos pos, BlockState blockState, Direction direction) {
		if (!(player.level instanceof ServerLevel serverLevel)) {
			return;
		}

		if (blockState.getMaterial().isSolidBlocking()) {
			if (/* 10% */ player.getRandom().nextInt(100) < 10) {
				CountdownAreaEffectCloud entity = new CountdownAreaEffectCloud(ADEntities.COUNTDOWN_CLOUD, player.level);
				entity.setPos(pos.above().getX(), pos.above().getY(), pos.above().getZ());
				entity.setWaitTime(0);
				entity.setPotion(new Potion(new MobEffectInstance(ADDragonEffects.BLAST_DUSTED, /* Effect duration is normally divided by 4 */ Functions.secondsToTicks(blastBreathEffectDuration) * 4)));
				entity.setDuration(Functions.secondsToTicks(blastBreathEffectDuration) + player.getRandom().nextInt(20));
				entity.setRadius(1);
				entity.setParticle(new LargeBlastDustParticleData(16, false, getIntColorFromTimeLeft(1)));
				entity.setOwner(player);
				serverLevel.addFreshEntity(entity);
			}
		}
	}

	@OnlyIn( Dist.CLIENT ) // FIXME :: dist
	public void sound(){
		Vec3 pos = player.getEyePosition(1.0F);
		SimpleSoundInstance startingSound = new SimpleSoundInstance(
				ADSoundRegistry.blastBreathStart,
				SoundSource.PLAYERS,
				1.0F,1.0F,
				SoundInstance.createUnseededRandom(),
				pos.x,pos.y,pos.z
		);
		Minecraft.getInstance().getSoundManager().playDelayed(startingSound, 0);
		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(AdditionalDragonsMod.MODID, "blast_breath_loop"), SoundSource.PLAYERS);
		Minecraft.getInstance().getSoundManager().queueTickingSound(new BlastBreathSound(this));
	}
	
	@OnlyIn( Dist.CLIENT )
	public void stopSound(){
		if(SoundRegistry.stormBreathEnd != null){
			Vec3 pos = player.getEyePosition(1.0F);
			SimpleSoundInstance endSound = new SimpleSoundInstance(
					ADSoundRegistry.blastBreathEnd,
					SoundSource.PLAYERS,
					1.0F,1.0F,
					SoundInstance.createUnseededRandom(),
					pos.x, pos.y, pos.z
			);
			Minecraft.getInstance().getSoundManager().playDelayed(endSound, 0);
		}

		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(AdditionalDragonsMod.MODID, "blast_breath_loop"), SoundSource.PLAYERS);
	}
	
	@Override
	public void onChanneling(Player player, int castDuration){
		super.onChanneling(player, castDuration);

		if(player.level.isClientSide && castDuration <= 0){
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> (SafeRunnable)this::sound);
		}

		if(player.isInWaterRainOrBubble()) {
			if(player.level.isClientSide){
				if(player.tickCount % 10 == 0){
					player.playSound(SoundEvents.LAVA_EXTINGUISH, 0.25F, 1F);
				}

				for(int i = 0; i < 12; i++){
					double xSpeed = speed * 1f * xComp;
					double ySpeed = speed * 1f * yComp;
					double zSpeed = speed * 1f * zComp;
					player.level.addParticle(ParticleTypes.SMOKE, dx, dy, dz, xSpeed, ySpeed, zSpeed);
				}
			}
			return;
		} else { 
			if(player.level.isClientSide){
				RandomSource random = player.getRandom();
				for(int i = 0; i < 4; i++){
					double xSpeed = speed * 1f * xComp;
					double ySpeed = speed * 1f * yComp;
					double zSpeed = speed * 1f * zComp;
					float randAge = 1 - (random.nextFloat() * 0.2f);
					int color = getIntColorFromTimeLeft(randAge);
					player.level.addParticle(new LargeBlastDustParticleData(37, false, color), dx, dy, dz, xSpeed, ySpeed, zSpeed);
				}
	
				for(int i = 0; i < 6; i++){
					double xSpeed = speed * xComp + spread * 0.7 * (random.nextFloat() * 2 - 1) * Math.sqrt(1 - xComp * xComp);
					double ySpeed = speed * yComp + spread * 0.7 * (random.nextFloat() * 2 - 1) * Math.sqrt(1 - yComp * yComp);
					double zSpeed = speed * zComp + spread * 0.7 * (random.nextFloat() * 2 - 1) * Math.sqrt(1 - zComp * zComp);
					float randAge = 1 - (random.nextFloat() * 0.4f);
					int color = getIntColorFromTimeLeft(randAge);
					player.level.addParticle(new LargeBlastDustParticleData(37, false, color), dx, dy, dz, xSpeed, ySpeed, zSpeed);
				}
			}
			hitEntities();
			if(player.tickCount % 10 == 0){
				hitBlocks();
			}
		}
	}
	
	public static boolean isValidTarget(LivingEntity attacker, LivingEntity target){
		if(target == null || attacker == null){
			return false;
		}

		if(target.getLastHurtByMob() == attacker && target.getLastHurtByMobTimestamp() + Functions.secondsToTicks(1) < target.tickCount){
			return false;
		}

		return TargetingFunctions.isValidTarget(attacker, target) && !DragonUtils.isDragonType(target, DragonTypes.CAVE);
	}

	@Override
	public int getSkillChargeTime() {
		return Functions.secondsToTicks(blastBreathCasttime);
	}

	@Override
	public int getContinuousManaCostTime() {
		return Functions.secondsToTicks(blastBreathManaTicks);
	}

	@Override
	public int getInitManaCost() {
		return blastBreathInitialMana;
	}

	@Override
	public void castComplete(Player player) {}

	@Override
	public int getManaCost() {
		return blastBreathOvertimeMana;
	}

	@Override
	public Integer[] getRequiredLevels(){
		return new Integer[]{0, 10, 30, 50};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(blastBreathCooldown);
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.explosion", "+" + blastBreathExplosionPower));
		return list;
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.explosion", getExplosionPower()));
		components.add(Component.translatable("ds.skill.fuse", blastBreathEffectDuration));
		return components;
	}

	@Override
	public String getName() {
		return "blast_breath";
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.TECTONIC;
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/blast_breath_0.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/blast_breath_1.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/blast_breath_2.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/blast_breath_3.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/blast_breath_4.png")
		};
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

}
