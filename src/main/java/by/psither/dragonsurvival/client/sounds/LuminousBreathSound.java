package by.psither.dragonsurvival.client.sounds;

import by.psither.dragonsurvival.magic.abilities.Primordial.SeaDragon.active.LuminousBreathAbility;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class LuminousBreathSound extends AbstractTickableSoundInstance{
	private final LuminousBreathAbility ability;

	public LuminousBreathSound(LuminousBreathAbility luminousBreathAbility){
		super(ADSoundRegistry.luminousBreathLoop, SoundSource.PLAYERS, luminousBreathAbility.getPlayer().getRandom());

		looping = true;

		this.x = luminousBreathAbility.getPlayer().getX();
		this.y = luminousBreathAbility.getPlayer().getY();
		this.z = luminousBreathAbility.getPlayer().getZ();

		this.ability = luminousBreathAbility;
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