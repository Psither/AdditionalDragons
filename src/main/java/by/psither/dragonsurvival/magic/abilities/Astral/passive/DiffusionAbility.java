package by.psither.dragonsurvival.magic.abilities.Astral.passive;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.passive.PassiveDragonAbility;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import net.minecraft.resources.ResourceLocation;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

@RegisterDragonAbility
public class DiffusionAbility extends PassiveDragonAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives"}, key = "diffusion", comment = "Whether the diffusion ability should be enabled" )
    public static Boolean diffusion = true;
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives", "diffusion"}, key = "diffusionReduction", comment = "What portion of incoming damage is reduced by diffusion.")
    public static Float diffusionReduction = 0.6f;
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives", "diffusion"}, key = "diffusionReduction", comment = "How much diffusion's effectiveness is logarithmically reduced for each level below the maximum.  The lower the value, the less effective at lower levels.")
    public static Float diffusionReductionPerLevel = 0.85f;
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives", "diffusion"}, key = "diffusionMaximum", comment = "The maximum amount of incoming damage that can be reduced by diffusion.")
    public static Float diffusionMaximum = 1.0f;
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "passives", "diffusion"}, key = "diffusionCooldownPerDamage", comment = "How many ticks will be added to your cooldowns per point of damage reduced.")
    public static Integer diffusionCooldownPerDamage = 40;

    @Override
    public int getSortOrder(){
        return 2;
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 6;
    }

    @Override
    public String getName(){
        return "diffusion";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    public float getStrength() {
        return (float) (Math.pow(diffusionReductionPerLevel, getMaxLevel() - getLevel()) * diffusionReduction);
    }

    public float getNewDamage(float damage) {
        return Math.max(damage * getStrength(), damage - diffusionMaximum);
    }

    public int getCooldownAmount(float damageDiff) {
        return (int) (Math.pow(diffusionReductionPerLevel, getLevel()) * diffusionCooldownPerDamage * damageDiff);
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_5.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/diffusion_6.png"),
        };
    }

    @Override
    public boolean isDisabled(){
        return super.isDisabled() || !diffusion;
    }
}
