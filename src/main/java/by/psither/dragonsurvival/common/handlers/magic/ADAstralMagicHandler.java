package by.psither.dragonsurvival.common.handlers.magic;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;
import by.psither.dragonsurvival.registry.ADDragonEffects;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

@SuppressWarnings("unused")
@EventBusSubscriber
public class ADAstralMagicHandler {
    @SubscribeEvent
    public static void interactWithBlock(UseItemOnBlockEvent event) {
        Player player = event.getPlayer();
        if (player.hasEffect(ADDragonEffects.PHASE_OUT) || player.hasEffect(ADDragonEffects.UNREALITY)) {
            event.cancelWithResult(ItemInteractionResult.FAIL);
        }
    }

    @SubscribeEvent
    public static void takeDamage(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && DragonStateProvider.isDragon(player)) {
            DragonStateHandler handler = DragonStateProvider.getOrGenerateHandler(player);
            if (handler.getType() instanceof AstralDragonType astral) {
                event.setNewDamage(astral.onPlayerDamaged(player, event.getNewDamage()));
            }
        }
    }
}
