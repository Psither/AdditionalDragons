package by.psither.dragonsurvival.server.handlers;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.util.DragonUtils;
import by.psither.dragonsurvival.common.dragon_types.ADDragonTypes;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@SuppressWarnings("unused")
@EventBusSubscriber
public class ServerFlightHandler {

    @SubscribeEvent
    public static void playerFlightIcon(PlayerTickEvent.Post playerTickEvent) {
        Player player = playerTickEvent.getEntity();
        DragonStateProvider.getCap(player).ifPresent(handler -> {
           if (handler.isDragon() && player.tickCount % 10 == 0 && handler.isWingsSpread()) {
               if (DragonUtils.isDragonType(player, ADDragonTypes.ASTRAL)) {
                   player.addEffect(new MobEffectInstance(ADDragonEffects.ASTRAL_WINGS, -1, 0, true, false, true));
               }
           }
           if (!handler.isDragon() || !handler.isWingsSpread()) {
               if (player.hasEffect(ADDragonEffects.ASTRAL_WINGS)) {
                   player.resetFallDistance();
                   player.removeEffect(ADDragonEffects.ASTRAL_WINGS);
               }
           }
        });
    }
}
