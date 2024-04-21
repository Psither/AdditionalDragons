package by.psither.dragonsurvival.client.particles.CaveDragon;

import java.awt.Color;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LargeBlastDustParticle extends TextureSheetParticle {
	private final float spread;
	private final SpriteSet sprites;
	private final Color color;
	boolean swirls;
	private int swirlTick;

	public LargeBlastDustParticle(ClientLevel world, double x, double y, double z, double vX, double vY, double vZ, double duration, boolean swirls, int color, SpriteSet sprite){
		super(world, x, y, z);
		setSize(1, 1);
		xd = vX;
		yd = vY;
		zd = vZ;
		lifetime = (int)duration;
		swirlTick = random.nextInt(120);
		spread = random.nextFloat();
		hasPhysics = true;
		this.swirls = swirls;
		this.color = getColorFromInt(color);
		setSpriteFromAge(sprite);
		sprites = sprite;
	}

	@Override
	protected float getU1(){
		return super.getU1() - (super.getU1() - super.getU0()) / 8f;
	}

	@Override
	protected float getV1(){
		return super.getV1() - (super.getV1() - super.getV0()) / 8f;
	}

	public static Color getColorFromInt(int color) {
		int red = Math.max(color % 255, 0);
		int green = Math.max((color / 255) % 255, 0);
		int blue = Math.max((color / 65025) % 255, 0);
		return new Color(red, green, blue);
	}

	@Override
	public void tick(){
		super.tick();

		if(swirls){
			Vector3f motionVec = new Vector3f((float)xd, (float)yd, (float)zd);
			motionVec.normalize();
			float yaw = (float)Math.atan2(motionVec.x(), motionVec.z());
			float pitch = (float)Math.atan2(motionVec.y(), 1);
			float swirlRadius = 1f * (age / (float)lifetime) * spread;
			Quaternion quatSpin = motionVec.rotation(swirlTick * 0.2f);
			Quaternion quatOrient = new Quaternion(pitch, yaw, 0, false);
			Vector3f vec = new Vector3f(swirlRadius, 0, 0);
			vec.transform(quatOrient);
			vec.transform(quatSpin);
			x += vec.x();
			y += vec.y();
			z += vec.z();
		} else {
			y += 10 * (age / lifetime);
		}

		if(age >= lifetime){
			remove();
		}
		age++;
		swirlTick++;
		setSpriteFromAge(sprites);
	}

	@Override
	public ParticleRenderType getRenderType(){
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
