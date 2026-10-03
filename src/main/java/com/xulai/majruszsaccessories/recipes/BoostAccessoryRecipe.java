package com.xulai.majruszsaccessories.recipes;

import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.events.OnBoosterCompatibilityGet;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BoostAccessoryRecipe extends CustomRecipe {
	public static Supplier< RecipeSerializer< ? > > create() {
		return ()->new SimpleCraftingRecipeSerializer<>( BoostAccessoryRecipe::new );
	}

	public BoostAccessoryRecipe( CraftingBookCategory category ) {
		super( category );
	}

	@Override
	public boolean matches( CraftingInput container, Level level ) {
		RecipeData data = RecipeData.build( container );
		if( data.getAccessoriesSize() != 1 ) {
			return false;
		}

		AccessoryHolder holder = data.getAccessory( 0 );
		return data.getCardsSize() == 0
			&& data.getBoostersSize() > 0
			&& data.getBoostersSize() <= holder.getBoosterSlotsLeft()
			&& BoostAccessoryRecipe.areCompatible( data.boosters(), holder.getBoosters() );
	}

	@Override
	public ItemStack assemble( CraftingInput container, HolderLookup.Provider provider ) {
		RecipeData data = RecipeData.build( container );
		AccessoryHolder holder = data.getAccessory( 0 ).copy();
		data.boosters().forEach( holder::addBooster );

		return holder.getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.BOOST_ACCESSORY_RECIPE.get();
	}

	private static boolean areCompatible( List< BoosterItem > a, List< BoosterItem > b ) {
		List< BoosterItem > items = new ArrayList<>( a );
		items.addAll( b );
		for( int i = 0; i < items.size(); ++i ) {
			for( int j = i + 1; j < items.size(); ++j ) {
				if( Events.dispatch( new OnBoosterCompatibilityGet( items.get( i ), items.get( j ) ) ).areIncompatible() ) {
					return false;
				}
			}
		}

		return true;
	}
}
