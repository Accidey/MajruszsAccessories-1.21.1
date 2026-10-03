package com.xulai.majruszlibrary.events;

import com.xulai.majruszlibrary.events.base.Event;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnPlayerTicked implements IEntityEvent {
	public final Player player;

	public static Event< OnPlayerTicked > listen( Consumer< OnPlayerTicked > consumer ) {
		return Events.get( OnPlayerTicked.class ).add( consumer );
	}

	public OnPlayerTicked( Player player ) {
		this.player = player;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
