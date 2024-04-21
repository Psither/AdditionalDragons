package by.psither.dragonsurvival.client.sounds;

import by.psither.dragonsurvival.magic.abilities.Tectonic.CaveDragon.active.BlastBreathAbility;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class BlastBreathSound extends AbstractTickableSoundInstance{
	private final BlastBreathAbility ability;

	public BlastBreathSound(BlastBreathAbility blastBreathAbility){
		super(ADSoundRegistry.blastBreathLoop, SoundSource.PLAYERS, blastBreathAbility.getPlayer().getRandom());

		looping = true;

		this.x = blastBreathAbility.getPlayer().getX();
		this.y = blastBreathAbility.getPlayer().getY();
		this.z = blastBreathAbility.getPlayer().getZ();

		this.ability = blastBreathAbility;
	}

	@Override
	public void tick(){
		if(ability.getPlayer() == null || ability.chargeTime == 0)
			stop();

		this.x = ability.getPlayer().getX();
		this.y = ability.getPlayer().getY();
		this.z = ability.getPlayer().getZ();
	}

	@Override
	public boolean canStartSilent(){
		return true;
	}
}