package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.MoreChestLoot;
import com.xulai.majruszsaccessories.common.AccessoryHandler;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.events.base.CustomConditions;
import com.xulai.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class AdventurerKit extends AccessoryHandler {
	public AdventurerKit() {
		super( MajruszsAccessories.ADVENTURER_KIT, AdventurerKit.class );

		this.add( MoreChestLoot.create( 1.2f ) )
			.add( AnyChestDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.ADVENTURER_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class AnyChestDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.025f;

		public static ISupplier< AccessoryItem > create() {
			return AnyChestDropChance::new;
		}

		protected AnyChestDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			MoreChestLoot.OnChestOpened.listen( this::addToGeneratedLoot )
				.addCondition( CustomConditions.dropChance( ()->this.chance, data->MoreChestLoot.OnChestOpened.findPlayer( data ).orElse( null ) ) );

			handler.getConfig()
				.define( "any_chest_spawn_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
