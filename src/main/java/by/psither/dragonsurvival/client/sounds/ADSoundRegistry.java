package by.psither.dragonsurvival.client.sounds;

import by.psither.dragonsurvival.AdditionalDragonsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ADSoundRegistry {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, AdditionalDragonsMod.MODID);
	public static SoundEvent luminousBreathStart, luminousBreathLoop, luminousBreathEnd;
	public static SoundEvent bugZapper;
	public static SoundEvent blastBreathStart, blastBreathLoop, blastBreathEnd;
	public static SoundEvent confoundingBreathStart, confoundingBreathLoop, confoundingBreathEnd;
	
	public static void register() {
		luminousBreathStart = register("luminous_breath_start");
		luminousBreathLoop = register("luminous_breath_loop");
		luminousBreathEnd = register("luminous_breath_end");
		blastBreathStart = register("blast_breath_start");
		blastBreathLoop = register("blast_breath_loop");
		blastBreathEnd = register("blast_breath_end");
		confoundingBreathStart = register("confounding_breath_start");
		confoundingBreathLoop = register("confounding_breath_loop");
		confoundingBreathEnd = register("confounding_breath_end");
		bugZapper = register("bug_zapper");
	}
	
	private static SoundEvent register(String name){
		SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(AdditionalDragonsMod.MODID, name));
		SOUNDS.register(name, ()->soundEvent);
		return soundEvent;
	}
}
