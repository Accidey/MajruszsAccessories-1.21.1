package com.xulai.majruszsaccessories.common.components;

import com.xulai.majruszlibrary.events.OnWanderingTradesUpdated;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.List;

public class TradeOffer< Type extends Item > extends BonusComponent< Type > {
	private static final List< TradeOffer< ? > > OFFERS = new ArrayList<>();

	static {
		OnWanderingTradesUpdated.listen( TradeOffer::addTrades );
	}

	public static < Type extends Item > ISupplier< Type > create() {
		return TradeOffer::new;
	}

	protected TradeOffer( BonusHandler< Type > handler ) {
		super( handler );

		OFFERS.add( this );
	}

	public MerchantOffer toMerchantOffer() {
		return new MerchantOffer( new ItemCost( this.getItem() ), this.getItemStack(), 2, 40, 0.05f ) {
			@Override
			public boolean satisfiedBy( ItemStack itemStack1, ItemStack itemStack2 ) {
				return itemStack1.is( TradeOffer.this.getItem() ) && itemStack2.isEmpty();
			}
		};
	}

	private ItemStack getItemStack() {
		return Random.next( List.of(
			new ItemStack( MajruszsAccessories.GAMBLING_CARD.get(), 1 ),
			new ItemStack( MajruszsAccessories.GAMBLING_CARD.get(), 1 ),
			new ItemStack( MajruszsAccessories.REMOVAL_CARD.get(), 1 ),
			new ItemStack( MajruszsAccessories.REVERSE_CARD.get(), 1 ),
			new ItemStack( Items.EMERALD, 7 )
		) );
	}

	private static void addTrades( OnWanderingTradesUpdated data ) {
		data.offers.addAll( Random.next( OFFERS.stream().map( TradeOffer::toMerchantOffer ).toList(), 5 ) );
	}
}
