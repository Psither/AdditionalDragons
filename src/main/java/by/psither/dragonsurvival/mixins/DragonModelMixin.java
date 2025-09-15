package by.psither.dragonsurvival.mixins;

import by.dragonsurvivalteam.dragonsurvival.client.models.DragonModel;
import by.dragonsurvivalteam.dragonsurvival.common.entity.DragonEntity;
import by.psither.dragonsurvival.registry.ADEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DragonModel.class)
public class DragonModelMixin {
    @ModifyReturnValue(method = "getRenderType(Lby/dragonsurvivalteam/dragonsurvival/common/entity/DragonEntity;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;", at=@At("RETURN"))
    public RenderType additionalDragons$getRenderType(RenderType original, @Local(argsOnly = true) DragonEntity animatable, @Local(argsOnly = true) final ResourceLocation texture) {
        Player player = animatable.getPlayer();

        if (player != null && player.hasEffect(ADEffects.PHASE_OUT)) {
            return RenderType.itemEntityTranslucentCull(texture);
        }
        return original;
    }
}
