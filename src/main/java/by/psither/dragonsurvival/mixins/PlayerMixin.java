package by.psither.dragonsurvival.mixins;

import by.psither.dragonsurvival.registry.ADEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyReturnValue(method = "mayUseItemAt", at = @At("RETURN"))
    private boolean additionalDragons$mayUseItemAt(boolean original) {
        if (((Player) (Object) this).hasEffect(ADEffects.PHASE_OUT)) {
            return false;
        }
        return original;
    }
}
