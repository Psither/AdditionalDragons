package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ADDamageTypes {
	public static final ResourceKey<DamageType> BLAST_DUST = createKey("blast_dust");
	public static final ResourceKey<DamageType> PYRO_ROAR = createKey("pyro_roar");
	public static final ResourceKey<DamageType> MIRROR_CURSE = createKey("mirror_curse");

    public static DamageSource damageSource(final Level level, final ResourceKey<DamageType> damageType) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType));
    }

    public static DamageSource entityDamageSource(final Level level, final ResourceKey<DamageType> damageType, final Entity entity) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType), entity);
    }

    private static ResourceKey<DamageType> createKey(final String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(AdditionalDragonsMod.MODID, name));
    }
}
