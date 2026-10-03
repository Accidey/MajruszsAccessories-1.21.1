package com.xulai.majruszsaccessories.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BoosterItem extends Item {
	final Rarity rarity;

	public static Supplier< BoosterItem > basic() {
		return ()->new BoosterItem( Rarity.UNCOMMON );
	}

	public static Supplier< BoosterItem > rare() {
		return ()->new BoosterItem( Rarity.RARE );
	}

	private BoosterItem( Rarity rarity ) {
		super( new Properties().stacksTo( 1 ).rarity( rarity ) );

		this.rarity = rarity;
	}

	public Rarity getRarity() {
		return this.rarity;
	}

	@Override
	public boolean isFoil( ItemStack itemStack ) {
		return true;
	}

}