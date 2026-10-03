package com.xulai.majruszlibrary.events;

import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;

@EventBusSubscriber
public class OnBabySpawnedNeoForge {
	@SubscribeEvent
	public static void onBabySpawned( BabyEntitySpawnEvent event ) {
		if( !( event.getParentA() instanceof Animal parentA ) || !( event.getParentB() instanceof Animal parentB ) ) {
			return;
		}

		AgeableMob child = event.getChild();
		Player player = event.getCausedByPlayer();

		Events.dispatch( new OnBabySpawned( parentA, parentB, child, player ) );
	}
}
