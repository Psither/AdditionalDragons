package by.psither.dragonsurvival.magic.abilities.Astral.active;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;
import static by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType.isCharged;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.AoeBuffAbility;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class PhaseOutAbility extends AoeBuffAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "phase_out"}, key = "phaseOut", comment = "Whether the phase out ability should be enabled" )
    public static Boolean phaseOut = true;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "phase_out"}, key = "phaseOutCooldown", comment = "The cooldown in seconds of the phase out ability" )
    public static Double phaseOutCooldown = 100.0;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "phase_out"}, key = "phaseOutCooldown", comment = "The cooldown in seconds of the phase out ability" )
    public static Double phaseOutCastTime = 1.0;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "phase_out"}, key = "phaseOutDuration", comment = "The duration in seconds of the phase out effect" )
    public static Double phaseOutDuration = 30.0;

    @ConfigRange( min = 0, max = 100 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "active", "phase_out"}, key = "phaseOutManaCost", comment = "The mana cost for using the phase out ability" )
    public static Integer phaseOutManaCost = 1;
    
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
        return 3;
    }

    @Override
    public String getName() {
        return "phase_out";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        if (isCharged(this.player)) {
            return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/phase_out_0.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/phase_out_1.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/phase_out_2.png")
            };
        }
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/unreality_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/unreality_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/unreality_2.png")
        };
    }

    @Override
    public int getMinLevel() {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getSkillCastingTime() {
        return Functions.secondsToTicks(phaseOutCastTime);
    }

    @Override
    public void castingComplete(Player player) {
        super.castingComplete(player);
        if (!player.level().isClientSide()) {
            if (DragonStateProvider.getOrGenerateHandler(player).getType() instanceof AstralDragonType astral && astral.charges > 0) {
                astral.charges--;
            }
        }
    }

    @Override
    public int getRange() {
        return 5;
    }

    @Override
    public ParticleOptions getParticleEffect() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    public MobEffectInstance getEffect() {
        return isCharged(player) ? new MobEffectInstance(ADDragonEffects.UNREALITY, Functions.secondsToTicks(phaseOutDuration * this.level / 2)) : new MobEffectInstance(ADDragonEffects.PHASE_OUT, Functions.secondsToTicks(phaseOutDuration * this.level));
    }

    @Override
    public int getManaCost() {
        return phaseOutManaCost;
    }

    @Override
    public Integer[] getRequiredLevels(){
        return new Integer[]{0, 7, 13};
    }

    @Override
    public int getSkillCooldown() {
        return Functions.secondsToTicks(phaseOutCooldown);
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || !phaseOut;
    }
}
