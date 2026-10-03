package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnItemCrafted;
import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin( InventoryMenu.class )
public abstract class MixinInventoryMenu {
	@Inject(
		at = @At(
			ordinal = 0,
			shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/world/inventory/InventoryMenu;moveItemStackTo (Lnet/minecraft/world/item/ItemStack;IIZ)Z",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "quickMoveStack (Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;"
	)
	private void quickMoveStack( Player player, int type, CallbackInfoReturnable< ItemStack > callback, ItemStack itemStack, Slot slot,
		ItemStack craftedStack, EquipmentSlot equipmentSlot
	) {
		Events.dispatch( new OnItemCrafted( player, craftedStack ) );
	}
}
