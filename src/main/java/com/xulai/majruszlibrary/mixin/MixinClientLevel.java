package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.mixininterfaces.IMixinClientLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mixin( ClientLevel.class )
public abstract class MixinClientLevel implements IMixinClientLevel {
	private final Map< Integer, Consumer< Entity > > majruszlibrary$DELAYED_CALLBACKS = new HashMap<>();

	@Inject(
		at = @At( "RETURN" ),
		method = "addEntity (Lnet/minecraft/world/entity/Entity;)V"
	)
	private void addEntity( Entity entity, CallbackInfo callback ) {
		Consumer< Entity > delayedCallback = majruszlibrary$DELAYED_CALLBACKS.get( entity.getId() );
		if( delayedCallback != null ) {
			majruszlibrary$DELAYED_CALLBACKS.remove( entity.getId() );
			delayedCallback.accept( entity );
		}
	}

	@Override
	public < Type > void majruszlibrary$delayExecution( int entityId, Class< Type > clazz, Consumer< Type > consumer ) {
		majruszlibrary$DELAYED_CALLBACKS.computeIfAbsent( entityId, id->entity->{
			if( clazz.isInstance( entity ) ) {
				consumer.accept( clazz.cast( entity ) );
			}
		} );
	}
}
