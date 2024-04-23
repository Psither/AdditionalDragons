package by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active;

import java.util.ArrayList;
import java.util.Locale;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.AbilityAnimation;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ChargeCastAbility;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@RegisterDragonAbility
public class SeekingTalonsAbility extends ChargeCastAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalons", comment = "Whether the seeking talons ability should be enabled" )
	public static Boolean seekingTalons = true;

	@ConfigRange( min = 1.0, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalonsDuration", comment = "The duration in seconds of the seeking talons effect given when the ability is used" )
	public static Double seekingTalonsDuration = 100.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalonsCooldown", comment = "The cooldown in seconds of the seeking talons ability" )
	public static Double seekingTalonsCooldown = 50.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalonsCasttime", comment = "The cast time in seconds of the seeking talons ability" )
	public static Double seekingTalonsCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalonsManaCost", comment = "The mana cost for using the seeking talons ability" )
	public static Integer seekingTalonsManaCost = 1;
	
	@ConfigRange( min = 0.0, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalonsCritBonus", comment = "The maximum bonus on your critical hit damage, decreases the more HP your target has" )
	public static Double seekingTalonsCritBonus = 1.0;
	
	@ConfigRange( min = 0.0, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "seeking_talons"}, key = "seekingTalonsBonusLoot", comment = "The amount of additional Looting added when scoring a kill" )
	public static Double seekingTalonsBonusLoot = 1.0;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !seekingTalons;
	}

	@Override
	public void castingComplete(Player player){
		player.addEffect(new MobEffectInstance(ADDragonEffects.SEEKING_TALONS, Functions.secondsToTicks(getDuration()), getLevel() - 1));
		player.level().playLocalSound(player.position().x, player.position().y + 0.5, player.position().z, SoundEvents.UI_TOAST_IN, SoundSource.PLAYERS, 5F, 0.1F, true);
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(seekingTalonsCasttime);
	}

	@Override
	public void onCasting(Player player, int casttime) { }

	@Override
	public int getManaCost() {
		return seekingTalonsManaCost;
	}

	@Override
	public Integer[] getRequiredLevels(){
		return new Integer[]{0, 20, 30};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(seekingTalonsCooldown);
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.DEEPWOODS;
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
		return "seeking_talons";
	}

	@Override
	public ResourceLocation[] getSkillTextures(){
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/seeking_talons_0.png"),
		                              new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/seeking_talons_1.png"),
		                              new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/seeking_talons_2.png"),
		                              new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/seeking_talons_3.png")};
	}

	public double getDuration() {
		return seekingTalonsDuration * getLevel();
	}
	
	public double getDamage() {
		return seekingTalonsCritBonus * getLevel();
	}
	
	public double getBonusLoot() {
		return seekingTalonsBonusLoot * getLevel();
	}

	@Override
	public boolean requiresStationaryCasting(){
		return false;
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.duration.seconds", getDuration()));
		components.add(Component.translatable("ds.skill.critboost", (int) getDamage() + "x"));
		components.add(Component.translatable("ds.skill.loot", getBonusLoot()));

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
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.duration.seconds", "+" + seekingTalonsDuration));
		list.add(Component.translatable("ds.skill.critboost", "+" + seekingTalonsCritBonus + "x"));
		list.add(Component.translatable("ds.skill.loot", "+" + seekingTalonsBonusLoot));
		return list;
	}

}
