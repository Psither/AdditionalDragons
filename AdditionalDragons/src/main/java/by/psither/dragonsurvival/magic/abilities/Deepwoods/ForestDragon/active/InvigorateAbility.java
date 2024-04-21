package by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active;

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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@RegisterDragonAbility
public class InvigorateAbility extends AoeBuffAbility {
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorate", comment = "Whether the invigorate ability should be enabled" )
	public static Boolean invigorate = true;

	@ConfigRange( min = 1.0, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateDuration", comment = "The duration in seconds of the invigorate effect given when the ability is used" )
	public static Double invigorateDuration = 100.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateCooldown", comment = "The cooldown in seconds of the invigorate ability" )
	public static Double invigorateCooldown = 60.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateCasttime", comment = "The cast time in seconds of the invigorate ability" )
	public static Double invigorateCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateManaCost", comment = "The mana cost for using the invigorate ability" )
	public static Integer invigorateManaCost = 1;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateMovementSpeedBonus", comment = "The movement speed bonus applied while in light" )
	public static Double invigorateMovementSpeedBonus = 0.4;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateAttackSpeedBonus", comment = "The attack speed bonus applied while in light" )
	public static Double invigorateAttackSpeedBonus = 0.4;
	
	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "invigorate"}, key = "invigorateLuckBonus", comment = "The luck bonus applied while in light" )
	public static Double invigorateLuckBonus = 1.0;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !invigorate;
	}

	@Override
	public MobEffectInstance getEffect() {
		return new MobEffectInstance(ADDragonEffects.INVIGORATE, Functions.secondsToTicks(invigorateDuration), getLevel() - 1);
	}

	@Override
	public ParticleOptions getParticleEffect() {
		return DSParticles.magicBeaconParticle;
	}

	@Override
	public int getRange() {
		return 5;
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(invigorateCasttime);
	}

	@Override
	public int getManaCost() {
		return invigorateManaCost;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[]{0, 25};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(invigorateCooldown);
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.DEEPWOODS;
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
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.luck", "+" + invigorateLuckBonus));
		list.add(Component.translatable("ds.skill.movementspeed", "+" + invigorateMovementSpeedBonus));
		list.add(Component.translatable("ds.skill.attackspeed", "+" + invigorateAttackSpeedBonus));
		return list;
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.luck", (int) (invigorateLuckBonus * getLevel())));
		components.add(Component.translatable("ds.skill.movementspeed", invigorateMovementSpeedBonus * getLevel()));
		components.add(Component.translatable("ds.skill.attackspeed", invigorateAttackSpeedBonus * getLevel()));
		return components;
	}

	@Override
	public String getName() {
		return "invigorate";
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/invigorate_0.png"),
                					new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/invigorate_1.png"),
                					new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/invigorate_2.png")};
	}

}
