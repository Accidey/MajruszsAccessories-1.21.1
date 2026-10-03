package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.events.OnTradesUpdated;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszlibrary.platform.Side;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.item.trading.MerchantOffer;

public class TradingDiscount extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 1.0f ) );

	public static ISupplier< AccessoryItem > create( float multiplier ) {
		return handler->new TradingDiscount( handler, multiplier );
	}

	protected TradingDiscount( BonusHandler< AccessoryItem > handler, float multiplier ) {
		super( handler );

		this.multiplier.set( multiplier, Range.of( 0.0f, 1.0f ) );

		OnTradesUpdated.listen( this::decreasePrices );

		this.addTooltip( "majruszsaccessories.bonuses.trading_discount", TooltipHelper.asPercent( this.multiplier ) );

		handler.getConfig()
			.define( "trading_discount", this.multiplier::define );
	}

	private void decreasePrices( OnTradesUpdated data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		float discount = holder.apply( this.multiplier );
		for( MerchantOffer offer : data.offers ) {
			int price = offer.getBaseCostA().getCount();
			float currentDiscount = ( float )offer.getSpecialPriceDiff() / price;
			offer.addToSpecialPriceDiff( Math.round( -( 1.0f + currentDiscount ) * discount * price ) );
		}
		if( Side.isLogicalServer() ) {
			this.spawnEffects( data, holder );
		}
	}

	private void spawnEffects( OnTradesUpdated data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 6 )
			.sizeBased( data.villager )
			.emit( data.getServerLevel() );
	}
}
