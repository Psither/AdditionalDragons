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

public record LargeGlowSlimeParticleOption(float duration, boolean swirls) implements ParticleOptions {
    public static final MapCodec<LargeGlowSlimeParticleOption> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder.group(
            Codec.FLOAT.fieldOf("duration").forGetter(LargeGlowSlimeParticleOption::duration),
            Codec.BOOL.fieldOf("swirls").forGetter(LargeGlowSlimeParticleOption::swirls)
    ).apply(codecBuilder, LargeGlowSlimeParticleOption::new));

    public static final StreamCodec<ByteBuf, LargeGlowSlimeParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, LargeGlowSlimeParticleOption::duration,
            ByteBufCodecs.BOOL, LargeGlowSlimeParticleOption::swirls,
            LargeGlowSlimeParticleOption::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return ADParticles.LARGE_GLOWSLIME.value();
    }
}
