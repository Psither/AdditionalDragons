package by.psither.dragonsurvival.client.particles.SeaDragon;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class DragonBubbleParticle extends TextureSheetParticle {
	public DragonBubbleParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
		super(world, x, y, z, xd, yd, zd);
		gravity = 0.05f;
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static final class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite){
			spriteSet = sprite;
		}

		@Override
		public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			DragonBubbleParticle particle = new DragonBubbleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
