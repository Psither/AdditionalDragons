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

public record SmallGlowSlimeParticleOption(float duration, boolean swirls) implements ParticleOptions {
    public static final MapCodec<SmallGlowSlimeParticleOption> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder.group(
            Codec.FLOAT.fieldOf("duration").forGetter(SmallGlowSlimeParticleOption::duration),
            Codec.BOOL.fieldOf("swirls").forGetter(SmallGlowSlimeParticleOption::swirls)
    ).apply(codecBuilder, SmallGlowSlimeParticleOption::new));

    public static final StreamCodec<ByteBuf, SmallGlowSlimeParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SmallGlowSlimeParticleOption::duration,
            ByteBufCodecs.BOOL, SmallGlowSlimeParticleOption::swirls,
            SmallGlowSlimeParticleOption::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return ADParticles.SMALL_GLOWSLIME.value();
    }
}
