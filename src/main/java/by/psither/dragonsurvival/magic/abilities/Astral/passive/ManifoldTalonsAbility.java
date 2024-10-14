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
public class ManifoldTalonsAbility extends PassiveDragonAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives"}, key = "manifoldTalons", comment = "Whether the manifold talons ability should be enabled" )
    public static Boolean manifoldTalons = true;

    @Override
    public int getSortOrder(){
        return 4;
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
        return "manifold_talons";
    }

    @Override
    public AbstractDragonType getDragonType(){
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/manifold_talons.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/manifold_talons.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/manifold_talons.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/manifold_talons.png"),
        };
    }

    @Override
    public boolean isDisabled(){
        return super.isDisabled() || !manifoldTalons;
    }
}
