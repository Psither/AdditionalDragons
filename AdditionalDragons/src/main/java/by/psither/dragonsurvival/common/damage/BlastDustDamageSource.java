package by.psither.dragonsurvival.common.damage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BlastDustDamageSource extends DamageSource {
	Entity source;

	public BlastDustDamageSource(Entity source2) {
		super("blast_dust");
		this.source = source2;
	}

	public BlastDustDamageSource() {
		super("blast_dust");
	}
}
