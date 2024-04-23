package by.psither.dragonsurvival.client.particles.SeaDragon;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import by.psither.dragonsurvival.client.particles.ADParticles;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class LargeGlowSlimeParticleData implements ParticleOptions {
	@SuppressWarnings("deprecation")
	public static final Deserializer<LargeGlowSlimeParticleData> DESERIALIZER = new Deserializer<LargeGlowSlimeParticleData>(){
		@Override
		public LargeGlowSlimeParticleData fromCommand(ParticleType<LargeGlowSlimeParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			float duration = (float)reader.readDouble();
			reader.expect(' ');
			boolean swirls = reader.readBoolean();
			return new LargeGlowSlimeParticleData(duration, swirls);
		}

		@Override
		public LargeGlowSlimeParticleData fromNetwork(ParticleType<LargeGlowSlimeParticleData> particleTypeIn, FriendlyByteBuf buffer){
			return new LargeGlowSlimeParticleData(buffer.readFloat(), buffer.readBoolean());
		}
	};

	private final float duration;
	private final boolean swirls;
	
	public static Codec<LargeGlowSlimeParticleData> CODEC(ParticleType<LargeGlowSlimeParticleData> particleType){
		return RecordCodecBuilder.create(codecBuilder -> codecBuilder.group(Codec.FLOAT.fieldOf("duration").forGetter(LargeGlowSlimeParticleData::getDuration), Codec.BOOL.fieldOf("swirls").forGetter(LargeGlowSlimeParticleData::getSwirls)).apply(codecBuilder, LargeGlowSlimeParticleData::new));
	}

	public LargeGlowSlimeParticleData(float duration, boolean spins){
		this.duration = duration;
		swirls = spins;
	}
	
	public float getDuration(){
		return duration;
	}

	@OnlyIn( Dist.CLIENT )
	public boolean getSwirls(){
		return swirls;
	}
	
	@Override
	public ParticleType<?> getType() {
		return ADParticles.LARGE_GLOWSLIME.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer){
		buffer.writeFloat(duration);
		buffer.writeBoolean(swirls);
	}

	@Override
	public String writeToString(){
		return String.format(Locale.ROOT, "%s %.2f %b", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), duration, swirls);
	}
}
