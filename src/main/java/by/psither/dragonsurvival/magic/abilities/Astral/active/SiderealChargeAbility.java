package by.psither.dragonsurvival.magic.abilities.Astral.active;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;
import static by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType.isCharged;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ChargeCastAbility;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class SiderealChargeAbility extends ChargeCastAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "siderealCharge"}, key = "siderealCharge", comment = "Whether the sidereal charge ability should be enabled" )
    public static Boolean siderealCharge = true;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "siderealCharge"}, key = "siderealChargeCooldown", comment = "The cooldown in seconds of the sidereal charge ability" )
    public static Double siderealChargeCooldown = 5.0;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "siderealCharge"}, key = "siderealChargeCooldown", comment = "The cooldown in seconds of the sidereal charge ability" )
    public static Double siderealChargeCastTime = 3.0;

    @ConfigRange( min = 0, max = 100 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "siderealCharge"}, key = "siderealChargeManaCost", comment = "The mana cost for using the sidereal charge ability" )
    public static Integer siderealChargeManaCost = 1;
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getTitle() {
        return Component.translatable("ds.skill." + this.getName() + (isCharged(player) ? ".enhanced" : ""));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getDescription(){
        return Component.translatable("ds.skill.description." + getName() + (isCharged(player) ? ".enhanced" : ""));
    }

    @Override
    public int getSortOrder() {
        return 4;
    }

    public int getManaCost() {
        return siderealChargeManaCost;
    }

    @Override
    public Integer[] getRequiredLevels() {
        return new Integer[]{0, 5, 17, 37};
    }

    public int getSkillCastingTime() {
        return Functions.secondsToTicks(siderealChargeCastTime);
    }

    @Override
    public void onCasting(Player player, int currentCastTime) {

    }

    public int getSkillCooldown() {
        return Functions.secondsToTicks(siderealChargeCooldown);
    }

    @Override
    public void castingComplete(Player player) {
        if (DragonStateProvider.getOrGenerateHandler(player).getType() instanceof AstralDragonType astral) {
            astral.charges = this.level;
        }
    }

    public String getName() { return "sidereal_charge"; }
    public int getMinLevel() { return 0; }
    public int getMaxLevel() { return 3; }
    public AbstractDragonType getDragonType() { return ADDragonTypes.ASTRAL; }

    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/sidereal_charge_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/sidereal_charge_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/sidereal_charge_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/sidereal_charge_3.png"),
        };
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || !siderealCharge;
    }
}
