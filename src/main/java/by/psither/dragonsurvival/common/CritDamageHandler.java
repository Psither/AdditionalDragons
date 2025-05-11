package by.psither.dragonsurvival.common;

import by.psither.dragonsurvival.registry.ADAttributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

@EventBusSubscriber
public class CritDamageHandler {
    @SubscribeEvent
    public static void modifyCritDamage(final CriticalHitEvent event) {
        event.setDamageMultiplier((float) (event.getDamageMultiplier() * event.getEntity().getAttributeValue(ADAttributes.CRIT_MULTIPLIER)));
    }
}
