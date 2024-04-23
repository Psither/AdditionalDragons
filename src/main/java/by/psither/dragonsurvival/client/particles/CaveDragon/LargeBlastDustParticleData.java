package by.psither.dragonsurvival.client.particles.CaveDragon;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import by.dragonsurvivalteam.dragonsurvival.client.particles.CaveDragon.LargeFireParticleData;
import by.psither.dragonsurvival.client.particles.ADParticles;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class LargeBlastDustParticleData implements ParticleOptions {
	public static final Deserializer<LargeBlastDustParticleData> DESERIALIZER = new Deserializer<LargeBlastDustParticleData>(){
		@Override
		public LargeBlastDustParticleData fromCommand(ParticleType<LargeBlastDustParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException{
			reader.expect(' ');
			float duration = (float)reader.readDouble();
			reader.expect(' ');
			boolean swirls = reader.readBoolean();
			reader.expect(' ');
			int color = reader.readInt();
			return new LargeBlastDustParticleData(duration, swirls, color);
		}

		@Override
		public LargeBlastDustParticleData fromNetwork(ParticleType<LargeBlastDustParticleData> particleTypeIn, FriendlyByteBuf buffer){
			return new LargeBlastDustParticleData(buffer.readFloat(), buffer.readBoolean(), buffer.readInt());
		}
	};

	private final float duration;
	private final boolean swirls;
	private final int color;
	
	public static Codec<LargeBlastDustParticleData> CODEC(ParticleType<LargeBlastDustParticleData> particleType){
		return RecordCodecBuilder.create(codecBuilder -> codecBuilder.group(Codec.FLOAT.fieldOf("duration").forGetter(LargeBlastDustParticleData::getDuration), Codec.BOOL.fieldOf("swirls").forGetter(LargeBlastDustParticleData::getSwirls), Codec.INT.fieldOf("color").forGetter(LargeBlastDustParticleData::getColor)).apply(codecBuilder, LargeBlastDustParticleData::new));
	}

	public LargeBlastDustParticleData(float duration, boolean spins, int color){
		this.duration = duration;
		swirls = spins;
		this.color = color;
	}

	public float getDuration(){
		return duration;
	}


	@OnlyIn( Dist.CLIENT )
	public boolean getSwirls(){
		return swirls;
	}
	
	public int getColor() {
		return color;
	}

	@Override
	public ParticleType<?> getType() {
		return ADParticles.LARGE_BLAST_DUST.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer){
		buffer.writeFloat(duration);
		buffer.writeBoolean(swirls);
		buffer.writeInt(color);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public String writeToString(){
		return String.format(Locale.ROOT, "%s %.2f %b %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), duration, swirls, color);
	}
}
