package by.psither.dragonsurvival.common.dragon_types;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.psither.dragonsurvival.common.dragon_types.subtypes.DeepwoodsDragonType;
import by.psither.dragonsurvival.common.dragon_types.subtypes.PrimordialDragonType;
import by.psither.dragonsurvival.common.dragon_types.subtypes.TectonicDragonType;
import by.psither.dragonsurvival.common.dragon_types.types.AstralDragonType;

public class ADDragonTypes {
	public static AstralDragonType ASTRAL;
	public static TectonicDragonType TECTONIC;
	public static PrimordialDragonType PRIMORDIAL;
	public static DeepwoodsDragonType DEEPWOODS;

	public static void registerSubtypes(){
		ASTRAL = DragonTypes.registerType(AstralDragonType::new);
        PRIMORDIAL = DragonTypes.registerSubtype(PrimordialDragonType::new);
        //AdditionalDragonsMod.LOGGER.info("Registered Primordial DragonType as " + PRIMORDIAL);
		DEEPWOODS = DragonTypes.registerSubtype(DeepwoodsDragonType::new);
		//AdditionalDragonsMod.LOGGER.info("Registered Deepwoods DragonType as " + DEEPWOODS);
		TECTONIC = DragonTypes.registerSubtype(TectonicDragonType::new);
		//AdditionalDragonsMod.LOGGER.info("Registered Tectonic DragonType as " + TECTONIC);
	}
}