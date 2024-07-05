package by.psither.dragonsurvival.client.particles.SeaDragon;

import by.dragonsurvivalteam.dragonsurvival.client.particles.dragon.DragonParticle;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class LargeGlowSlimeParticle extends DragonParticle {
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

	public static class Type extends ParticleType<Data> {
		protected Type(boolean pOverrideLimitter) {
			super(pOverrideLimitter);
		}

		@Override
		public @NotNull MapCodec<Data> codec() {
			return Data.CODEC;
		}

		@Override
		public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, Data> streamCodec() {
			return Data.STREAM_CODEC;
		}
	}

	public record Data(float duration, boolean swirls) implements ParticleOptions {
		public static MapCodec<Data> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder.group(Codec.FLOAT.fieldOf("duration").forGetter(Data::duration), Codec.BOOL.fieldOf("swirls").forGetter(Data::swirls)).apply(codecBuilder, Data::new));

		public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.FLOAT,
				Data::duration,
				ByteBufCodecs.BOOL,
				Data::swirls,
				Data::new
		);

		public static final ParticleType<Data> TYPE = new Type(false);

		@Override
		public float duration() {
			return duration;
		}

		@Override
		public boolean swirls() {
			return swirls;
		}

		@Override
		public @NotNull ParticleType<?> getType() {
			return TYPE;
		}
	}

    @OnlyIn( Dist.CLIENT )
	public static final class Factory implements ParticleProvider<Data>{
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite){
			spriteSet = sprite;
		}

		@Override
		public Particle createParticle(Data typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			LargeGlowSlimeParticle particle = new LargeGlowSlimeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.duration(), typeIn.swirls(), spriteSet);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
