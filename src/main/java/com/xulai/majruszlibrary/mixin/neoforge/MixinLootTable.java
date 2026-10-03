package com.xulai.majruszlibrary.mixin.neoforge;

import com.xulai.majruszlibrary.events.OnLootGenerated;
import com.xulai.majruszlibrary.events.base.Events;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootTable.class )
public abstract class MixinLootTable {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getRandomItems (Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"
	)
	private void getRandomItems( LootParams context, CallbackInfoReturnable< ObjectArrayList< ItemStack > > callback ) {
		callback.setReturnValue( Events.dispatch( new OnLootGenerated( callback.getReturnValue(), ( ( LootTable )( Object )this ).getLootTableId(), context ) ).generatedLoot );
	}
}
