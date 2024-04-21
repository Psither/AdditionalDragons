package by.psither.dragonsurvival.client.particles.SeaDragon;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class DragonBubbleParticle extends TextureSheetParticle {
	public DragonBubbleParticle(ClientLevel world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public DragonBubbleParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
		super(world, x, y, z, xd, yd, zd);
		gravity = 0.05f;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}
}
