package com.xulai.majruszsaccessories.events;

import com.xulai.majruszlibrary.events.base.Event;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnBoosterTooltip {
	public final List< Component > components = new ArrayList<>();
	public final BoosterItem booster;

	public static Event< OnBoosterTooltip > listen( Consumer< OnBoosterTooltip > consumer ) {
		return Events.get( OnBoosterTooltip.class ).add( consumer );
	}

	public OnBoosterTooltip( BoosterItem booster ) {
		this.booster = booster;
	}
}
