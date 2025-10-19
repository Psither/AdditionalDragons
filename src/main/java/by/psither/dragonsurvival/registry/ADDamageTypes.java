package by.psither.dragonsurvival.registry;

import net.minecraft.world.damagesource.DamageSource;

public class ADDamageTypes {
	public static final DamageSource BLAST_DUST = new DamageSource("blast_dust");
	public static final DamageSource PYRO_ROAR = new DamageSource("pyro_roar").setIsFire();
	public static final DamageSource MIRROR_CURSE = new DamageSource("mirror_curse").bypassInvul().bypassMagic().bypassArmor();
}
