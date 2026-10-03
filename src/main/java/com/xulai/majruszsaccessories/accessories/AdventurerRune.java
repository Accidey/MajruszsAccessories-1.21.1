package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.BrushingExtraItem;
import com.xulai.majruszsaccessories.accessories.components.MoreChestLoot;
import com.xulai.majruszsaccessories.accessories.components.SwimmingSpeedBonus;
import com.xulai.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class AdventurerRune extends AccessoryHandler {
	public AdventurerRune() {
		super( MajruszsAccessories.ADVENTURER_RUNE, AdventurerRune.class );

		this.add( MoreChestLoot.create( 1.5f ) )
			.add( BrushingExtraItem.create( 0.2f ) )
			.add( SwimmingSpeedBonus.create( 0.25f ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
