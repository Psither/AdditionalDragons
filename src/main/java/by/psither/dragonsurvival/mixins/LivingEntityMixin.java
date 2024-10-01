package by.psither.dragonsurvival.mixins;

import by.psither.dragonsurvival.registry.ADDragonEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyReturnValue(method="isInvulnerableTo", at=@At(value= "RETURN"))
    public boolean isInvulnerableTo$additionalDragons(boolean original, @Local(argsOnly = true) DamageSource src) {
        if (((LivingEntity)(Object) this).hasEffect(ADDragonEffects.UNREALITY)) {
            return original && src.isDirect();
        } else if (((LivingEntity)(Object) this).hasEffect(ADDragonEffects.PHASE_OUT)) {
            if (!src.isDirect() && src.getEntity() instanceof LivingEntity attacker && !attacker.hasEffect(ADDragonEffects.PHASE_OUT)) {
                return false;
            }
        }
        if (src.getEntity() instanceof LivingEntity living) {
            if (living.hasEffect(ADDragonEffects.UNREALITY) || ((LivingEntity)(Object) this).hasEffect(ADDragonEffects.PHASE_OUT) && !living.hasEffect(ADDragonEffects.PHASE_OUT)) {
                return true;
            }
        }
        return original;
    }
}
