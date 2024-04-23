package by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active;

import java.util.ArrayList;
import java.util.List;

import by.dragonsurvivalteam.dragonsurvival.client.particles.ForestDragon.LargePoisonParticleData;
import by.dragonsurvivalteam.dragonsurvival.client.sounds.SoundRegistry;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.BreathAbility;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.ADParticles;
import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticleData;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.client.sounds.ConfoundingBreathSound;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADDamageSources;
import by.psither.dragonsurvival.registry.ADDamageTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;

@RegisterDragonAbility
public class ConfoundingBreathAbility extends BreathAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreath", comment = "Whether the confounding breath ability should be enabled" )
	public static Boolean confoundingBreath = true;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathDamage", comment = "The amount of damage the confounding breath ability deals on contact. This value is multiplied by the ability level." )
	public static Double confoundingBreathDamage = 0.5;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathInitialMana", comment = "The mana cost for starting the confounding breath ability" )
	public static Integer confoundingBreathInitialMana = 1;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathCooldown", comment = "The cooldown in seconds of the confounding breath ability" )
	public static Double confoundingBreathCooldown = 10.0;

	@ConfigRange( min = 0.05, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathCasttime", comment = "The cast time in seconds of the confounding breath ability" )
	public static Double confoundingBreathCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathOvertimeMana", comment = "The mana cost of sustaining the confounding breath ability" )
	public static Integer confoundingBreathOvertimeMana = 1;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathManaTicks", comment = "How often in seconds, mana is consumed while using confounding breath" )
	public static Double confoundingBreathManaTicks = 2.0;

	@ConfigRange ( min = 0.0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathEffectDuration", comment = "How long the confusion effect lasts for once applied" )
	public static Double confoundingBreathEffectDuration = 10.0;
	
	@ConfigRange ( min = 0.0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathEffectDuration", comment = "How far away the afflicted will seek new targets" )
	public static Double confoundingBreathEffectRange = 10.0;
	
	@ConfigRange ( min = 0.0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathEffectStrength", comment = "What portion of damage the afflicted will deal back to themselves when attacking" )
	public static Double confoundingBreathEffectStrength = 0.2;

	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "confounding_breath"}, key = "confoundingBreathAffectsPlayers", comment = "Whether players suffer disorienting effects from confounding breath" )
	public static Boolean confoundingBreathAffectsPlayers = true;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !confoundingBreath;
	}

	public static void changeTargetToRandomMob(Mob mob) {
		if (!mob.level().isClientSide()) {
			List<LivingEntity> list1 = mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(getEffectRange()));
			// Remove all forest dragons from potential targets
			// Also remove self as target
			list1 = list1.stream().filter(e -> {
				if (e instanceof Player p) { return (!DragonUtils.isDragonType(p, DragonTypes.FOREST)); }
				else if (e == mob) return false;
				return true;
			}).toList();
			if (list1.size() <= 0) {
				mob.setTarget(null); // No valid targets to swap to.
				return; 
			}
			int targetIndex = mob.getRandom().nextInt(list1.size());
			mob.setTarget(list1.get(targetIndex));
		}
	}

	public static void confoundPlayer(Player player, int amp) {
		if (player.level().isClientSide()) {
			if (DragonUtils.isDragonType(player, DragonTypes.FOREST) || !confoundingBreathAffectsPlayers) return;

			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
			if (amp > 1) {
				player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100));
			} else if (amp > 0) {
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100));
			}
		}
	}

	public static double getEffectRange() {
		return confoundingBreathEffectRange;
	}

	@Override
	public void castComplete(Player player) { }

	@Override
	public int getInitManaCost() {
		return confoundingBreathInitialMana;
	}

	@Override
	public int getManaCost() {
		return confoundingBreathOvertimeMana;
	}

	@Override
	public int getContinuousManaCostTime() {
		return Functions.secondsToTicks(confoundingBreathManaTicks);
	}

	@Override
	public int getSkillChargeTime() {
		return Functions.secondsToTicks(confoundingBreathCasttime);
	}

	@Override
	public void onCharging(Player player, int currentChargeTime) { }

	@OnlyIn( Dist.CLIENT ) // FIXME :: dist
	public void sound(){
		Vec3 pos = player.getEyePosition(1.0F);
		SimpleSoundInstance startingSound = new SimpleSoundInstance(
				ADSoundRegistry.confoundingBreathStart,
				SoundSource.PLAYERS,
				1.0F,1.0F,
				SoundInstance.createUnseededRandom(),
				pos.x,pos.y,pos.z
		);
		Minecraft.getInstance().getSoundManager().playDelayed(startingSound, 0);
		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(AdditionalDragonsMod.MODID, "confounding_breath_loop"), SoundSource.PLAYERS);
		Minecraft.getInstance().getSoundManager().queueTickingSound(new ConfoundingBreathSound(this));
	}
	
	@OnlyIn( Dist.CLIENT )
	public void stopSound(){
		if(ADSoundRegistry.confoundingBreathEnd != null){
			Vec3 pos = player.getEyePosition(1.0F);
			SimpleSoundInstance endSound = new SimpleSoundInstance(
					ADSoundRegistry.confoundingBreathEnd,
					SoundSource.PLAYERS,
					1.0F,1.0F,
					SoundInstance.createUnseededRandom(),
					pos.x, pos.y, pos.z
			);
			Minecraft.getInstance().getSoundManager().playDelayed(endSound, 0);
		}

		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(AdditionalDragonsMod.MODID, "confounding_breath_loop"), SoundSource.PLAYERS);
	}

	@Override
	public void onChanneling(Player player, int castDuration) {
		super.onChanneling(player, castDuration);

		if(player.level().isClientSide && castDuration <= 0){
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> (SafeRunnable)this::sound);
		}

		if(player.level().isClientSide){
			RandomSource random = player.getRandom();
			for(int i = 0; i < 4; i++){
				double xSpeed = speed * 1f * xComp;
				double ySpeed = speed * 1f * yComp;
				double zSpeed = speed * 1f * zComp;
				player.level().addParticle(new LargePoisonParticleData(37, true), dx, dy, dz, xSpeed, ySpeed, zSpeed);
			}

			for(int i = 0; i < 6; i++){
				double xSpeed = speed * xComp + spread * 0.7 * (random.nextFloat() * 2 - 1) * Math.sqrt(1 - xComp * xComp);
				double ySpeed = speed * yComp + spread * 0.7 * (random.nextFloat() * 2 - 1) * Math.sqrt(1 - yComp * yComp);
				double zSpeed = speed * zComp + spread * 0.7 * (random.nextFloat() * 2 - 1) * Math.sqrt(1 - zComp * zComp);
				player.level().addParticle(new SmallConfoundParticleData(37, false), dx, dy, dz, xSpeed, ySpeed, zSpeed);
			}
		}
		hitEntities();
		if(player.tickCount % 10 == 0){
			hitBlocks();
		}
	}

	@Override
	public Integer[] getRequiredLevels(){
		return new Integer[]{0, 10, 25, 40};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(confoundingBreathCooldown);
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.DEEPWOODS;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public String getName() {
		return "confounding_breath";
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/confounding_breath_0.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/confounding_breath_1.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/confounding_breath_2.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/confounding_breath_3.png"),
				  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/confounding_breath_4.png")
		};
	}

	@Override
	public boolean canHitEntity(LivingEntity entity) {
		return true;
	}

	@Override
	public float getDamage() {
		return (float) (confoundingBreathDamage * getLevel());
	}
	
	public int getEffectDuration() {
		return Functions.secondsToTicks(confoundingBreathEffectDuration);
	}

	@Override
	public void onBlock(BlockPos pos, BlockState blockState, Direction direction) {
		if (!(player.level() instanceof ServerLevel serverLevel)) {
			return;
		}

		if (blockState.isSolid()) {
			if (/* 30% */ player.getRandom().nextInt(100) < 30) {
				AreaEffectCloud entity = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, player.level());
				entity.setWaitTime(0);
				entity.setPos(pos.above().getX(), pos.above().getY(), pos.above().getZ());
				entity.setPotion(new Potion(new MobEffectInstance(ADDragonEffects.CONFOUNDED, /* Effect duration is normally divided by 4 */ Functions.secondsToTicks(confoundingBreathEffectDuration) * 4, getLevel() - 1)));
				entity.setDuration(Functions.secondsToTicks(2));
				entity.setRadius(1);
				entity.setParticle(new SmallConfoundParticleData(37, false));
				entity.setOwner(player);
				serverLevel.addFreshEntity(entity);
			}
		}
	}

	@Override
	public void onEntityHit(LivingEntity entity) {
		if (!entity.level().isClientSide()) {
			if (!DragonUtils.isDragonType(entity, DragonTypes.FOREST)) {
				if (getDamage() > 0) {
					hurtTarget(entity);
				}
				onDamage(entity);
			}
		}
	}

	public void hurtTarget(LivingEntity entity) {
		TargetingFunctions.attackTargets(getPlayer(), e -> e.hurt(ADDamageTypes.entityDamageSource(player.level(), ADDamageTypes.MIRROR_CURSE, player), getDamage()), entity);
	}

	public static boolean isValidTarget(LivingEntity attacker, LivingEntity target){
		if(target == null || attacker == null){
			return false;
		}

		if(target.getLastHurtByMob() == attacker && target.getLastHurtByMobTimestamp() + Functions.secondsToTicks(1) < target.tickCount){
			return false;
		}

		return TargetingFunctions.isValidTarget(attacker, target) && !DragonUtils.isDragonType(target, DragonTypes.FOREST);
	}

	@Override
	public void onDamage(LivingEntity entity) {
		if (!entity.level().isClientSide()) {
			entity.addEffect(new MobEffectInstance(ADDragonEffects.CONFOUNDED, getEffectDuration(), getLevel() - 1));
			if (entity instanceof Mob mob)
				changeTargetToRandomMob(mob);
			else if (entity instanceof Player player) {
				confoundPlayer(player, getLevel());
			}
		}
	}

	public static void reflectDamage(LivingEntity en, int amp, float dam) {
		if (en.level().isClientSide() || (en instanceof Player player && DragonUtils.isDragonType(player, DragonTypes.FOREST))) return;
		//System.out.println("Returning " + dam * (amp + 1) * confoundingBreathEffectStrength + " damage to " + en);
		en.hurt(ADDamageTypes.entityDamageSource(en.level(), ADDamageTypes.BLAST_DUST, en), (float) (dam * (amp + 1) * confoundingBreathEffectStrength));
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.curse", confoundingBreathEffectStrength * getLevel()));
		components.add(Component.translatable("ds.skill.duration.seconds", confoundingBreathEffectDuration));
		return components;
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.curse", "+" + confoundingBreathEffectStrength));
		return list;
	}

	public static void produceQuestionMarks(LivingEntity entity) {
		if (entity.level().isClientSide()) {
			if (DragonUtils.isDragonType(entity, DragonTypes.FOREST)) return;
			entity.level().addAlwaysVisibleParticle(ADParticles.questionMarkParticle, entity.getX(), entity.getY() + (entity.getBbHeight() * 1.1), entity.getZ(), 0.0, 0.0, 0.0);
		}
	}
}
