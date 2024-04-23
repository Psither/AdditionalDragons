package by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.ADParticles;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;

import java.util.ArrayList;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.types.SeaDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.AoeBuffAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.DragonEffects;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@RegisterDragonAbility
public class BubbleShieldAbility extends AoeBuffAbility {
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShield", comment = "Whether the bubble shield ability should be enabled" )
	public static Boolean bubbleShield = true;

	@ConfigRange( min = 1.0, max = 10000.0 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldDuration", comment = "The duration in seconds of the bubble shield effect given when the ability is used" )
	public static Double bubbleShieldDuration = 100.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldCooldown", comment = "The cooldown in seconds of the bubble shield ability" )
	public static Double bubbleShieldCooldown = 60.0;

	@ConfigRange( min = 0.05, max = 10000 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldCasttime", comment = "The cast time in seconds of the bubble shield ability" )
	public static Double bubbleShieldCasttime = 1.0;

	@ConfigRange( min = 0, max = 100 )
	@ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldManaCost", comment = "The mana cost for using the bubble shield ability" )
	public static Integer bubbleShieldManaCost = 1;

	@ConfigRange ( min = 0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldPreventsWaterDamage", comment = "Whether cave dragons can safely enter water with this effect active" )
	public static boolean bubbleShieldPreventsWaterDamage = true;
	
	@ConfigRange ( min = 0, max = 10000.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldStrength", comment = "How much damage the bubble shield can sustain before being lost" )
	public static Double bubbleShieldStrength = 2.0;
	
	@ConfigRange ( min = 1.0, max = 100.0 )
	@ConfigOption ( side = ConfigSide.SERVER, category = {"magic", "abilities", "primordial_dragon", "bubble_shield"}, key = "bubbleShieldHydrationFactor", comment = "How much longer it takes to dry out while in a bubble shield." )
	public static Double bubbleShieldHydrationFactor = 5.0;

	@Override
	public boolean isDisabled(){
		return super.isDisabled() || !bubbleShield;
	}

	@Override
	public int getRange() {
		return 5;
	}
	
	public static void produceBubbles(LivingEntity entity) {
		if (entity.level().isClientSide()) {
			// Create particles because it's pretty
			for (int i = 0; i < 6; i++) {
				if (entity.getRandom().nextInt(100) < (entity.isInWaterRainOrBubble() ? 30 : 70)) {
					float randX = (entity.getRandom().nextFloat() * 1.5F) - 0.75F;
					float randY = entity.getRandom().nextFloat() - 0.5f;
					float randZ = (entity.getRandom().nextFloat() * 1.5F) - 0.75F;
					entity.level().addParticle(ADParticles.dragonBubbleParticle, entity.getX() + randX, entity.getY() + (entity.getEyeHeight() / 2) + randY, entity.getZ() + randZ, 0.0, 0.0, 0.0);
				}
			}
			if (DragonUtils.isDragonType(entity, DragonTypes.CAVE) && entity.getRandom().nextInt(100) < (entity.isInWaterRainOrBubble() ? 30 : 70)) {
				float randX = (entity.getRandom().nextFloat() * 1F) - 0.5F;
				float randY = entity.getRandom().nextFloat() - 0.5f;
				float randZ = (entity.getRandom().nextFloat() * 1F) - 0.5F;
				entity.level().addParticle(ParticleTypes.FLAME, entity.getX() + randX, entity.getY() + randY, entity.getZ() + randZ, 0.0, 0.0, 0.0);
			}
		}
	}
	
	public static void restoreHydrationAndAir(Player player) {
		if (!player.level().isClientSide()) {
			if (DragonUtils.isDragonType(player, DragonTypes.SEA)) {
				Level world = player.level();
				Biome biome = world.getBiome(player.blockPosition()).value();
				SeaDragonType sdt = (SeaDragonType) DragonUtils.getHandler(player).getType();

				boolean hotBiome = biome.getPrecipitationAt(player.blockPosition()) == Precipitation.NONE && biome.getBaseTemperature() > 1.0;
				double timeIncrement = (world.isNight() ? 0.5F : 1.0) * (hotBiome ? biome.getBaseTemperature() : 1F);
				sdt.timeWithoutWater -= (ServerConfig.seaTicksBasedOnTemperature ? timeIncrement : 1) * ((bubbleShieldHydrationFactor - 2) / (bubbleShieldHydrationFactor - 1)) * 5;
				return;
			} else {
				player.setAirSupply(player.getMaxAirSupply());
			}
			if (DragonUtils.isDragonType(player, DragonTypes.CAVE)) {
				player.addEffect(new MobEffectInstance(DragonEffects.FIRE, 20, 0, true, true));
			}
		}
	}

	@Override
	public ParticleOptions getParticleEffect() {
		return ADParticles.dragonBubbleParticle;
	}

	@Override
	public MobEffectInstance getEffect(){
		return new MobEffectInstance(ADDragonEffects.BUBBLE_SHIELD, Functions.secondsToTicks(bubbleShieldDuration), getLevel() - 1);
	}

	@Override
	public int getSkillCastingTime() {
		return Functions.secondsToTicks(bubbleShieldCasttime);
	}

	@Override
	public int getManaCost() {
		return bubbleShieldManaCost;
	}

	@Override
	public Integer[] getRequiredLevels() {
		return new Integer[]{0, 25, 40};
	}

	@Override
	public int getSkillCooldown() {
		return Functions.secondsToTicks(bubbleShieldCooldown);
	}

	@Override
	public String getName() {
		return "bubble_shield";
	}
	
	@Override
	public int getSortOrder() {
		return 3;
	}

	@Override
	public AbstractDragonType getDragonType() {
		return ADDragonTypes.PRIMORDIAL;
	}

	@Override
	public ResourceLocation[] getSkillTextures() {
		return new ResourceLocation[]{new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/bubble_shield_0.png"),
                					new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/bubble_shield_1.png"),
                					new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/bubble_shield_2.png"),
                					new ResourceLocation(AdditionalDragonsMod.MODID, "textures/skills/primordial/bubble_shield_3.png")};
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public ArrayList<Component> getLevelUpInfo(){
		ArrayList<Component> list = super.getLevelUpInfo();
		list.add(Component.translatable("ds.skill.shield.strength", "+" + bubbleShieldStrength));
		list.add(Component.translatable("ds.skill.duration.seconds", "+" + bubbleShieldDuration));
		return list;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

}
