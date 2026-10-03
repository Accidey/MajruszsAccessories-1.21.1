package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.FishingExtraTreasure;
import com.xulai.majruszsaccessories.accessories.components.FishingLuckBonus;
import com.xulai.majruszsaccessories.accessories.components.FishingLureBonus;
import com.xulai.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class AnglerRune extends AccessoryHandler {
	public AnglerRune() {
		super( MajruszsAccessories.ANGLER_RUNE, AnglerRune.class );

		this.add( FishingLuckBonus.create( 3.0f ) )
			.add( FishingLureBonus.create( 0.25f ) )
			.add( FishingExtraTreasure.create( 0.06f ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
