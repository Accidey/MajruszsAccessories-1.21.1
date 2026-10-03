package com.xulai.majruszsaccessories.cards;

import com.xulai.majruszlibrary.events.OnItemTooltip;
import com.xulai.majruszlibrary.text.TextHelper;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.items.CardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RemovalCard extends CardItem {
	@Override
	public void apply( AccessoryHolder holder ) {
		if( holder.hasAnyBooster() ) {
			holder.removeBoosters();
		}
	}

	@Override
	public void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.translatable( "majruszsaccessories.cards.remove" ).withStyle( ChatFormatting.GRAY ) );
	}

	@Override
	public List< ItemStack > getCraftingRemainder( AccessoryHolder holder ) {
		return holder.getBoosters().stream().map( ItemStack::new ).toList();
	}
}
