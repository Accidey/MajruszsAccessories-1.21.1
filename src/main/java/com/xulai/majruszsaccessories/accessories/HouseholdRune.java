package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.SleepingBonuses;
import com.xulai.majruszsaccessories.accessories.components.StrongerPotions;
import com.xulai.majruszsaccessories.accessories.components.TradingDiscount;
import com.xulai.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class HouseholdRune extends AccessoryHandler {
	public HouseholdRune() {
		super( MajruszsAccessories.HOUSEHOLD_RUNE, HouseholdRune.class );

		this.add( TradingDiscount.create( 0.15f ) )
			.add( SleepingBonuses.create( 1.2f, 360 ) )
			.add( StrongerPotions.create( 0.5f, 1.2f ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
