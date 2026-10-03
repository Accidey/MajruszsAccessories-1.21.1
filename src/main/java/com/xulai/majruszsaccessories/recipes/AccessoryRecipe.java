package com.xulai.majruszsaccessories.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.config.Config;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszlibrary.math.Range;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AccessoryRecipe extends CustomRecipe {
	final AccessoryItem result;
	final List< AccessoryItem > ingredients;

	public static Supplier< RecipeSerializer< ? > > create() {
		return Serializer::new;
	}

	public AccessoryRecipe( CraftingBookCategory category, AccessoryItem result, List< AccessoryItem > ingredients ) {
		super( category );

		this.result = result;
		this.ingredients = ingredients;
	}

	@Override
	public boolean matches( CraftingInput container, Level level ) {
		RecipeData data = RecipeData.build( container );

		return data.getCardsSize() == 0
			&& data.getBoostersSize() == 0
			&& data.getAccessoriesSize() == this.ingredients.size()
			&& this.ingredients.stream().allMatch( data::hasAccessory );
	}

	@Override
	public ItemStack assemble( CraftingInput container, HolderLookup.Provider provider ) {
		RecipeData data = RecipeData.build( container );
		float average = data.getAverageBonus();
		float std = data.getStandardDeviation();
		float minBonus = Config.Efficiency.RANGE.clamp( average - std );
		float maxBonus = Config.Efficiency.RANGE.clamp( average + std );

		return AccessoryHolder.create( this.result ).setBonus( Range.of( minBonus, maxBonus ) ).getItemStack();
	}

	@Override
	public boolean canCraftInDimensions( int width, int height ) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer() {
		return MajruszsAccessories.ACCESSORY_RECIPE.get();
	}

	public static class Serializer implements RecipeSerializer< AccessoryRecipe > {
		public static final MapCodec< AccessoryRecipe > CODEC = RecordCodecBuilder.mapCodec( inst -> inst.group(
			CraftingBookCategory.CODEC.fieldOf( "category" ).orElse( CraftingBookCategory.MISC ).forGetter( AccessoryRecipe::category ),
			BuiltInRegistries.ITEM.byNameCodec().xmap( item->( AccessoryItem )item, item->item ).fieldOf( "result" ).forGetter( recipe->recipe.result ),
			BuiltInRegistries.ITEM.byNameCodec().xmap( item->( AccessoryItem )item, item->item ).listOf().fieldOf( "ingredients" ).forGetter( recipe->recipe.ingredients )
		).apply( inst, AccessoryRecipe::new ) );

		public static final StreamCodec< RegistryFriendlyByteBuf, AccessoryRecipe > STREAM_CODEC = StreamCodec.of( Serializer::toNetwork, Serializer::fromNetwork );

		@Override
		public MapCodec< AccessoryRecipe > codec() {
			return CODEC;
		}

		@Override
		public StreamCodec< RegistryFriendlyByteBuf, AccessoryRecipe > streamCodec() {
			return STREAM_CODEC;
		}

		private static AccessoryRecipe fromNetwork( RegistryFriendlyByteBuf buffer ) {
			CraftingBookCategory category = buffer.readEnum( CraftingBookCategory.class );
			int size = buffer.readVarInt();
			List< AccessoryItem > ingredients = new ArrayList<>();
			for( int idx = 0; idx < size; ++idx ) {
				ingredients.add( ( AccessoryItem )ByteBufCodecs.registry( Registries.ITEM ).decode( buffer ) );
			}

			AccessoryItem result = ( AccessoryItem )ByteBufCodecs.registry( Registries.ITEM ).decode( buffer );
			return new AccessoryRecipe( category, result, ingredients );
		}

		private static void toNetwork( RegistryFriendlyByteBuf buffer, AccessoryRecipe recipe ) {
			buffer.writeEnum( recipe.category() );
			buffer.writeVarInt( recipe.ingredients.size() );
			recipe.ingredients.forEach( ingredient->ByteBufCodecs.registry( Registries.ITEM ).encode( buffer, ingredient ) );
			ByteBufCodecs.registry( Registries.ITEM ).encode( buffer, recipe.result );
		}
	}
}
