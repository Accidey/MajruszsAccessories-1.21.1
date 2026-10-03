package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnPlayerWakedUp;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.SleepingBonuses;
import com.xulai.majruszsaccessories.common.AccessoryHandler;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.events.base.CustomConditions;
import com.xulai.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class DreamCatcher extends AccessoryHandler {
	public DreamCatcher() {
		super( MajruszsAccessories.DREAM_CATCHER, DreamCatcher.class );

		this.add( SleepingBonuses.create( 1, 300 ) )
			.add( SleepingDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.HOUSEHOLD_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class SleepingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.1f;

		public static ISupplier< AccessoryItem > create() {
			return SleepingDropChance::new;
		}

		protected SleepingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnPlayerWakedUp.listen( this::spawnAccessory )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->!data.wasSleepStoppedManually )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.player ) );

			handler.getConfig()
				.define( "sleeping_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnPlayerWakedUp data ) {
			this.spawnFlyingItem( data.getLevel(), data.player.position() );
		}
	}
}
