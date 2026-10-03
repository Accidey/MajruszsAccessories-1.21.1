package com.xulai.majruszsaccessories.events;

import com.xulai.majruszlibrary.events.base.Event;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszsaccessories.common.AccessoryHolder;

import java.util.function.Consumer;

public class OnAccessoryExtraBonusGet {
	public final AccessoryHolder holder;
	public final float original = 0.0f;
	public float bonus = 0.0f;

	public static Event< OnAccessoryExtraBonusGet > listen( Consumer< OnAccessoryExtraBonusGet > consumer ) {
		return Events.get( OnAccessoryExtraBonusGet.class ).add( consumer );
	}

	public OnAccessoryExtraBonusGet( AccessoryHolder holder ) {
		this.holder = holder;
	}
}
