package com.xulai.majruszsaccessories.boosters.components;

import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.events.OnAccessoryDropChanceGet;
import com.xulai.majruszsaccessories.items.BoosterItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;

public class AccessoryDropChance extends BonusComponent< BoosterItem > {
	RangedFloat multiplier = new RangedFloat().id( "accessory_drop_chance_multiplier" );

	public static ISupplier< BoosterItem > create( float multiplier ) {
		return handler->new AccessoryDropChance( handler, multiplier );
	}

	protected AccessoryDropChance( BonusHandler< BoosterItem > handler, float multiplier ) {
		super( handler );

		this.multiplier.set( multiplier, Range.of( 0.0f, 10.0f ) );

		OnAccessoryDropChanceGet.listen( this::increaseChance );

		this.addTooltip( "majruszsaccessories.boosters.drop_chance", TooltipHelper.asBooster( this::getItem ), TooltipHelper.asFixedPercent( this.multiplier ) );

		this.multiplier.define( handler.getConfig() );
	}

	private void increaseChance( OnAccessoryDropChanceGet data ) {
		data.chance += data.original * this.multiplier.get() * AccessoryHolders.get( data.player ).getBoostersCount( this::getItem );
	}
}
