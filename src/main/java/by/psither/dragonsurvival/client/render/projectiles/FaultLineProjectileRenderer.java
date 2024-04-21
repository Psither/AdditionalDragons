package by.psither.dragonsurvival.client.render.projectiles;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.common.entity.projectiles.FaultLineProjectileEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class FaultLineProjectileRenderer extends ArrowRenderer<FaultLineProjectileEntity> {
	public FaultLineProjectileRenderer(EntityRendererProvider.Context p_i46179_1_){
		super(p_i46179_1_);
	}

	@Override
	public ResourceLocation getTextureLocation(FaultLineProjectileEntity entity){
		return new ResourceLocation(AdditionalDragonsMod.MODID, "textures/entity/fault_line_" + FaultLineProjectileEntity.getAmmoTypeName(entity.getAmmoType()) + ".png");
	}
}