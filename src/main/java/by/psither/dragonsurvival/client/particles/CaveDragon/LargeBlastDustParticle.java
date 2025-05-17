package by.psither.dragonsurvival.client.particles.CaveDragon;

import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.DragonParticle;
import by.dragonsurvivalteam.dragonsurvival.common.particles.SmallFireParticleOption;
import by.psither.dragonsurvival.common.particles.LargeBlastDustParticleOption;

import java.awt.Color;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;

public class LargeBlastDustParticle extends DragonParticle {
	Color color;

	protected LargeBlastDustParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, double duration, boolean swirls, int color, SpriteSet sprite) {
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, duration, swirls, sprite);
		this.color = getColorFromInt(color);
	}

	@Override
	public void remove(){
		if (this.level.getRandom().nextInt(100) < 5) {
			level.addParticle(new SmallFireParticleOption(16, false), x, y, z, 0, 0.01, 0);
		}
		super.remove();
	}

	public static Color getColorFromInt(int color) {
		return new Color(color);
	}

	public static final class Factory implements ParticleProvider<LargeBlastDustParticleOption>{
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite){ spriteSet = sprite; }

		@Override
		public Particle createParticle(LargeBlastDustParticleOption type, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			LargeBlastDustParticle particle = new LargeBlastDustParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, type.duration(), type.swirls(), type.color(), spriteSet);
			particle.setSpriteFromAge(spriteSet);
			particle.setColor(particle.color.getRed() / 255f, particle.color.getGreen() / 255f, particle.color.getBlue() / 255f);
			return particle;
		}
	}
}