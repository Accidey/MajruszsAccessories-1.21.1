package com.xulai.majruszsaccessories.items;

import com.xulai.majruszsaccessories.common.AccessoryHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class AccessoryItem extends Item {
	private final int tier;
	private final int boosterSlotsCount;

	public static Supplier< AccessoryItem > tier1() {
		return ()->new AccessoryItem( 1 );
	}

	public static Supplier< AccessoryItem > tier2() {
		return ()->new AccessoryItem( 2 );
	}

	public static Supplier< AccessoryItem > tier3() {
		return ()->new AccessoryItem( 3 );
	}

	protected AccessoryItem( int tier ) {
		super( new Properties().stacksTo( 1 ) );

		this.tier = tier;
		this.boosterSlotsCount = tier;
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return AccessoryHolder.getOrCreate( itemStack ).hasMaxBonus();
	}

	public int getTier() {
		return this.tier;
	}

	public int getBoosterSlotsCount() {
		return this.boosterSlotsCount;
	}
}