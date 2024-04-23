package by.psither.dragonsurvival.client.particles.SeaDragon;

import by.dragonsurvivalteam.dragonsurvival.client.particles.SeaDragon.LargeLightningParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LargeGlowSlimeParticle extends LargeLightningParticle {
	private final float spread;
	private final SpriteSet sprites;
	boolean swirls;
	private int swirlTick;

	public LargeGlowSlimeParticle(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls, SpriteSet sprite){
		super(world, x, y, z, vX, vY, vZ, duration, swirls, sprite);
		setSize(1, 1);
		xd = vX;
		yd = vY;
		zd = vZ;
		lifetime = (int)duration;
		swirlTick = random.nextInt(120);
		spread = random.nextFloat();
		hasPhysics = false;
		this.swirls = swirls;
		setSpriteFromAge(sprite);
		sprites = sprite;
	}

	@OnlyIn( Dist.CLIENT )
	public static final class ParticleFactory implements ParticleProvider<LargeGlowSlimeParticleData>{
		private final SpriteSet spriteSet;

		public ParticleFactory(SpriteSet sprite){
			spriteSet = sprite;
		}

		@Override
		public Particle createParticle(LargeGlowSlimeParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			LargeGlowSlimeParticle particle = new LargeGlowSlimeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getDuration(), typeIn.getSwirls(), spriteSet);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
