package com.xulai.majruszsaccessories.listeners;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.events.OnItemTooltip;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszlibrary.text.TextHelper;
import com.xulai.majruszsaccessories.events.OnBoosterTooltip;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

@AutoInstance
public class BoosterTooltipUpdater {
	public BoosterTooltipUpdater() {
		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.itemStack.getItem() instanceof BoosterItem ) );
	}

	private void addTooltip( OnItemTooltip data ) {
		data.components.addAll( this.buildGenericInfo() );
		data.components.addAll( this.buildEffectsInfo( data ) );
	}

	private List< Component > buildGenericInfo() {
		return List.of( TextHelper.translatable( Tooltips.INFO ).withStyle( ChatFormatting.GOLD ) );
	}

	private List< Component > buildEffectsInfo( OnItemTooltip data ) {
		return Events.dispatch( new OnBoosterTooltip( ( BoosterItem )data.itemStack.getItem() ) ).components;
	}

	static final class Tooltips {
		static final String INFO = "majruszsaccessories.items.booster_tooltip";
	}
}
