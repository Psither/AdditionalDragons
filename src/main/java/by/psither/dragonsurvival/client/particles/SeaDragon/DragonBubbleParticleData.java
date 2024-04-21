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

public class DragonBubbleParticleData implements ParticleOptions {
	public static final Deserializer<DragonBubbleParticleData> DESERIALIZER = new Deserializer<DragonBubbleParticleData>(){
		@Override
		public DragonBubbleParticleData fromCommand(ParticleType<DragonBubbleParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			float duration = (float)reader.readDouble();
			return new DragonBubbleParticleData(duration);
		}

		@Override
		public DragonBubbleParticleData fromNetwork(ParticleType<DragonBubbleParticleData> particleTypeIn, FriendlyByteBuf buffer){
			return new DragonBubbleParticleData(buffer.readFloat());
		}
	};
	
	private final float duration;

	public static Codec<DragonBubbleParticleData> CODEC(ParticleType<DragonBubbleParticleData> particleType){
		return RecordCodecBuilder.create(codecBuilder -> codecBuilder.group(Codec.FLOAT.fieldOf("duration").forGetter(DragonBubbleParticleData::getDuration)).apply(codecBuilder, DragonBubbleParticleData::new));
	}

	public DragonBubbleParticleData(float duration){
		this.duration = duration;
	}
	
	public float getDuration() {
		return this.duration;
	}

	@Override
	public ParticleType<?> getType() {
		return ADParticles.dragonBubbleParticle;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer){
		buffer.writeFloat(duration);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public String writeToString(){
		return String.format(Locale.ROOT, "%s %.2f", Registry.PARTICLE_TYPE.getKey(getType()), duration);
	}
}
