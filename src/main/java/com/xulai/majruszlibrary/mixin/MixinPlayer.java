package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnPlayerTicked;
import com.xulai.majruszlibrary.events.OnPlayerWakedUp;
import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Player.class )
public abstract class MixinPlayer {
	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Events.dispatch( new OnPlayerTicked( ( Player )( Object )this ) );
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "stopSleepInBed (ZZ)V"
	)
	public void stopSleepInBed( boolean $$0, boolean wasSleepStoppedManually, CallbackInfo callback ) {
		Events.dispatch( new OnPlayerWakedUp( ( Player )( Object )this, wasSleepStoppedManually ) );
	}
}
