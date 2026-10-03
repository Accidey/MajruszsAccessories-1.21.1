package com.xulai.majruszsaccessories.boosters;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.xulai.majruszsaccessories.boosters.components.BoosterIncompatibility;
import com.xulai.majruszsaccessories.common.BoosterHandler;

@AutoInstance
public class GoldenDice extends BoosterHandler {
	public GoldenDice() {
		super( MajruszsAccessories.GOLDEN_DICE, GoldenDice.class );

		this.add( AccessoryDropChance.create( 0.3f ) )
			.add( BoosterIncompatibility.create( MajruszsAccessories.DICE ) );
	}
}
