package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnItemDamaged;
import com.xulai.majruszlibrary.events.OnItemTooltip;
import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin( value = ItemStack.class, priority = 1100 )
public abstract class MixinItemStack {
	@ModifyArg(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak (ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
			value = "INVOKE"
		),
		index = 0,
		method = "hurtAndBreak (ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V"
	)
	private int hurtAndBreak( int damage, ServerLevel level, LivingEntity entity, Consumer< Item > consumer ) {
		ItemStack itemStack = ( ItemStack )( Object )this;
		if( !itemStack.isDamageableItem() ) {
			return damage;
		}

		ServerPlayer player = entity instanceof ServerPlayer serverPlayer ? serverPlayer : null;
		int extraDamage = Events.dispatch( new OnItemDamaged( player, itemStack, damage ) ).getExtraDamage();
		if( extraDamage == 0 ) {
			return damage;
		}

		return damage + extraDamage;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "getTooltipLines (Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;"
	)
	private void getTooltipLines( Item.TooltipContext context, Player player, TooltipFlag flag, CallbackInfoReturnable< List< Component > > callback ) {
		List< Component > components = callback.getReturnValue();
		if( components.isEmpty() ) {
			return;
		}

		Events.dispatch( new OnItemTooltip( ( ItemStack )( Object )this, components, flag, player ) );
	}
}
