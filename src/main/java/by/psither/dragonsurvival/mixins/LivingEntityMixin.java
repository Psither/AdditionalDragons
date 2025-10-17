package by.psither.dragonsurvival.mixins;

import by.psither.dragonsurvival.registry.ADEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean additionalDragons$isInvulnerableTo(final boolean original, @Local(argsOnly = true) DamageSource source) {
        if (((LivingEntity) (Object) this).hasEffect(ADEffects.PHASE_OUT)) {
            if (source.isDirect() && source.getDirectEntity() instanceof LivingEntity livingEntity) {
                if (!livingEntity.hasEffect(ADEffects.PHASE_OUT)) {
                    return true;
                }
            } else if (source.getEntity() instanceof LivingEntity livingEntity) {
                if (!livingEntity.hasEffect(ADEffects.PHASE_OUT)) {
                    return true;
                }
            }
        } else {
            if (source.isDirect() && source.getDirectEntity() instanceof LivingEntity sourceEntity && sourceEntity.hasEffect(ADEffects.PHASE_OUT)) {
                return true;
            } else if (source.getEntity() instanceof LivingEntity sourceEntity && sourceEntity.hasEffect(ADEffects.PHASE_OUT)) {
                return true;
            }
        }
        return original;
    }
}
