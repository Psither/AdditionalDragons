package by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active;

import java.util.ArrayList;
import java.util.Locale;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.client.particles.DSParticles;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.AoeBuffAbility;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

@RegisterDragonAbility
public class UnstoppableAbility extends AoeBuffAbility {

	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppable", comment = "Whether the unstoppable ability should be enabled")
	public static Boolean unstoppable = true;

	@ConfigRange(min = 0, max = 100)
	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppableManaCost", comment = "The mana cost for the unstoppable ability")
	public static Integer unstoppableManaCost = 1;

	@ConfigRange(min = 0.05, max = 10000)
	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppableCasttime", comment = "The cast time in seconds of the unstoppable ability")
	public static Double unstoppableCasttime = 2.0;

	@ConfigRange(min = 0.05, max = 10000)
	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppableRange", comment = "The range of the unstoppable ability")
	public static Double unstoppableRange = 5.0;

	@ConfigRange(min = 0.05, max = 10000)
	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppableCooldown", comment = "The cooldown in seconds of the unstoppable ability")
	public static Double unstoppableCooldown = 30.0;

	@ConfigRange(min = 0.05, max = 10000)
	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppableEffectDuration", comment = "How long the Unstoppable effect lasts")
	public static Double unstoppableEffectDuration = 100.0;

	@ConfigRange(min = 0.05, max = 10000)
	@ConfigOption(side = ConfigSide.SERVER, category = { "magic", "abilities", "tectonic_dragon", "unstoppable" }, key = "unstoppableKnockbackResistValue", comment = "How much knockback resistance you gain from Unstoppable")
	public static Double unstoppableKnockbackResistValue = 0.25;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !unstoppable;
	}

	@Override
	public MobEffectInstance getEffect() {
		return new MobEffectInstance(ADDragonEffects.UNSTOPPABLE, Functions.secondsToTicks(unstoppableEffectDuration), getLevel() - 1);
	}

	@Override
	public ParticleOptions getParticleEffect() {
		return DSParticles.fireBeaconParticle;
	}

	@Override
	public int getRange() {
		return (int) (unstoppableRange * 1);
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(unstoppableCasttime);
	}

	@Override
	public int getManaCost() {
		return unstoppableManaCost;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[] { 0, 25 };
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(unstoppableCooldown);
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.TECTONIC;
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.knockbackresist", (unstoppableKnockbackResistValue * getLevel())));
		return components;
	}
	
	@Override
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.knockbackresist", "+" + unstoppableKnockbackResistValue));
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

	@Override
	public int getSortOrder() {
		return 3;
	}

	@Override
	public String getName() {
		return "unstoppable";
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[] {
			new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/unstoppable_0.png"),
			new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/unstoppable_1.png"),
			new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/unstoppable_2.png") 
		};
	}

}
