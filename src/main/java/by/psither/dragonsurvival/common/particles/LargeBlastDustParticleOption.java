package by.psither.dragonsurvival.common.particles;

import by.psither.dragonsurvival.registry.ADParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record LargeBlastDustParticleOption(float duration, boolean swirls, int color) implements ParticleOptions {
    public static final MapCodec<LargeBlastDustParticleOption> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder.group(
            Codec.FLOAT.fieldOf("duration").forGetter(LargeBlastDustParticleOption::duration),
            Codec.BOOL.fieldOf("swirls").forGetter(LargeBlastDustParticleOption::swirls),
            Codec.INT.fieldOf("color").forGetter(LargeBlastDustParticleOption::color)
    ).apply(codecBuilder, LargeBlastDustParticleOption::new));

    public static final StreamCodec<ByteBuf, LargeBlastDustParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, LargeBlastDustParticleOption::duration,
            ByteBufCodecs.BOOL, LargeBlastDustParticleOption::swirls,
            ByteBufCodecs.INT, LargeBlastDustParticleOption::color,
            LargeBlastDustParticleOption::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return ADParticles.LARGE_BLAST_DUST.value();
    }
}
