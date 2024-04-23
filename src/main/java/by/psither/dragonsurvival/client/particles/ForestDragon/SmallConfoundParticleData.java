package by.psither.dragonsurvival.client.particles.ForestDragon;

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

public class SmallConfoundParticleData implements ParticleOptions {
	public static final Deserializer<SmallConfoundParticleData> DESERIALIZER = new Deserializer<SmallConfoundParticleData>(){
		@Override
		public SmallConfoundParticleData fromCommand(ParticleType<SmallConfoundParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			float duration = (float)reader.readDouble();
			reader.expect(' ');
			boolean swirls = reader.readBoolean();
			return new SmallConfoundParticleData(duration, swirls);
		}

		@Override
		public SmallConfoundParticleData fromNetwork(ParticleType<SmallConfoundParticleData> particleTypeIn, FriendlyByteBuf buffer){
			return new SmallConfoundParticleData(buffer.readFloat(), buffer.readBoolean());
		}
	};

	private final float duration;
	private final boolean swirls;
	
	public static Codec<SmallConfoundParticleData> CODEC(ParticleType<SmallConfoundParticleData> particleType){
		return RecordCodecBuilder.create(codecBuilder -> codecBuilder.group(Codec.FLOAT.fieldOf("duration").forGetter(SmallConfoundParticleData::getDuration), Codec.BOOL.fieldOf("swirls").forGetter(SmallConfoundParticleData::getSwirls)).apply(codecBuilder, SmallConfoundParticleData::new));
	}

	public SmallConfoundParticleData(float duration, boolean spins){
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
		return ADParticles.SMALL_CONFOUND.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer){
		buffer.writeFloat(duration);
		buffer.writeBoolean(swirls);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public String writeToString(){
		return String.format(Locale.ROOT, "%s %.2f %b", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), duration, swirls);
	}
}
