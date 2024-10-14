package by.psither.dragonsurvival.magic.abilities.Astral.active;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;
import static by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType.isCharged;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigOption;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigRange;
import by.dragonsurvivalteam.dragonsurvival.config.obj.ConfigSide;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.BreathAbility;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@RegisterDragonAbility
public class AstralBreathAbility extends BreathAbility {
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreath", comment = "Whether the astral breath ability should be enabled" )
    public static Boolean astralBreath = true;

    @ConfigRange( min = 0.0, max = 100.0 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreathDamage", comment = "The amount of damage the astral breath ability deals. This value is multiplied by the skill level." )
    public static Double astralBreathDamage = 1.0;

    @ConfigRange( min = 0, max = 100 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreathInitialMana", comment = "The mana cost for starting the astral breath ability" )
    public static Integer astralBreathInitialMana = 1;

    @ConfigRange( min = 0.05, max = 10000 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreathCooldown", comment = "The cooldown in seconds of the astral breath ability" )
    public static Double astralBreathCooldown = 10.0;

    @ConfigRange( min = 0.05, max = 10000.0 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreathCasttime", comment = "The cast time in seconds of the astral breath ability" )
    public static Double astralBreathCasttime = 1.0;

    @ConfigRange( min = 0, max = 100 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreathOvertimeMana", comment = "The mana cost of sustaining the astral breath ability" )
    public static Integer astralBreathOvertimeMana = 1;

    @ConfigRange( min = 0.0, max = 100.0 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "astralBreathManaTicks", comment = "How often in seconds, mana is consumed while using astral breath" )
    public static Double astralBreathManaTicks = 3.0;

    @ConfigRange( min = 0.0, max = 10000.0 )
    @ConfigOption( side = ConfigSide.SERVER, category = {"magic", "abilities", "astral_dragon", "astral_breath"}, key = "annihilationThreshold", comment = "The health threshold at which targets will be erased from existence")
    public static Double annihilationThreshold = 2.5;

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
        return 1;
    }

    @Override
    public boolean canHitEntity(LivingEntity entity){
        return !(entity instanceof Player) || player.canHarmPlayer((Player)entity);
    }

    @Override
    public void onEntityHit(LivingEntity entity) {
        if (!DragonUtils.isDragonType(entity, ADDragonTypes.ASTRAL)) {
            super.onEntityHit(entity);
        }
    }

    @Override
    public void onDamage(LivingEntity livingEntity) {
        if (isCharged(this.player)) {
            doChargedEffects(livingEntity);
        } else {
            doUnchargedEffects(livingEntity);
        }
    }

    public void doChargedEffects(LivingEntity livingEntity){
        if (player.level().isClientSide()) return;

        if (livingEntity.getHealth() < annihilationThreshold) {
            if (!(livingEntity instanceof Player)) {
                livingEntity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            } else {
                livingEntity.kill();
            }
        }
    }

    public void doUnchargedEffects(LivingEntity livingEntity){
        if (player.level().isClientSide()) return;

        for (MobEffectInstance instance : livingEntity.getActiveEffects()) {
            if (instance.getEffect().value().isBeneficial()) {
                livingEntity.removeEffect(instance.getEffect());
            }
        }
    }

    @Override
    public void onKeyReleased(Player player) {
        if (this.chargeTime >= this.getSkillChargeTime()) {
            DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler(player);
            if (handler.getType() instanceof AstralDragonType astral && astral.charges > 0) {
                astral.charges--;
            }
        }
        super.onKeyReleased(player);
    }

    @Override
    public float getDamage(){
        return getDamage(getLevel());
    }

    public static float getDamage(int level) {
        return (float) (astralBreathDamage * level);
    }

    @Override
    public void onBlock(BlockPos blockPos, BlockState blockState, Direction direction) {

    }

    @Override
    public int getSkillChargeTime() {
        return Functions.secondsToTicks(astralBreathCasttime);
    }

    @Override
    public int getContinuousManaCostTime() {
        return Functions.secondsToTicks(astralBreathManaTicks);
    }

    @Override
    public int getInitManaCost() {
        return astralBreathInitialMana;
    }

    @Override
    public void castComplete(Player player) {

    }

    @Override
    public int getManaCost() {
        return astralBreathOvertimeMana;
    }

    @Override
    public Integer[] getRequiredLevels(){
        return new Integer[]{0, 11, 29, 43};
    }

    @Override
    public ArrayList<Component> getInfo(){
        ArrayList<Component> components = super.getInfo();
        if (isCharged(player)) {
            components.add(Component.translatable("ds.skill.annihilation_threshold", "" + annihilationThreshold));
        }
        components.add(Component.translatable("ds.skill.damage", "+" + astralBreathDamage));
        return components;
    }

    @Override
    public ArrayList<Component> getLevelUpInfo(){
        ArrayList<Component> components = super.getLevelUpInfo();
        components.add(Component.translatable("ds.skill.damage", "+" + astralBreathDamage));
        return components;
    }

    @Override
    public int getSkillCooldown() {
        return Functions.secondsToTicks(astralBreathCooldown);
    }

    @Override
    public String getName() {
        return "astral_breath";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return ADDragonTypes.ASTRAL;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        if (isCharged(this.player)) {
            return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/annihilation_breath_0.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/annihilation_breath_1.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/annihilation_breath_2.png"),
                    ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/annihilation_breath_3.png"),
            };
        }
        return new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/nullification_breath_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/nullification_breath_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/nullification_breath_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/astral/nullification_breath_3.png"),
        };
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getMinLevel() {
        return 3;
    }

    @Override
    public boolean isDisabled(){
        return super.isDisabled() || !astralBreath;
    }
}
