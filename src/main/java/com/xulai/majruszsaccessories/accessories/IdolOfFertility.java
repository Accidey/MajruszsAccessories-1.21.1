package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnBabySpawned;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.BreedingTwins;
import com.xulai.majruszsaccessories.common.AccessoryHandler;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.events.base.CustomConditions;
import com.xulai.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class IdolOfFertility extends AccessoryHandler {
	public IdolOfFertility() {
		super( MajruszsAccessories.IDOL_OF_FERTILITY, IdolOfFertility.class );

		this.add( BreedingTwins.create( 0.25f ) )
			.add( BreedingDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.NATURE_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class BreedingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.01f;

		public static ISupplier< AccessoryItem > create() {
			return BreedingDropChance::new;
		}

		protected BreedingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnBabySpawned.listen( this::spawnTotem )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.predicate( data->data.player != null ) )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.player ) );

			handler.getConfig()
				.define( "breeding_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnTotem( OnBabySpawned data ) {
			this.spawnFlyingItem( data.getLevel(), data.parentA.position(), data.parentB.position() );
		}
	}
}
