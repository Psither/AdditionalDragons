package by.psither.dragonsurvival.magic.abilities.Astral.innate;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.innate.InnateDragonAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.config.ADServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class VoidBodyAbility extends InnateDragonAbility {
    @Override
    public Component getDescription(){
        return Component.translatable("ds.skill.description." + getName());
    }

    @Override
    public int getMaxLevel(){
        return 1;
    }

    @Override
    public int getMinLevel(){
        return 0;
    }

    @Override
    public String getName(){
        return "void_body";
    }

    @Override
    public AbstractDragonType getDragonType(){
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures(){
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/void_body_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/void_body_1.png")};
    }

    @Override
    public int getLevel(){
        return ServerConfig.penalties && ADServerConfig.VOID_BODY_DRAIN != 0 ? 1 : 0;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public boolean isDisabled(){
        return super.isDisabled() || !ServerConfig.penalties || ADServerConfig.VOID_BODY_DRAIN == 0;
    }

    @Override
    public int getSortOrder(){
        return 4;
    }
}
