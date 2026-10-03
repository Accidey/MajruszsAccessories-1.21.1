package com.xulai.majruszlibrary.events;

import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber
public class OnItemEquippedNeoForge {
	@SubscribeEvent
	public static void onItemEquipped( LivingEquipmentChangeEvent event ) {
		LivingEntity entity = event.getEntity();
		EquipmentSlot slot = event.getSlot();
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();

		Events.dispatch( new OnItemEquipped( entity, slot, from, to ) );
	}
}
