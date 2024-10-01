package by.psither.dragonsurvival.magic.abilities.Astral.innate;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.innate.InnateDragonAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.resources.ResourceLocation;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class AstralDragonInfoAbility extends InnateDragonAbility {
    @Override
    public String getName() {
        return "astral_dragon";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures(){
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_dragon.png")};
    }

    @Override
    public int getSortOrder(){
        return 3;
    }
}
