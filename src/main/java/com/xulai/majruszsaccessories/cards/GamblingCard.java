package com.xulai.majruszsaccessories.cards;

import com.xulai.majruszlibrary.events.OnItemTooltip;
import com.xulai.majruszlibrary.text.TextHelper;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.config.Config;
import com.xulai.majruszsaccessories.items.CardItem;
import net.minecraft.ChatFormatting;

public class GamblingCard extends CardItem {
	@Override
	public void apply( AccessoryHolder holder ) {
		if( holder.hasBonusRangeDefined() ) {
			holder.removeBonus();
		} else {
			holder.setBonus( Config.Efficiency.RANGE, AccessoryHolder.RandomType.NORMAL_DISTRIBUTION );
		}
	}

	@Override
	public void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.translatable( "majruszsaccessories.cards.redraw" ).withStyle( ChatFormatting.GRAY ) );
	}
}
