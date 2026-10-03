package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnAnimalTamed;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.TamingStrongerAnimals;
import com.xulai.majruszsaccessories.common.AccessoryHandler;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.events.base.CustomConditions;
import com.xulai.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class CertificateOfTaming extends AccessoryHandler {
	public CertificateOfTaming() {
		super( MajruszsAccessories.CERTIFICATE_OF_TAMING, CertificateOfTaming.class );

		this.add( TamingStrongerAnimals.create( 0.2f ) )
			.add( TamingDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.NATURE_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class TamingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.1f;

		public static ISupplier< AccessoryItem > create() {
			return TamingDropChance::new;
		}

		protected TamingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnAnimalTamed.listen( this::spawnCertificate )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->data.tamer ) );

			handler.getConfig()
				.define( "taming_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnCertificate( OnAnimalTamed data ) {
			this.spawnFlyingItem( data.getLevel(), data.animal.position(), data.tamer.position() );
		}
	}
}
