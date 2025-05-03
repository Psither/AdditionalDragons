package by.psither.dragonsurvival.client.particles.ForestDragon;

import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.DragonParticle;
import by.dragonsurvivalteam.dragonsurvival.common.particles.SmallPoisonParticleOption;
import by.psither.dragonsurvival.common.particles.SmallConfoundParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SmallConfoundParticle extends DragonParticle {
	public SmallConfoundParticle(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls, SpriteSet sprite){
		super(world, x, y, z, vX, vY, vZ, duration, swirls, sprite);
	}

	@Override
	public void remove(){
		if (this.level.getRandom().nextInt(100) < 5)
			level.addParticle(new SmallPoisonParticleOption(16, false), x, y, z, 0, 0.01, 0);
		super.remove();
	}

	@OnlyIn( Dist.CLIENT )
	public static final class Factory implements ParticleProvider<SmallConfoundParticleOption>{
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite){ spriteSet = sprite; }

		@Override
		public Particle createParticle(SmallConfoundParticleOption type, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			SmallConfoundParticle particle = new SmallConfoundParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, type.duration(), type.swirls(), spriteSet);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
