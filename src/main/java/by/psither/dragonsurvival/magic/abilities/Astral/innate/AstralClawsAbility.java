package by.psither.dragonsurvival.magic.abilities.Astral.innate;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.innate.DragonClawsAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.resources.ResourceLocation;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

@RegisterDragonAbility
public class AstralClawsAbility extends DragonClawsAbility {
    @Override
    public String getName() {
        return "astral_claws_and_teeth";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures(){
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_5.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_6.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_claws_and_teeth_7.png")};
    }

    @Override
    public int getSortOrder() {
        return 1;
    }
}
