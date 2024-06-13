package by.psither.dragonsurvival.common.dragon_types;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.psither.dragonsurvival.common.dragon_types.types.DeepwoodsDragonType;
import by.psither.dragonsurvival.common.dragon_types.types.PrimordialDragonType;
import by.psither.dragonsurvival.common.dragon_types.types.TectonicDragonType;

public class ADDragonTypes {
	public static TectonicDragonType TECTONIC;
	public static PrimordialDragonType PRIMORDIAL;
	public static DeepwoodsDragonType DEEPWOODS;

	public static void registerSubtypes(){
        PRIMORDIAL = DragonTypes.registerSubtype(PrimordialDragonType::new);
        //AdditionalDragonsMod.LOGGER.info("Registered Primordial DragonType as " + PRIMORDIAL);
		DEEPWOODS = DragonTypes.registerSubtype(DeepwoodsDragonType::new);
		//AdditionalDragonsMod.LOGGER.info("Registered Deepwoods DragonType as " + DEEPWOODS);
		TECTONIC = DragonTypes.registerSubtype(TectonicDragonType::new);
		//AdditionalDragonsMod.LOGGER.info("Registered Tectonic DragonType as " + TECTONIC);
	}
}