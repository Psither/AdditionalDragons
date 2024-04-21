package by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.KeyInputHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.InstantCastAbility;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticleData;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.common.entity.projectiles.FaultLineProjectileEntity;
import by.psither.dragonsurvival.registry.ADEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@RegisterDragonAbility
public class FaultLineAbility extends InstantCastAbility {

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLine", comment = "Whether the fault line ability should be enabled" )
	public static Boolean faultLine = true;

	@ConfigRange( min = 0.0, max = 100.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLineDamage", comment = "The amount of damage the fault line ability deals on contact. This value is multiplied by the skill level." )
	public static Double faultLineDamage = 2.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLineManaCost", comment = "The mana cost for the fault line ability" )
	public static Integer faultLineManaCost = 1;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLineCooldown", comment = "The cooldown in seconds of the fault line ability" )
	public static Double faultLineCooldown = 2.0;
	
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLineMultishot", comment = "Whether Fault Line will fire additional shots per level." )
	public static boolean faultLineMultishot = true;

	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLineMultishotConsumesMore", comment = "Whether each additional shot will consume ammo and can be picked up to recover it." )
	public static boolean faultLineMultishotConsumesMore = false;
	
	@ConfigRange( min = 0.0, max = 100.0)
	@ConfigOption (side = ConfigSide.SERVER, category = {"magic", "abilities", "tectonic_dragon", "fault_line"}, key = "faultLineSpread", comment = "The amount each additional shot fired will add to its inaccuracy")
	public static Float faultLineSpread = 3.0F;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !faultLine;
	}

	public static Map<String, Item> faultLineAmmoTypes = Map.ofEntries(
			Map.entry("cobblestone", Items.COBBLESTONE),
			Map.entry("stone", Items.STONE),
			Map.entry("cobbled_deepslate", Items.COBBLED_DEEPSLATE),
			Map.entry("deepslate", Items.DEEPSLATE),
			Map.entry("mud", Items.MUD),
			Map.entry("granite", Items.GRANITE),
			Map.entry("diorite", Items.DIORITE),
			Map.entry("andesite", Items.ANDESITE),
			Map.entry("sandstone", Items.SANDSTONE),
			Map.entry("tuff", Items.TUFF),
			Map.entry("netherrack", Items.NETHERRACK),
			Map.entry("blackstone", Items.BLACKSTONE),
			Map.entry("basalt", Items.BASALT),
			Map.entry("magma_block", Items.MAGMA_BLOCK),
			Map.entry("end_stone", Items.END_STONE)
	);

	public static Map<Item, SoundEvent> faultLineAmmoSounds = Map.ofEntries(
			Map.entry(Items.COBBLESTONE, SoundEvents.STONE_BREAK),
			Map.entry(Items.STONE, SoundEvents.STONE_BREAK),
			Map.entry(Items.COBBLED_DEEPSLATE, SoundEvents.DEEPSLATE_BREAK),
			Map.entry(Items.DEEPSLATE, SoundEvents.DEEPSLATE_BREAK),
			Map.entry(Items.MUD, SoundEvents.MUD_BREAK),
			Map.entry(Items.GRANITE, SoundEvents.STONE_BREAK),
			Map.entry(Items.DIORITE, SoundEvents.STONE_BREAK),
			Map.entry(Items.ANDESITE, SoundEvents.STONE_BREAK),
			Map.entry(Items.SANDSTONE, SoundEvents.STONE_BREAK),
			Map.entry(Items.TUFF, SoundEvents.STONE_BREAK),
			Map.entry(Items.NETHERRACK, SoundEvents.NETHERRACK_BREAK),
			Map.entry(Items.BLACKSTONE, SoundEvents.STONE_BREAK),
			Map.entry(Items.BASALT, SoundEvents.BASALT_BREAK),
			Map.entry(Items.MAGMA_BLOCK, SoundEvents.STONE_BREAK),
			Map.entry(Items.END_STONE, SoundEvents.STONE_BREAK)
	);

	float[] spreadPatternX = { 0f, 0f, 0f, 5f, -5f };
	float[] spreadPatternY = { 0f, 5f, -5f, 0f, 0f };

	@Override
	public void onCast(Player player){
		Vec3 vector3d = player.getViewVector(1.0F);
		double speed = 1d;
		double d2 = vector3d.x * speed;
		double d3 = vector3d.y * speed;
		double d4 = vector3d.z * speed;
		boolean creative = player.getAbilities().instabuild; // creative mode

		DragonStateHandler handler = DragonUtils.getHandler(player);
		handler.getMovementData().bite = true;

		for (int i = getLevel(); i > 0; i--) {
			ItemStack shot = getAmmo(player, creative || (!faultLineMultishotConsumesMore && i > 1));
			if (shot.equals(ItemStack.EMPTY)) {
				if (creative) {
					// In creative, make a standard shot.
					shot = new ItemStack(Items.STONE);
				} else if (!faultLineMultishotConsumesMore && i > 0) {
					// No ammo to shoot, and not in creative, make some smoke.
					player.level.addParticle(ParticleTypes.SMOKE, player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), 0.0, 0.0, 0.0);
					continue;
				}
			}

			FaultLineProjectileEntity entity = new FaultLineProjectileEntity(ADEntities.FAULT_LINE, player, player.level);
			entity.setPos(entity.getX() + d2, entity.getY() + d3, entity.getZ() + d4);
			if (shot.getItem().equals(Items.BLACKSTONE) || shot.getItem().equals(Items.DEEPSLATE) || shot.getItem().equals(Items.STONE))
				entity.setBaseDamage(getDamage() * 1.2f);
			else if (shot.getItem().equals(Items.MUD))
				entity.setBaseDamage(getDamage() * 0.85f);
			else
				entity.setBaseDamage(getDamage());
			entity.setAmmoType(shot);
			entity.setShotLevel(getLevel());
			entity.setSoundEvent(faultLineAmmoSounds.get(shot.getItem()));
			if (shot.getItem().equals(Items.MAGMA_BLOCK) && !entity.isInWaterRainOrBubble())
				entity.setSecondsOnFire(5);
			if (creative || (!faultLineMultishotConsumesMore && i > 1))
				entity.pickup = AbstractArrow.Pickup.DISALLOWED;
			entity.shootFromRotation(player, player.getXRot() + spreadPatternX[i], player.getYRot() + spreadPatternY[i], 0.0F, 4F, i * faultLineSpread);
			player.level.addFreshEntity(entity);
			
			if (!faultLineMultishot)
				break;
		}
	}
	
	public ItemStack getAmmo(Player player, boolean flag) {
		ItemStack ammoStack = getFirstAmmoType(player);
		if (ammoStack == null) return ItemStack.EMPTY;
		ItemStack shot;
		if (!flag) {
			shot = ammoStack.split(1);
		} else {
			shot = ammoStack.copy().split(1);
		}
		return shot;
	}

	public ItemStack getFirstAmmoType(Player player) {
		if (faultLineAmmoTypes.containsValue(player.getMainHandItem().getItem()))
			return player.getMainHandItem();
		else if (faultLineAmmoTypes.containsValue(player.getOffhandItem().getItem()))
			return player.getOffhandItem();
		for (ItemStack is : player.getInventory().items) {
			if (faultLineAmmoTypes.containsValue(is.getItem())) {
				return is;
			}
		}
		return null;
	}

	public double getDamage() {
		return faultLineDamage * getLevel();
	}

	@Override
	public int getManaCost() {
		return faultLineManaCost;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[]{0, 15, 25, 35};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(faultLineCooldown);
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.TECTONIC;
	}
	
	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.damage", "+" + faultLineDamage));
		return list;
	}

	@Override
	public ArrayList<Component> getInfo(){
		ArrayList<Component> components = super.getInfo();
		components.add(Component.translatable("ds.skill.damage", (int) getDamage()));

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
	public int getSortOrder(){
		return 2;
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
	public boolean requiresStationaryCasting(){
		return false;
	}

	@Override
	public String getName() {
		return "fault_line";
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/fault_line_0.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/fault_line_1.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/fault_line_2.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/fault_line_3.png"),
				  					  new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/tectonic/fault_line_4.png")};
	}
}
