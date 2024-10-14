package by.psither.dragonsurvival.magic.abilities.Astral.innate;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.input.Keybind;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.innate.DragonWingAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class AstralWingsAbility extends DragonWingAbility {
    @Override
    public Component getDescription(){
        DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler(player);
        if (handler.hasFlight()) {
            String key = Keybind.TOGGLE_WINGS.getKey().getDisplayName().getString().toUpperCase(Locale.ROOT);

            if(key.isEmpty())
                key = Keybind.TOGGLE_WINGS.getKey().getDisplayName().getString();

            return Component.translatable("ds.skill.description.astral_wings", key).append("\n").append(Component.translatable("ds.skill.description.astral_wings" + (handler.getMovementData().spinLearned ? ".has_spin" : ".no_spin")));
        }
        return Component.translatable("ds.skill.description.astral_wings.no_flight");
    }

    @Override
    public String getName() {
        return "astral_wings";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures(){
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_wings_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/astral_wings_1.png")};
    }

    @Override
    public int getSortOrder(){
        return 2;
    }
}
