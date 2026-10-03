package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnClientTicked;
import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Minecraft.class )
public abstract class MixinMinecraft {
	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Events.dispatch( new OnClientTicked() );
	}
}
