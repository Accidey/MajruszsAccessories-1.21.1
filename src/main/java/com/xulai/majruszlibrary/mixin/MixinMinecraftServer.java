package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnLevelsLoaded;
import com.xulai.majruszlibrary.events.OnLevelsSaved;
import com.xulai.majruszlibrary.events.OnServerTicked;
import com.xulai.majruszlibrary.events.base.Events;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin( MinecraftServer.class )
public abstract class MixinMinecraftServer {
	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			target = "Lnet/minecraft/server/MinecraftServer;tickChildren (Ljava/util/function/BooleanSupplier;)V",
			value = "INVOKE"
		),
		method = "tickServer (Ljava/util/function/BooleanSupplier;)V"
	)
	private void tickServer( BooleanSupplier haveTime, CallbackInfo callback ) {
		Events.dispatch( new OnServerTicked() );
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/server/MinecraftServer;overworld ()Lnet/minecraft/server/level/ServerLevel;",
			value = "INVOKE"
		),
		method = "saveAllChunks (ZZZ)Z"
	)
	private void saveAllChunks( boolean $$0, boolean $$1, boolean $$2, CallbackInfoReturnable< Boolean > callback ) {
		Events.dispatch( new OnLevelsSaved( ( MinecraftServer )( Object )this ) );
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "createLevels (Lnet/minecraft/server/level/progress/ChunkProgressListener;)V"
	)
	private void createLevels( ChunkProgressListener listener, CallbackInfo callback ) {
		Events.dispatch( new OnLevelsLoaded( ( MinecraftServer )( Object )this ) );
	}
}
