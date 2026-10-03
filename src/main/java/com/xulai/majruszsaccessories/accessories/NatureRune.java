package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.BreedingTwins;
import com.xulai.majruszsaccessories.accessories.components.HarvestingDoubleCrops;
import com.xulai.majruszsaccessories.accessories.components.TamingStrongerAnimals;
import com.xulai.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class NatureRune extends AccessoryHandler {
	public NatureRune() {
		super( MajruszsAccessories.NATURE_RUNE, NatureRune.class );

		this.add( TamingStrongerAnimals.create( 0.25f ) )
			.add( BreedingTwins.create( 0.3f ) )
			.add( HarvestingDoubleCrops.create( 0.3f ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
