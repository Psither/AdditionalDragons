package by.psither.dragonsurvival.registry.dragon.ability.entity_effects;

import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.DragonAbilityInstance;
import by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.entity_effects.AbilityEntityEffect;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record GroupHealUniqueEffect(LevelBasedValue amount, LevelBasedValue conversion, LevelBasedValue regenLevel) implements AbilityEntityEffect {
    public static final MapCodec<GroupHealUniqueEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(GroupHealUniqueEffect::amount),
            LevelBasedValue.CODEC.fieldOf("conversion").forGetter(GroupHealUniqueEffect::conversion),
            LevelBasedValue.CODEC.fieldOf("regen_level").forGetter(GroupHealUniqueEffect::regenLevel)
    ).apply(instance, GroupHealUniqueEffect::new));

    private static final float[] ticksPerHeal = new float[] {50, 25, 12, 6, 3, 1};

    @Override
    public void apply(ServerPlayer dragon, DragonAbilityInstance ability, Entity target) {
        float bonusRegen = 0;

        if (target instanceof LivingEntity livingEntity) {
            if (livingEntity.isInvertedHealAndHarm()) {
                bonusRegen = Math.min(livingEntity.getHealth(), amount.calculate(ability.level()) * 2);
                livingEntity.hurt(dragon.damageSources().magic(), amount.calculate(ability.level()) * 2);
            } else {
                bonusRegen = Math.min(livingEntity.getMaxHealth() - livingEntity.getHealth(), amount.calculate(ability.level()));
                livingEntity.heal(amount.calculate(ability.level()));
            }
        }
        float totalHealing = 0;
        for (MobEffectInstance instance : dragon.getActiveEffects()) {
            if (instance.getEffect().is(MobEffects.REGENERATION)) {
                totalHealing = instance.getDuration() / ticksPerHeal[Math.clamp(instance.getAmplifier(), 0, ticksPerHeal.length - 1)];
            }
        }
        totalHealing += (bonusRegen * conversion.calculate(ability.level()));
        if (totalHealing > 0) {
            dragon.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION,
                    (int) (totalHealing * ticksPerHeal[(int) Math.clamp(regenLevel.calculate(ability.level()), 0, ticksPerHeal.length - 1)]),
                    (int) regenLevel.calculate(ability.level())
            ));
        }
    }

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() {
        return CODEC;
    }
}
