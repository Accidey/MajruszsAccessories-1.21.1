package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.events.OnAccessoryCompatibilityGet;
import com.xulai.majruszsaccessories.items.AccessoryItem;

import java.util.function.Supplier;

public class AccessoryIncompatibility extends BonusComponent< AccessoryItem > {
	public static ISupplier< AccessoryItem > create( Supplier< AccessoryItem > accessory ) {
		return handler->new AccessoryIncompatibility( handler, accessory );
	}

	protected AccessoryIncompatibility( BonusHandler< AccessoryItem > handler, Supplier< AccessoryItem > accessory ) {
		super( handler );

		OnAccessoryCompatibilityGet.listen( OnAccessoryCompatibilityGet::makeIncompatible )
			.addCondition( data->{
				return data.a.equals( this.getItem() ) && data.b.equals( accessory.get() )
					|| data.a.equals( accessory.get() ) && data.b.equals( this.getItem() );
			} );
	}
}
