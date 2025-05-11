package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import static by.psither.dragonsurvival.AdditionalDragonsMod.MODID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ADAttributes {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MODID);

    public static final Holder<Attribute> CRIT_MULTIPLIER = REGISTRY.register("crit_multiplier", () -> new RangedAttribute(Translation.Type.ATTRIBUTE.wrap("crit_multiplier"), 1, 0, 16));
    public static final Holder<Attribute> BONUS_LOOT = REGISTRY.register("bonus_loot", () -> new RangedAttribute(Translation.Type.ATTRIBUTE.wrap("bonus_loot"), 0, 0, 16));

    @SubscribeEvent
    public static void attachAttributes(final EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, CRIT_MULTIPLIER);
        event.add(EntityType.PLAYER, BONUS_LOOT);
    }
}
