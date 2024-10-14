package by.psither.dragonsurvival.magic.abilities.Astral.passive;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.MagicAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class AstralMagicAbility extends MagicAbility{
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives"}, key = "astralMagic", comment = "Whether the astral magic ability should be enabled" )
    public static Boolean astralMagic = true;

    @Override
    public int getSortOrder(){
        return 1;
    }

    @Override
    public String getName(){
        return "astral_magic";
    }

    @Override
    public AbstractDragonType getDragonType(){
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures(){
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_5.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_6.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_7.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_8.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_9.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_10.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_11.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_12.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_13.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_14.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_15.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_16.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_17.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_18.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_19.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_magic_20.png")
        };
    }

    @Override
    public boolean isDisabled(){
        return super.isDisabled() || !astralMagic;
    }
}