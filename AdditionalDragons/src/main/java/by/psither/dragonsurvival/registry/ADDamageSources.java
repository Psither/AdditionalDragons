package by.psither.dragonsurvival.registry;

import by.psither.dragonsurvival.common.damage.BlastDustDamageSource;
import net.minecraft.world.damagesource.DamageSource;

public class ADDamageSources {
	public static final DamageSource BLAST_DUST = new DamageSource("blastDust").setExplosion();
	public static final DamageSource PYRO_ROAR = new DamageSource("pyroRoar").setExplosion();
	public static final DamageSource MIRROR_CURSE = new DamageSource("mirrorCurse").bypassArmor().bypassEnchantments().bypassInvul().bypassMagic();
}
