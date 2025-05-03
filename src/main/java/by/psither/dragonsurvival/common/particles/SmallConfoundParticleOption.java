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

public record SmallConfoundParticleOption(float duration, boolean swirls) implements ParticleOptions {
    public static final MapCodec<SmallConfoundParticleOption> CODEC = RecordCodecBuilder.mapCodec(codecBuilder -> codecBuilder.group(
            Codec.FLOAT.fieldOf("duration").forGetter(SmallConfoundParticleOption::duration),
            Codec.BOOL.fieldOf("swirls").forGetter(SmallConfoundParticleOption::swirls)
    ).apply(codecBuilder, SmallConfoundParticleOption::new));

    public static final StreamCodec<ByteBuf, SmallConfoundParticleOption> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SmallConfoundParticleOption::duration,
            ByteBufCodecs.BOOL, SmallConfoundParticleOption::swirls,
            SmallConfoundParticleOption::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return ADParticles.SMALL_GLOWSLIME.value();
    }
}
