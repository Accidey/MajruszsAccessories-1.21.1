package com.xulai.majruszsaccessories.cards;

import com.xulai.majruszlibrary.events.OnItemTooltip;
import com.xulai.majruszlibrary.text.TextHelper;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.items.CardItem;
import net.minecraft.ChatFormatting;

public class ReverseCard extends CardItem {
	@Override
	public void apply( AccessoryHolder holder ) {
		holder.setBonus( -holder.getBaseBonus() );
	}

	@Override
	public void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.translatable( "majruszsaccessories.cards.negate" ).withStyle( ChatFormatting.GRAY ) );
	}
}
