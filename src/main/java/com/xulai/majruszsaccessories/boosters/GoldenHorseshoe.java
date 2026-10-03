package com.xulai.majruszsaccessories.boosters;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.boosters.components.BoosterIncompatibility;
import com.xulai.majruszsaccessories.boosters.components.LuckBonus;
import com.xulai.majruszsaccessories.common.BoosterHandler;

@AutoInstance
public class GoldenHorseshoe extends BoosterHandler {
	public GoldenHorseshoe() {
		super( MajruszsAccessories.GOLDEN_HORSESHOE, GoldenHorseshoe.class );

		this.add( LuckBonus.create( 1.5f ) )
			.add( BoosterIncompatibility.create( MajruszsAccessories.HORSESHOE ) );
	}
}
