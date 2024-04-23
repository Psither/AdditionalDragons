package by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.sounds.ADSoundRegistry;
import by.psither.dragonsurvival.common.blocks.GlowSlimeBlock;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joml.Vector3f;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.client.particles.SeaDragon.LargeLightningParticleData;
import by.dragonsurvivalteam.dragonsurvival.common.capability.EntityStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.common.entity.projectiles.BallLightningEntity;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.magic.ManaHandler;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.abilities.SeaDragon.active.StormBreathAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.AbilityAnimation;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ChargeCastAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.DragonEffects;
import by.psither.dragonsurvival.registry.ADBlocks;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import by.psither.dragonsurvival.utils.MathUtils;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.ResourceHelper;
import by.dragonsurvivalteam.dragonsurvival.util.TargetingFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@RegisterDragonAbility
public class HighVoltageAbility extends ChargeCastAbility {
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltage", comment = "Whether the high voltage ability should be enabled" )
	public static Boolean highVoltage = true;

	@ConfigRange( min = 1.0, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltageDuration", comment = "The duration in seconds of the high voltage effect given when the ability is used" )
	public static Double highVoltageDuration = 30.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltageCooldown", comment = "The cooldown in seconds of the high voltage ability" )
	public static Double highVoltageCooldown = 50.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltageCasttime", comment = "The cast time in seconds of the high voltage ability" )
	public static Double highVoltageCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltageManaCost", comment = "The mana cost for using the high voltage ability" )
	public static Integer highVoltageManaCost = 1;

	@ConfigRange ( min = 0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltageActiveRange", comment = "How far away the attacker can be and still trigger the effect" )
	public static Double highVoltageActiveRange = 10.0;
	
	@ConfigRange ( min = 0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltagePassiveRange", comment = "How far away the passive shock aura extends" )
	public static Double highVoltagePassiveRange = 2.0;
	
	@ConfigRange ( min = 0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "high_voltage"}, key = "highVoltageDamage", comment = "How much damage will be done to the target on activation" )
	public static Double highVoltageDamage = 2.0;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !highVoltage;
	}

	public static double getActiveRange(int amp) {
		return highVoltageActiveRange * (amp + 1);
	}

	public static double getPassiveRange(int amp) {
		return highVoltagePassiveRange * (amp + 1);
	}
	
	public static double getDamage(int amp) {
		return highVoltageDamage * (amp + 1);
	}
	
	public double getActiveRange() {
		return highVoltageActiveRange * getLevel();
	}
	
	public double getPassiveRange() {
		return highVoltagePassiveRange * getLevel();
	}
	
	public double getDamage() {
		return highVoltageDamage * getLevel();
	}
	
	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(highVoltageCasttime);
	}

	@Override
	public int getSortOrder(){
		return 4;
	}

	@Override
	public void onCasting(Player player, int currentCastTime) {}

	@Override
	public void castingComplete(Player player){
		player.addEffect(new MobEffectInstance(ADDragonEffects.HIGH_VOLTAGE, Functions.secondsToTicks(getDuration()), getLevel() - 1));
		player.level().playLocalSound(player.position().x, player.position().y + 0.5, player.position().z, SoundEvents.UI_TOAST_IN, SoundSource.PLAYERS, 5F, 0.1F, true);
	}

	@Override
	public int getManaCost() {
		return 1;
	}

	public static void attackNearbyTargets(LivingEntity entity, int amp) {
		int range = (int) getPassiveRange(amp);
		List<Entity> entities = entity.level().getEntities(null, new AABB(entity.position().x - range, entity.position().y - range, entity.position().z - range, entity.position().x + range, entity.position().y + range, entity.position().z + range));
		entities.removeIf(e -> e == entity || e instanceof BallLightningEntity);
		entities.removeIf(e -> e.distanceTo(entity) > range);
		entities.removeIf(e -> !(e instanceof LivingEntity));
		entities.removeIf(e -> (e instanceof LivingEntity en && en.getHealth() <= 0));

		for(Entity ent : entities){
			zapTarget(entity, ent, amp);
		}
	}
	
	public static void zapTarget(LivingEntity source, Entity target, int amp) {
		ClipContext cc = new ClipContext(source.getPosition(0), target.getPosition(0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
		if (target.level().clip(cc).getType() == HitResult.Type.BLOCK) {
			return;
		}
		float damage = (float) ((amp + 1) * HighVoltageAbility.getDamage(amp));
		if(source.level().isClientSide){
			// Creates a trail of particles between the entity and target(s)
			int steps = 20;
			Vector3f randLoc = MathUtils.randomPointInSphere((float) getPassiveRange(amp), source.getRandom());
			for (int i = 0; i < steps; i++) {
				Vec3 distV = new Vec3(target.getX() - source.getX(), target.getY() - source.getY(), target.getZ() - source.getZ());
				double distFrac = (steps - (double)(i)) / steps;
				// the current entity coordinate + ((the distance between it and the target) * (the fraction of the total))
				double stepX = source.getX() + (distV.x * distFrac);
				double stepY = source.getY() + (source.getEyeHeight() / 2) + (distV.y * distFrac);
				double stepZ = source.getZ() + (distV.z * distFrac);
				source.level().addParticle(new LargeLightningParticleData(16F, false), stepX, stepY, stepZ, 0.0, 0.0, 0.0);
			}
		} else {
			if (target instanceof LivingEntity livingtarget) {
				if (DragonUtils.isDragonType(target, DragonTypes.SEA)) return;
				if (TargetingFunctions.attackTargets(source, entity -> entity.hurt(source.damageSources().lightningBolt(), damage), target)) {
					livingtarget.setDeltaMovement(livingtarget.getDeltaMovement().multiply(0.25, 1, 0.25));
					onHurtTarget(source, livingtarget);
				}
			}
		}
		source.level().playLocalSound(target.position().x, target.position().y + 0.5, target.position().z, ADSoundRegistry.bugZapper, SoundSource.PLAYERS, 5F, 1F, false);
	}
	
	public static void onHurtTarget(LivingEntity source, Entity target) {
		if(source.getRandom().nextInt(100) < 50){
			if(!source.level().isClientSide){
				source.addEffect(new MobEffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(30)));
			}
		}

		if(!target.level().isClientSide){
			if(!StormBreathAbility.chargedBlacklist.contains(ResourceHelper.getKey(target).toString())){
				if(source.getRandom().nextInt(100) < 40){
					EntityStateHandler cap = DragonUtils.getEntityHandler(target);

					cap.lastAfflicted = source.getId();
					cap.chainCount = 1;

					if (target instanceof LivingEntity livingtarget)
						livingtarget.addEffect(new MobEffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(10), 0, false, true));
				}
			}
			if (target instanceof LivingEntity le)
				StormBreathAbility.onDamageChecks(le);
		}
	}

	public static void producePassiveParticles(LivingEntity entity, int amp) {
		if (entity.level().isClientSide()) {
			// Create particles sometimes because it's pretty
			if (/* 40% 4/sec */ entity.getRandom().nextInt(100) < 40) {
				for (int i = 0; i < (5 * (amp + 1)); i++) {
					Vector3f loc = MathUtils.randomPointInSphere((float) getPassiveRange(amp), entity.getRandom());
					float randX = (entity.getRandom().nextFloat() * 3f) - 1.5f;
					float randY = (entity.getRandom().nextFloat() * 1f) - 0.5f;
					float randZ = (entity.getRandom().nextFloat() * 3f) - 1.5f;
					entity.level().addParticle(new LargeLightningParticleData(15, false), entity.getX() + loc.x(), entity.getY() + entity.getEyeHeight() + loc.y(), entity.getZ() + loc.z(), randX * 0.1, randY * 0.1, randZ * 0.1);
				}
			}
		}
	}


	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[] {0, 15};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(highVoltageCooldown);
	}

	@Override
	public boolean requiresStationaryCasting(){return true;}

	@Override
	public AbilityAnimation getLoopingAnimation(){
		return new AbilityAnimation("cast_self_buff", true, false);
	}

	@Override
	public AbilityAnimation getStoppingAnimation(){
		return new AbilityAnimation("self_buff", 0.52 * 20, true, false);
	}

	public int getDuration(){
		return (int) (highVoltageDuration * getLevel());
	}
	
	@Override
	public String getName() {
		return "high_voltage";
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.duration.seconds", getDuration()));
		components.add(Component.translatable("ds.skill.range.passive", (int) getPassiveRange()));
		components.add(Component.translatable("ds.skill.range.active", (int) getActiveRange()));
		components.add(Component.translatable("ds.skill.damage", (int) getDamage()));

		if(!KeyInputHandler.ABILITY4.isUnbound()){
			String key = KeyInputHandler.ABILITY4.getKey().getDisplayName().getString().toUpperCase(Locale.ROOT);

			if(key.isEmpty()){
				key = KeyInputHandler.ABILITY4.getKey().getDisplayName().getString();
			}
			components.add(Component.translatable("ds.skill.keybind", key));
		}
		return components;
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.PRIMORDIAL;
	}

	@Override
	public ResourceLocation[] getSkillTextures(){
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/high_voltage_0.png"),
		                              new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/high_voltage_1.png"),
		                              new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/high_voltage_2.png")};
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.duration.seconds", "+" + highVoltageDuration));
		list.add(Component.translatable("ds.skill.range.passive", "+" + highVoltagePassiveRange));
		list.add(Component.translatable("ds.skill.range.active", "+" + highVoltageActiveRange));
		list.add(Component.translatable("ds.skill.damage", "+" + highVoltageDamage));
		return list;
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

}
