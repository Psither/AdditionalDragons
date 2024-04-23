package by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joml.Vector3f;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.LargeFireParticleData;
import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.SmallFireParticleData;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.AbilityAnimation;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ChargeCastAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.DSDamageTypes;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADDamageTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import by.psither.dragonsurvival.utils.MathUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

@RegisterDragonAbility
public class PyroclasticRoarAbility extends ChargeCastAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "pyroclasticRoar", comment = "Whether the pyroclastic roar ability should be enabled" )
	public static Boolean pyroclasticRoar = true;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "pyroclasticRoarDamage", comment = "The amount of damage the pyroclastic roar ability deals. This value is multiplied by the skill level." )
	public static Double pyroclasticRoarDamage = 2.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "pyroclasticRoarManaCost", comment = "The mana cost for the pyroclastic roar ability" )
	public static Integer pyroclasticRoarManaCost = 1;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "pyroclasticRoarCasttime", comment = "The cast time in seconds of the pyroclastic roar ability" )
	public static Double pyroclasticRoarCasttime = 2.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "pyroclasticRoarRange", comment = "The range of the pyroclastic roar ability" )
	public static Double pyroclasticRoarRange = 2.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "pyroclasticRoarCooldown", comment = "The cooldown in seconds of the pyroclastic roar ability" )
	public static Double pyroclasticRoarCooldown = 30.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "volcanicRageEffectDuration", comment = "How long the Volcanic Rage effect lasts" )
	public static Double volcanicRageEffectDuration = 40.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "volcanicRageArmorToughnessValue", comment = "How much armor toughness you gain from Volcanic Rage" )
	public static Double volcanicRageArmorToughnessValue = 3.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "volcanicRageAttackDamageValue", comment = "How much attack damage you gain from Volcanic Rage" )
	public static Double volcanicRageAttackDamageValue = 3.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "pyroclastic_roar"}, key = "volcanicRageAttackKnockbackValue", comment = "How much attack knockback you gain from Volcanic Rage" )
	public static Double volcanicRageAttackKnockbackValue = 3.0;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !pyroclasticRoar;
	}

	@Override
	public void castingComplete(Player player){
		if (player.level().isClientSide()) {
			for (int i = 0; i < getRange() * getRange() * 10; i++) {
				Vector3f vec = MathUtils.randomPointInSphere(getRange(), player.getRandom());
				ClipContext cc = new ClipContext(player.getPosition(0), new Vec3(player.getX() + vec.x(), player.getY() + vec.y(), player.getZ() + vec.z()), ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, null);
				if (player.level().clip(cc).getType() == HitResult.Type.BLOCK) {
					continue;
				}
				player.level().addAlwaysVisibleParticle(new LargeFireParticleData(27 + player.getRandom().nextInt(20), true), player.getX() + vec.x(), player.getY() + vec.y(), player.getZ() + vec.z(), (0.5D - player.getRandom().nextDouble()) * 0.15D, 0.01F, (0.5D - player.getRandom().nextDouble()) * 0.15D);
			}
			player.level().addAlwaysVisibleParticle(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 0.0F, 0.0F, 0.0F);
			player.level().playLocalSound(player.position().x, player.position().y + 0.5, player.position().z, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 40F, 0.9F, false);
		} else {
			List<LivingEntity> list1 = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(getRange() * 2));
			if(!list1.isEmpty())
				for (LivingEntity livingentity : list1) {
					applyEffects(livingentity, player);
				}
		}
	}

	public void applyEffects(LivingEntity entity, Player player) {
		if (entity.equals(player)) {
			player.addEffect(new MobEffectInstance(ADDragonEffects.VOLCANIC_RAGE, getDuration(), getLevel() - 1));
		} else { // Shamelessly stolen from Explosion, with a few additions
			ClipContext cc = new ClipContext(player.getPosition(0), entity.getPosition(0), ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, null);
			if (player.level().clip(cc).getType() == HitResult.Type.BLOCK) {
				return;
			}
			float f2 = getRange() * 2.0F;
			Vec3 vec3 = new Vec3(player.getX(), player.getY(), player.getZ());
			double d5 = entity.getX() - player.getX();
			double d7 = entity.getY() - player.getY();
			double d9 = entity.getZ() - player.getZ();
			double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
			if (d13 != 0.0D) {
				d5 /= d13;
				d7 /= d13;
				d9 /= d13;
				double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f2;
				double d10 = (getRange() - d12) * 0.3;
				entity.hurt(this.getDamageSource(), (float) getDamage());
				double d11 = d10;
				if (entity instanceof LivingEntity) {
					d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
				}

				entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
			}
		}
	}
	
	public double getDamage() {
		return pyroclasticRoarDamage * getLevel();
	}

	private DamageSource getDamageSource() {
		return ADDamageTypes.entityDamageSource(player.level(), ADDamageTypes.PYRO_ROAR, player);
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(pyroclasticRoarCasttime);
	}

	@Override
	public void onCasting(Player player, int arg1) {
		if (player.level().isClientSide()) {
			for (int i = 0; i < getRange() * getRange() * 0.2; i++) {
				Vector3f vec = MathUtils.randomPointInSphere(getRange(), player.getRandom());
				ClipContext cc = new ClipContext(player.getPosition(0), new Vec3(player.getX() + vec.x(), player.getY() + vec.y(), player.getZ() + vec.z()), ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, null);
				if (player.level().clip(cc).getType() == HitResult.Type.BLOCK) {
					continue;
				}
				player.level().addAlwaysVisibleParticle(new SmallFireParticleData(16, true), player.getX() + vec.x(), player.getY() + vec.y(), player.getZ() + vec.z(), 0.0F, 0.04F, 0.0F);
			}
			if (!DragonUtils.getHandler(player).isWingsSpread()) {
				player.setDeltaMovement(0, 0, 0);
			}
		}
	}

	@Override
	public int getManaCost() {
		return pyroclasticRoarManaCost;
	}
	
	public float getRange() {
		return (float) (pyroclasticRoarRange * getLevel());
	}
	
	public int getDuration() {
		return Functions.secondsToTicks(volcanicRageEffectDuration * getLevel());
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.damage", (int) getDamage()));
		components.add(Component.translatable("ds.skill.aoe", (int) getRange() + "x" + (int) getRange()));
		return components;
	}

	@Override
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.damage", "+" + pyroclasticRoarDamage));
		list.add(Component.translatable("ds.skill.aoe", "+" + pyroclasticRoarRange));
		return list;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[]{0, 20, 45};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(pyroclasticRoarCooldown);
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.TECTONIC;
	}

	@Override
	public AbilityAnimation getLoopingAnimation(){
		return new AbilityAnimation("sit_on_magic_source", true, false);
	}

	@Override
	public boolean requiresStationaryCasting(){
		return false;
	}

	@Override
	public int getSortOrder() {
		return 4;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "pyroclastic_roar";
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/pyroclastic_roar_0.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/pyroclastic_roar_1.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/pyroclastic_roar_2.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/pyroclastic_roar_3.png")};
	}
}
