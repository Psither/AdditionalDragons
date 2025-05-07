package by.psither.dragonsurvival.common.effects;

import by.dragonsurvivalteam.dragonsurvival.common.codecs.predicates.CustomPredicates;
import by.dragonsurvivalteam.dragonsurvival.common.effects.ModifiableMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class InvigorateEffect extends ModifiableMobEffect {
    public InvigorateEffect(MobEffectCategory type, int color, boolean incurable) {
        super(type, color, incurable);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        float daytime = livingEntity.level().getTimeOfDay(0);
        if (CustomPredicates.getSunLightLevel(livingEntity) > 4 && (daytime > 12000 || daytime < 1000)) { // In light & the moon is out
            this.addAttributeModifiers(livingEntity.getAttributes(), amplifier);
        } else {
            this.removeAttributeModifiers(livingEntity.getAttributes());
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
