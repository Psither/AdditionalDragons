package by.psither.dragonsurvival.registry;

import by.dragonsurvivalteam.dragonsurvival.common.effects.ConfoundedEffect;
import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.effects.BubbleShieldEffect;
import by.psither.dragonsurvival.common.effects.HighVoltageEffect;
import by.psither.dragonsurvival.common.effects.MoonlightEffect;
import by.psither.dragonsurvival.common.effects.SeekingTalonsEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

import static by.psither.dragonsurvival.AdditionalDragonsMod.res;

public class ADEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, AdditionalDragonsMod.MODID);

    public static Holder<MobEffect> HIGH_VOLTAGE = REGISTRY.register("high_voltage", () -> new HighVoltageEffect(MobEffectCategory.BENEFICIAL, 0x0, false));
    public static Holder<MobEffect> BUBBLE_SHIELD = REGISTRY.register("bubble_shield", () -> new BubbleShieldEffect(MobEffectCategory.BENEFICIAL, 0x0)
            .addAttributeModifier(Attributes.MAX_ABSORPTION, res("effect.bubble_shield.absorption"), 2.0, AttributeModifier.Operation.ADD_VALUE));
    public static Holder<MobEffect> INVIGORATE = REGISTRY.register("invigorate", () -> new MoonlightEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, res("effect.invigorate.movement_speed"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.ATTACK_SPEED, res("effect.invigorate.attack_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.LUCK, res("effect.invigorate.luck"), 0.5, AttributeModifier.Operation.ADD_VALUE)
    );
    public static Holder<MobEffect> CONFOUNDED = REGISTRY.register("confounded", () -> new ConfoundedEffect(MobEffectCategory.HARMFUL, 0x0, false));
    public static Holder<MobEffect> SEEKING_TALONS = REGISTRY.register("seeking_talons", () -> new SeekingTalonsEffect(MobEffectCategory.BENEFICIAL, 0x0, false)
            .addAttributeModifier(ADAttributes.CRIT_MULTIPLIER, res("effect.seeking_talons.crit_multiplier"), 0.4, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(ADAttributes.BONUS_LOOT, res("effect.seeking_talons.bonus_loot"), 0.5, AttributeModifier.Operation.ADD_VALUE)
    );
}
