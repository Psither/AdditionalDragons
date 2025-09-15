package by.psither.dragonsurvival.common.effects;

import by.psither.dragonsurvival.registry.ADEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

@EventBusSubscriber
public class PhaseOutEffect extends MobEffect {
    public PhaseOutEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @SubscribeEvent
    public static void phaseVisible(final LivingEvent.LivingVisibilityEvent event) {
        if (event.getEntity().hasEffect(ADEffects.PHASE_OUT)) {
            event.modifyVisibility(1 - ((double) event.getEntity().getEffect(ADEffects.PHASE_OUT).getDuration() / 600));
        }
    }
}
