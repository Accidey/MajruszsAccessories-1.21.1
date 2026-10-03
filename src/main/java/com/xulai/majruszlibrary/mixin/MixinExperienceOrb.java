package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnExpOrbPickedUp;
import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin( ExperienceOrb.class )
public abstract class MixinExperienceOrb {
	@ModifyArg(
		at = @At(
			target = "Lnet/minecraft/world/entity/ExperienceOrb;repairPlayerItems (Lnet/minecraft/server/level/ServerPlayer;I)I",
			value = "INVOKE"
		),
		index = 1,
		method = "playerTouch (Lnet/minecraft/world/entity/player/Player;)V"
	)
	private int getValue( ServerPlayer player, int value ) {
		return Events.dispatch( new OnExpOrbPickedUp( player, ( ExperienceOrb )( Object )this, value ) ).getExperience();
	}
}
