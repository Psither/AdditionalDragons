package by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active;

import java.util.ArrayList;
import java.util.Locale;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.psither.dragonsurvival.common.entity.projectiles.MistyBarbProjectileEntity;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ChargeCastAbility;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@RegisterDragonAbility
public class MistyBarbAbility extends ChargeCastAbility {
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "misty_barb"}, key = "mistyBarb", comment = "Whether the misty barb ability should be enabled" )
	public static boolean mistyBarb = true;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "misty_barb"}, key = "mistyBarbCooldown", comment = "The cooldown in seconds of the misty barb ability" )
	public static double mistyBarbCooldown = 10.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "misty_barb"}, key = "mistyBarbCasttime", comment = "The cast time in seconds of the misty barb ability" )
	public static double mistyBarbCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "misty_barb"}, key = "mistyBarbManaCost", comment = "The mana cost for using the misty barb ability" )
	public static Integer mistyBarbManaCost = 1;
	
	@ConfigRange( min = 0.0, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "misty_barb"}, key = "mistyBarbDamage", comment = "The damage the barb will inflict on a direct hit" )
	public static double mistyBarbDamage = 1.0;
	
	@ConfigRange( min = 0.0, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "deepwoods_dragon", "misty_barb"}, key = "mistyBarbRadius", comment = "The radius of the Drain cloud produced after the barb detonates") 
	public static double mistyBarbRadius = 0.8;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !mistyBarb;
	}

	@Override
	public void castingComplete(Player player){
		Vec3 vector3d = player.getViewVector(1.0F);
		double speed = 1d;
		double d2 = vector3d.x * speed;
		double d3 = vector3d.y * speed;
		double d4 = vector3d.z * speed;

		DragonStateHandler handler = DragonUtils.getHandler(player);
		handler.getMovementData().bite = true;

		MistyBarbProjectileEntity entity = new MistyBarbProjectileEntity(ADEntities.MISTY_BARB, player, player.level);
		entity.setPos(entity.getX() + d2, entity.getY() + d3, entity.getZ() + d4);
		entity.setShotLevel(getLevel());
		entity.setBaseDamage(getDamage());
		entity.pickup = AbstractArrow.Pickup.DISALLOWED;
		entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4F, 1.0F);
		player.level.addFreshEntity(entity);
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(mistyBarbCasttime);
	}

	@Override
	public void onCasting(Player player, int castTime) {}

	@Override
	public int getManaCost() {
		return mistyBarbManaCost;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[]{0, 20, 30, 40};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(mistyBarbCooldown);
	}

	public double getDamage() {
		return mistyBarbDamage * getLevel();
	}

	public double getRange() {
		return mistyBarbRadius * getLevel();
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.DEEPWOODS;
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
	public int getSortOrder() {
		return 2;
	}

	@Override
	public String getName() {
		return "misty_barb";
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.damage", "+" + mistyBarbDamage));
		list.add(Component.translatable("ds.skill.range.blocks", "+" + mistyBarbRadius));
		return list;
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.damage", getDamage()));
		components.add(Component.translatable("ds.skill.range.blocks", getRange()));

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
	public boolean requiresStationaryCasting(){
		return false;
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/misty_barb_0.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/misty_barb_1.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/misty_barb_2.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/misty_barb_3.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/deepwoods/misty_barb_4.png")};
	}

}
