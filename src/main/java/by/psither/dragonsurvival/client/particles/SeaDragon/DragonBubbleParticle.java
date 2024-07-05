package by.psither.dragonsurvival.client.particles.SeaDragon;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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

	public static class Data implements ParticleOptions {
		public static MapCodec<Data> CODEC = MapCodec.unit(new Data());

		public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = StreamCodec.unit(new Data());

		public static final ParticleType<Data> TYPE = new Type(false);

		@Override
		public @NotNull ParticleType<?> getType() {
			return TYPE;
		}
	}

	@OnlyIn( Dist.CLIENT )
	public static final class Factory implements ParticleProvider<Data> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite){
			spriteSet = sprite;
		}

		@Override
		public Particle createParticle(Data typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			DragonBubbleParticle particle = new DragonBubbleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setSpriteFromAge(spriteSet);
			return particle;
		}
	}
}
