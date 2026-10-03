package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.events.OnEntitySwimSpeedMultiplierGet;
import com.xulai.majruszlibrary.events.OnEntityTicked;
import com.xulai.majruszlibrary.events.OnItemEquipped;
import com.xulai.majruszlibrary.events.OnItemSwingDurationGet;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszlibrary.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( value = LivingEntity.class, priority = 1100 )
public abstract class MixinLivingEntity implements IMixinLivingEntity {
	private @Shadow int useItemRemaining;
	float majruszlibrary$swimSpeedMultiplier = 1.0f;

	@Override
	public float majruszlibrary$getSwimSpeedMultiplier() {
		return this.majruszlibrary$swimSpeedMultiplier;
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Events.dispatch( new OnEntityTicked( ( LivingEntity )( Object )this ) );

		this.majruszlibrary$swimSpeedMultiplier = Events.dispatch( new OnEntitySwimSpeedMultiplierGet( ( LivingEntity )( Object )this, 1.0f ) ).getMultiplier();
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getCurrentSwingDuration ()I"
	)
	private void getCurrentSwingDuration( CallbackInfoReturnable< Integer > callback ) {
		callback.setReturnValue( Events.dispatch( new OnItemSwingDurationGet( ( LivingEntity )( Object )this, callback.getReturnValue() ) )
			.getSwingDuration() );
	}
}
