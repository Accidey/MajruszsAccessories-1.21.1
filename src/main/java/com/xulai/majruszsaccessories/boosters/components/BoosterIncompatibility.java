package com.xulai.majruszsaccessories.boosters.components;

import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.events.OnBoosterCompatibilityGet;
import com.xulai.majruszsaccessories.items.BoosterItem;

import java.util.function.Supplier;

public class BoosterIncompatibility extends BonusComponent< BoosterItem > {
	public static ISupplier< BoosterItem > create( Supplier< BoosterItem > booster ) {
		return handler->new BoosterIncompatibility( handler, booster );
	}

	protected BoosterIncompatibility( BonusHandler< BoosterItem > handler, Supplier< BoosterItem > booster ) {
		super( handler );

		OnBoosterCompatibilityGet.listen( OnBoosterCompatibilityGet::makeIncompatible )
			.addCondition( data->{
				return data.a.equals( this.getItem() ) && data.b.equals( booster.get() )
					|| data.a.equals( booster.get() ) && data.b.equals( this.getItem() );
			} );
	}
}
