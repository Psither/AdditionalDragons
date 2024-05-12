package by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mojang.math.Vector3f;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.utils.MathUtils;
import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.AbilityAnimation;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ChargeCastAbility;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@RegisterDragonAbility
public class GroupHealAbility extends ChargeCastAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHeal", comment = "Whether the Group Heal ability should be enabled" )
	public static boolean groupHeal = true;

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHealSelfStrength", comment = "How much healing you gain without valid targets" )
	public static Double groupHealSelfStrength = 1.5;

	@ConfigRange( min = 0.05, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHealCooldown", comment = "The cooldown in seconds of the Group Heal ability" )
	public static Double groupHealCooldown = 10.0;

	@ConfigRange( min = 0.05, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHealCasttime", comment = "The cast time in seconds of the Group Heal ability" )
	public static Double groupHealCasttime = 0.8;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHealManaCost", comment = "The mana cost for using the Group Heal ability" )
	public static Integer groupHealManaCost = 2;
	
	@ConfigRange( min = 0, max = 10000.0)
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHealRange", comment = "The radius of the Group Heal ability, multiplied by the ability level" )
	public static Double groupHealRange = 2.0;
	
	@ConfigRange ( min = 0, max = 10000.0)
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "group_heal"}, key = "groupHealStrength", comment = "The strength of the Group Heal ability, multiplied by the ability level" )
	public static Double groupHealStrength = 1.5;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !groupHeal;
	}

	@Override
	public int getSortOrder() {
		return 2;
	}
	
	public float getRange() {
		return (float) (groupHealRange * getLevel());
	}
	
	public float getHealStrength() {
		return (float) (groupHealStrength * getLevel());
	}
	
	public float getSelfHealStrength() {
		return (float) (groupHealSelfStrength * getLevel());
	}

	@Override
	public int getManaCost() {
		return groupHealManaCost;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[]{0, 20, 45, 50};
	}

	@Override
	public int getSkillCooldown(){
		return Functions.secondsToTicks(groupHealCooldown);
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(groupHealCasttime);
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.aoe", (int) getRange() + "x" + (int) getRange()));
		components.add(Component.translatable("ds.skill.heal.strength", getHealStrength()));

		if(!KeyInputHandler.ABILITY2.isUnbound()){
			String key = KeyInputHandler.ABILITY2.getKey().getDisplayName().getString().toUpperCase(Locale.ROOT);

			if(key.isEmpty()){
				key = KeyInputHandler.ABILITY2.getKey().getDisplayName().getString();
			}
			components.add(Component.translatable("ds.skill.keybind", key));
		}
		return components;
	}

	@Override
	public String getName() {
		return "group_heal";
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.PRIMORDIAL;
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/group_heal_0.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/group_heal_1.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/group_heal_2.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/group_heal_3.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/group_heal_4.png")};
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public AbilityAnimation getLoopingAnimation(){
		return new AbilityAnimation("cast_self_buff", true, false);
	}

	@Override
	public AbilityAnimation getStoppingAnimation(){
		return new AbilityAnimation("self_buff", 0.52 * 20, true, false);
	}
	
	@Override
	public void onCasting(Player player, int currentCastTime) {
		
	}

	@Override
	public void castingComplete(Player player){
		// Reused from AoeBuffAbility
		if (player.level.isClientSide()) {
			float f5 = (float)Math.PI * getRange() * getRange() * 0.4f;
	
			for(int i = 0; i < 5; i++)
				for(int k1 = 0; (float)k1 < f5; ++k1){
					Vector3f vec = MathUtils.randomPointInSphere(getRange(), player.getRandom());
					//float f6 = player.getRandom().nextFloat() * ((float)Math.PI * 2F);
	
					//float f7 = Mth.sqrt(player.getRandom().nextFloat()) * getRange();
					//float f8 = Mth.cos(f6) * f7;
					//float f9 = Mth.sin(f6) * f7;
					player.level.addAlwaysVisibleParticle(getParticleEffect(), player.getX() + vec.x(), player.getY() + vec.y(), player.getZ() + vec.z(), (0.5D - player.getRandom().nextDouble()) * 0.15D, 0.01F, (0.5D - player.getRandom().nextDouble()) * 0.15D);
					//player.level.addAlwaysVisibleParticle(getParticleEffect(), player.getX() + (double)f8, player.getY(), player.getZ() + (double)f9, (0.5D - player.getRandom().nextDouble()) * 0.15D, 0.01F, (0.5D - player.getRandom().nextDouble()) * 0.15D);
				}
			player.level.playLocalSound(player.position().x, player.position().y + 0.5, player.position().z, SoundEvents.UI_TOAST_OUT, SoundSource.PLAYERS, 2F, 0.1F, false);
		}
		List<LivingEntity> list1 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(getRange()));
		if(!list1.isEmpty())
			for(LivingEntity livingentity : list1){
				double d0 = livingentity.getX() - player.getX();
				double d1 = livingentity.getZ() - player.getZ();
				double d2 = d0 * d0 + d1 * d1;

				float healSum = 0;
				if(d2 <= (double)(getRange() * getRange())){
					healSum += applyHealOrHurt(livingentity, player);
				}
				if (healSum > 0) {
					player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (int) (healSum * 24), 2));
				}
			}
	}

	private float applyHealOrHurt(LivingEntity livingentity, Player player) {
		if (livingentity.equals(player)) {
			return getSelfHealStrength();
		}
		if (livingentity.isInvertedHealAndHarm()) {
			if (!player.level.isClientSide()) {
				float hp = livingentity.getHealth();
				livingentity.hurt(DamageSource.playerAttack(player).setMagic().bypassArmor(), getHealStrength() * 2);
				return Math.min(hp, getHealStrength() * 2);
			}
			else {
				for (int i = 0; i < getHealStrength() / 2; i++) {
					float randX = (player.getRandom().nextFloat() - 0.5f) * 0.7f;
					float randY = (player.getRandom().nextFloat() - 0.5f) * 0.7f;
					float randZ = (player.getRandom().nextFloat() - 0.5f) * 0.7f;
					player.level.addAlwaysVisibleParticle(ParticleTypes.DAMAGE_INDICATOR, livingentity.getX() + randX, livingentity.getY() + livingentity.getEyeHeight() + randY, livingentity.getZ() + randZ, 0.0, 0.0, 0.0);
					
				}
				return 0;
			}
		} else {
			float missingHealth = livingentity.getMaxHealth() - livingentity.getHealth();
			if (!player.level.isClientSide()) {
				livingentity.heal(getHealStrength());
				return (Math.min(missingHealth, getHealStrength()));
			}
			else {
				for (int i = 0; i < Math.min(missingHealth, getHealStrength()) / 2; i++) {
					float randX = (player.getRandom().nextFloat() - 0.5f) * 0.7f;
					float randY = (player.getRandom().nextFloat() - 0.5f) * 0.7f;
					float randZ = (player.getRandom().nextFloat() - 0.5f) * 0.7f;
					player.level.addAlwaysVisibleParticle(ParticleTypes.HEART, livingentity.getX() + randX, livingentity.getY() + livingentity.getEyeHeight() + randY, livingentity.getZ() + randZ, 0.0, 0.0, 0.0);
					player.level.playLocalSound(livingentity.position().x, livingentity.position().y + 0.5, livingentity.position().z, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2F, 1.4F, true);
					
				}
				return 0;
			}
		}
	}

	private ParticleOptions getParticleEffect() {
		return ParticleTypes.HAPPY_VILLAGER;
	}
	
	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.heal.strength", "+" + groupHealStrength));
		list.add(Component.translatable("ds.skill.range.blocks", "+" + groupHealRange));
		return list;
	}
}
