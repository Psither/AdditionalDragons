package by.psither.dragonsurvival.common.effects;

import by.dragonsurvivalteam.dragonsurvival.DragonSurvival;
import by.dragonsurvivalteam.dragonsurvival.registry.attachments.DSDataAttachments;
import by.dragonsurvivalteam.dragonsurvival.registry.attachments.PenaltySupply;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BubbleShieldEffect extends MobEffect {
    private final ResourceLocation waterSupply = DragonSurvival.res("water_supply");
    public BubbleShieldEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getAbsorptionAmount() <= 0 && !livingEntity.level().isClientSide) {
            return false;
        }

        livingEntity.setAirSupply(livingEntity.getMaxAirSupply());
        if (livingEntity instanceof Player player) {
            PenaltySupply penaltySupply = player.getData(DSDataAttachments.PENALTY_SUPPLY);
            if (penaltySupply.hasSupply(waterSupply)) {
                penaltySupply.setSupply(waterSupply, penaltySupply.getRawSupply(waterSupply) + ((amplifier + 1) * 2));
            }
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(2 * (1 + amplifier))));
    }
}
