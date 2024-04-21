package by.psither.dragonsurvival.client.particles.ForestDragon;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class QuestionMarkParticle extends TextureSheetParticle {
	public QuestionMarkParticle(ClientLevel world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public QuestionMarkParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
		super(world, x, y, z, xd, yd, zd);
		gravity = 0.02f;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
}
