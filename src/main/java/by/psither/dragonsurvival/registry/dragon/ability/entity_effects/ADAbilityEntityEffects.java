package by.psither.dragonsurvival.registry.dragon.ability.entity_effects;

import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ADAbilityEntityEffects {
    @SubscribeEvent
    public static void registerEntries(final RegisterEvent event) {
        if (event.getRegistry() == AbilityEntityEffect.REGISTRY) {
            event.register(AbilityEntityEffect.REGISTRY_KEY, AdditionalDragonsMod.res("group_heal_unique_effect"), () -> GroupHealUniqueEffect.CODEC);
        }
    }
}
