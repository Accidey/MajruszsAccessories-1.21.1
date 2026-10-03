package com.xulai.majruszsaccessories.listeners;

import com.xulai.majruszlibrary.events.OnItemCrafted;
import com.xulai.majruszlibrary.events.base.Priority;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementProviders {
	static {
		OnItemCrafted.listen( AdvancementProviders::giveAdvancements )
			.addCondition( data->data.player instanceof ServerPlayer )
			.addCondition( data->data.itemStack.getItem() instanceof AccessoryItem )
			.priority( Priority.LOWEST );
	}

	private static void giveAdvancements( OnItemCrafted data ) {
		ServerPlayer player = ( ServerPlayer )data.player;
		AccessoryHolder holder = AccessoryHolder.getOrCreate( data.itemStack );
		if( holder.hasAnyBooster() ) {
			MajruszsAccessories.HELPER.triggerAchievement( player, "booster_used" );
		}
		if( Math.abs( holder.getBonus() - 0.69 ) < 1e-5 ) {
			MajruszsAccessories.HELPER.triggerAchievement( player, "booster_nice" );
		}
	}
}
