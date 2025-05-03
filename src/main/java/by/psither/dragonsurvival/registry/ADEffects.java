package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.effects.BubbleShieldEffect;
import by.psither.dragonsurvival.common.effects.HighVoltageEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ADEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, AdditionalDragonsMod.MODID);

    public static Holder<MobEffect> HIGH_VOLTAGE = REGISTRY.register("high_voltage", () -> new HighVoltageEffect(MobEffectCategory.BENEFICIAL, 0x0, false));
    public static Holder<MobEffect> BUBBLE_SHIELD = REGISTRY.register("bubble_shield", () -> new BubbleShieldEffect(MobEffectCategory.BENEFICIAL, 0x0)
            .addAttributeModifier(Attributes.MAX_ABSORPTION, AdditionalDragonsMod.res("effect.bubble_shield.absorption"), 2.0, AttributeModifier.Operation.ADD_VALUE));
}
