package by.psither.dragonsurvival.client.particles.SeaDragon;

import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.DragonParticle;
import by.psither.dragonsurvival.common.particles.LargeGlowSlimeParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;

public class LargeGlowSlimeParticle extends DragonParticle {
	public LargeGlowSlimeParticle(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls, SpriteSet sprite){
		super(world, x, y, z, vX, vY, vZ, duration, swirls, sprite);
	}

	public static final class Factory implements ParticleProvider<LargeGlowSlimeParticleOption> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite){ spriteSet = sprite; }

		@Override
		public Particle createParticle(LargeGlowSlimeParticleOption type, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			LargeGlowSlimeParticle particle = new LargeGlowSlimeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, type.duration(), type.swirls(), spriteSet);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
