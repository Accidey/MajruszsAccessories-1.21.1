package com.xulai.majruszlibrary.events;

import com.xulai.majruszlibrary.events.base.Event;
import com.xulai.majruszlibrary.events.base.Events;

import java.util.function.Consumer;

public class OnServerTicked {
	public static Event< OnServerTicked > listen( Consumer< OnServerTicked > consumer ) {
		return Events.get( OnServerTicked.class ).add( consumer );
	}

	public OnServerTicked() {}
}
