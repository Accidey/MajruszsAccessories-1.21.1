package com.xulai.majruszlibrary.events;

import com.xulai.majruszlibrary.events.base.Event;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.function.Consumer;

public class OnTradesUpdated implements IEntityEvent {
	public final Villager villager;
	public final MerchantOffers offers;
	public final Player player;

	public static Event< OnTradesUpdated > listen( Consumer< OnTradesUpdated > consumer ) {
		return Events.get( OnTradesUpdated.class ).add( consumer );
	}

	public OnTradesUpdated( Villager villager, Player player ) {
		this.villager = villager;
		this.offers = villager.getOffers();
		this.player = player;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
