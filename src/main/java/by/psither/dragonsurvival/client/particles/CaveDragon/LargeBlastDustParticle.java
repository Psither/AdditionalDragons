package by.psither.dragonsurvival.client.particles.CaveDragon;

import java.awt.Color;

import by.dragonsurvivalteam.dragonsurvival.client.particles.DSParticles;
import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.LargeFireParticle;
import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.SmallFireParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LargeBlastDustParticle extends LargeFireParticle {
	private final float spread;
	private final SpriteSet sprites;
	private final Color color;
	boolean swirls;
	private int swirlTick;

	public LargeBlastDustParticle(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls, int color, SpriteSet sprite){
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
		this.color = getColorFromInt(color);
		setSpriteFromAge(sprite);
		sprites = sprite;
	}

	@Override
	public void remove(){
		if (this.level.getRandom().nextInt(100) < 5)
			level.addParticle(new SmallFireParticleData(16, false), x, y, z, 0, 0.01, 0);
		super.remove();
	}

	public static Color getColorFromInt(int color) {
		int red = Math.max(color % 255, 0);
		int green = Math.max((color / 255) % 255, 0);
		int blue = Math.max((color / 65025) % 255, 0);
		return new Color(red, green, blue);
	}

	@OnlyIn( Dist.CLIENT )
	public static final class ParticleFactory implements ParticleProvider<LargeBlastDustParticleData>{
		private final SpriteSet spriteSet;

		public ParticleFactory(SpriteSet sprite){
			spriteSet = sprite;
		}

		@Override
		public Particle createParticle(LargeBlastDustParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			LargeBlastDustParticle particle = new LargeBlastDustParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getDuration(), typeIn.getSwirls(), typeIn.getColor(), spriteSet);
			particle.setSpriteFromAge(spriteSet);
			// TODO: Why does setColor invert the red and green colors for this particle?  It should be Red Green Blue
			particle.setColor(particle.color.getGreen(), particle.color.getRed(), particle.color.getBlue());
			return particle;
		}
	}
}
