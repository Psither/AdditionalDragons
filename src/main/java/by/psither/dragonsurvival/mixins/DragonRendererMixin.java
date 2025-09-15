package by.psither.dragonsurvival.mixins;

import by.dragonsurvivalteam.dragonsurvival.client.render.entity.dragon.DragonRenderer;
import by.dragonsurvivalteam.dragonsurvival.common.entity.DragonEntity;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.magic.HunterHandler;
import by.psither.dragonsurvival.registry.ADEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import software.bernie.geckolib.util.Color;

@Mixin(DragonRenderer.class)
public abstract class DragonRendererMixin {
    @ModifyReturnValue(method = "getRenderColor(Lby/dragonsurvivalteam/dragonsurvival/common/entity/DragonEntity;FI)Lsoftware/bernie/geckolib/util/Color;", at=@At("RETURN"))
    public Color additionalDragons$getRenderColor(Color original, @Local(argsOnly = true) DragonEntity animatable) {
        Player player = animatable.getPlayer();
        if (player == null)
            return original;
        if (!player.isInvisible() && player.hasEffect(ADEffects.PHASE_OUT)) {
            int color = HunterHandler.applyAlpha((float) Math.max(0.05f, (1.0 - (player.getEffect(ADEffects.PHASE_OUT).getDuration() / 1000.0))), original.getColor());
            return Color.ofARGB(FastColor.ARGB32.alpha(color), FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color));
        }

        return original;
    }
}
