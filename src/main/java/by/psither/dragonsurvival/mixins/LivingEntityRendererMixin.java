package by.psither.dragonsurvival.mixins;

import by.dragonsurvivalteam.dragonsurvival.common.handlers.magic.HunterHandler;
import by.psither.dragonsurvival.registry.ADEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    protected LivingEntityRendererMixin(final EntityRendererProvider.Context context) {
        super(context);
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;"), index = 2)
    private boolean additionalDragons$getTranslucentRenderType(boolean isTranslucent, @Local(argsOnly = true) final T entity) {
        if (!isTranslucent) {
            return entity.hasEffect(ADEffects.PHASE_OUT);
        }
        return true;
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"), index = 4)
    private int additionalDragons$modifyAlpha(int color, @Local(argsOnly = true) final T entity) {
        if (!entity.isInvisible() && entity.hasEffect(ADEffects.PHASE_OUT)) {
            return HunterHandler.applyAlpha((float) Math.max(0.2f, (1.0 - (entity.getEffect(ADEffects.PHASE_OUT).getDuration() / 120.0))), color);
        }

        return color;
    }
}
