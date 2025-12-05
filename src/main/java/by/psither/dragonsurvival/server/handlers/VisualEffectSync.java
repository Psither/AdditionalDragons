package by.psither.dragonsurvival.server.handlers;

import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncVisualEffectAdded;
import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncVisualEffectRemoval;
import by.psither.dragonsurvival.registry.ADEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber
public class VisualEffectSync {
    private static final List<Holder<MobEffect>> VISUAL_EFFECTS = List.of(
            ADEffects.HIGH_VOLTAGE,
            ADEffects.BUBBLE_SHIELD,
            ADEffects.INVIGORATE,
            ADEffects.CONFOUNDED,
            ADEffects.SEEKING_TALONS,
            ADEffects.PHASE_OUT
    );

    @SubscribeEvent
    public static void handleEffectAdded(final MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();

        if (entity.level().isClientSide()) {
            return;
        }

        MobEffectInstance instance = event.getEffectInstance();

        if (!VISUAL_EFFECTS.contains(instance.getEffect())) {
            return;
        }

        PacketDistributor.sendToPlayersTrackingEntity(entity, new SyncVisualEffectAdded(entity.getId(), instance));
    }

    @SubscribeEvent
    public static void handleEffectRemoved(final MobEffectEvent.Remove event) {
        handleEffectRemoval(event.getEffectInstance(), event.getEntity());
    }

    @SubscribeEvent
    public static void handleEffectExpired(final MobEffectEvent.Expired event) {
        handleEffectRemoval(event.getEffectInstance(), event.getEntity());
    }

    private static void handleEffectRemoval(final MobEffectInstance instance, final LivingEntity entity) {
        // Client-side entry is likely our own packet
        if (instance == null || entity.level().isClientSide()) {
            return;
        }

        if (!VISUAL_EFFECTS.contains(instance.getEffect())) {
            return;
        }

        PacketDistributor.sendToPlayersTrackingEntity(entity, new SyncVisualEffectRemoval(entity.getId(), instance.getEffect()));
    }
}
