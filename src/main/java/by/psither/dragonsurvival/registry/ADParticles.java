package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import by.psither.dragonsurvival.client.particles.CaveDragon.LargeBlastDustParticle;
import by.psither.dragonsurvival.client.particles.ForestDragon.SmallConfoundParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.DragonBubbleParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.LargeGlowSlimeParticle;
import by.psither.dragonsurvival.client.particles.SeaDragon.SmallGlowSlimeParticle;
import by.psither.dragonsurvival.common.particles.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@EventBusSubscriber( bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ADParticles{
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, AdditionalDragonsMod.MODID);

	public static final DeferredHolder<ParticleType<?>, ParticleType<LargeBlastDustParticleOption>> LARGE_BLAST_DUST = register("large_blast_dust", () -> LargeBlastDustParticleOption.CODEC, () -> LargeBlastDustParticleOption.STREAM_CODEC);
	public static final DeferredHolder<ParticleType<?>, ParticleType<SmallGlowSlimeParticleOption>> SMALL_GLOWSLIME = register("small_glowslime", () -> SmallGlowSlimeParticleOption.CODEC, () -> SmallGlowSlimeParticleOption.STREAM_CODEC);
	public static final DeferredHolder<ParticleType<?>, ParticleType<LargeGlowSlimeParticleOption>> LARGE_GLOWSLIME = register("large_glowslime", () -> LargeGlowSlimeParticleOption.CODEC, () -> LargeGlowSlimeParticleOption.STREAM_CODEC);
	public static final DeferredHolder<ParticleType<?>, ParticleType<SmallConfoundParticleOption>> SMALL_CONFOUND = register("small_confound", () -> SmallConfoundParticleOption.CODEC, () -> SmallConfoundParticleOption.STREAM_CODEC);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRAGON_BUBBLE = REGISTRY.register("dragon_bubble", () -> new SimpleParticleType(false));

	private static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> register(final String name, final Supplier<MapCodec<T>> codecSupplier, final Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecSupplier) {
		return REGISTRY.register(name, () -> new ParticleType<>(false) {
			@Override
			public @NotNull MapCodec<T> codec() {
				return codecSupplier.get();
			}

			@Override
			public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return streamCodecSupplier.get();
			}
		});
	}

	//Insecure modifications
	@SubscribeEvent( priority = EventPriority.LOWEST)
	public static void registerParticles(RegisterParticleProvidersEvent event){
		event.registerSpriteSet(ADParticles.SMALL_GLOWSLIME.get(), SmallGlowSlimeParticle.Factory::new);
		event.registerSpriteSet(ADParticles.LARGE_GLOWSLIME.get(), LargeGlowSlimeParticle.Factory::new);
		event.registerSpriteSet(ADParticles.LARGE_BLAST_DUST.get(), LargeBlastDustParticle.Factory::new);
		event.registerSpriteSet(ADParticles.SMALL_CONFOUND.get(), SmallConfoundParticle.Factory::new);
		event.registerSpriteSet(ADParticles.DRAGON_BUBBLE.get(), DragonBubbleParticle.Factory::new);
	}
}