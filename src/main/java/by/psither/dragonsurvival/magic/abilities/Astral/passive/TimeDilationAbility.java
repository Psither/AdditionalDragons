package by.psither.dragonsurvival.magic.abilities.Astral.passive;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.PassiveDragonAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class TimeDilationAbility extends PassiveDragonAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives"}, key = "timeDilation", comment = "Whether the time dilation ability should be enabled" )
    public static Boolean timeDilation = true;

    @Override
    public int getSortOrder(){
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getName(){
        return "time_dilation";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/time_dilation_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/time_dilation_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/time_dilation_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/time_dilation_3.png"),
        };
    }

    @Override
    public boolean isDisabled(){
        return super.isDisabled() || !timeDilation;
    }
}
