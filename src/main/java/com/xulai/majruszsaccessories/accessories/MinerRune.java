package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.MiningDurabilityBonus;
import com.xulai.majruszsaccessories.accessories.components.MiningExtraItem;
import com.xulai.majruszsaccessories.accessories.components.MiningSpeedBonus;
import com.xulai.majruszsaccessories.common.AccessoryHandler;

@AutoInstance
public class MinerRune extends AccessoryHandler {
	public MinerRune() {
		super( MajruszsAccessories.MINER_RUNE, MinerRune.class );

		this.add( MiningExtraItem.create( 0.04f ) )
			.add( MiningSpeedBonus.create( 0.12f ) )
			.add( MiningDurabilityBonus.create( 0.12f ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}
}
