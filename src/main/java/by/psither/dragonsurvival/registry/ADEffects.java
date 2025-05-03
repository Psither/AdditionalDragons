package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.effects.HighVoltageEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ADEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, AdditionalDragonsMod.MODID);

    public static Holder<MobEffect> HIGH_VOLTAGE = REGISTRY.register("", () -> new HighVoltageEffect(MobEffectCategory.BENEFICIAL, 0x0, false));
}
