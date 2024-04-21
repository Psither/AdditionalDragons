package by.psither.dragonsurvival.client.sounds;

import by.psither.dragonsurvival.magic.abilities.Deepwoods.ForestDragon.active.ConfoundingBreathAbility;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class ConfoundingBreathSound extends AbstractTickableSoundInstance{
	private final ConfoundingBreathAbility ability;

	public ConfoundingBreathSound(ConfoundingBreathAbility confoundingBreathAbility){
		super(ADSoundRegistry.confoundingBreathLoop, SoundSource.PLAYERS, confoundingBreathAbility.getPlayer().getRandom());

		looping = true;

		this.x = confoundingBreathAbility.getPlayer().getX();
		this.y = confoundingBreathAbility.getPlayer().getY();
		this.z = confoundingBreathAbility.getPlayer().getZ();

		this.ability = confoundingBreathAbility;
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