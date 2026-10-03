package com.xulai.majruszlibrary.mixin;

import com.xulai.majruszlibrary.animations.IAnimableEntity;
import com.xulai.majruszlibrary.mixininterfaces.IMixinEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Entity.class )
public abstract class MixinEntity implements IMixinEntity {
	private static final String majruszlibrary$TAG_ID = "MajruszLibrary";
	private int majruszlibrary$glowTicks = 0;
	private int majruszlibrary$invisibleTicks = 0;
	private CompoundTag majruszlibrary$extraTag = null;

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		this.majruszlibrary$glowTicks = Math.max( this.majruszlibrary$glowTicks - 1, 0 );
		this.majruszlibrary$invisibleTicks = Math.max( this.majruszlibrary$invisibleTicks - 1, 0 );
		if( this instanceof IAnimableEntity animable ) {
			animable.getAnimations().tick();
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isCurrentlyGlowing ()Z"
	)
	private void isCurrentlyGlowing( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.majruszlibrary$glowTicks > 0 );
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isInvisible ()Z"
	)
	private void isInvisible( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.majruszlibrary$invisibleTicks > 0 );
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "saveWithoutId (Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"
	)
	private void saveWithoutId( CompoundTag tag, CallbackInfoReturnable< CompoundTag > callback ) {
		if( this.majruszlibrary$extraTag != null ) {
			tag.put( majruszlibrary$TAG_ID, this.majruszlibrary$extraTag );
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "load (Lnet/minecraft/nbt/CompoundTag;)V"
	)
	private void load( CompoundTag tag, CallbackInfo callback ) {
		if( tag.contains( majruszlibrary$TAG_ID ) ) {
			this.majruszlibrary$extraTag = tag.getCompound( majruszlibrary$TAG_ID );
		}
	}

	@Override
	public void majruszlibrary$addGlowTicks( int ticks ) {
		this.majruszlibrary$glowTicks += ticks;
	}

	@Override
	public void majruszlibrary$addInvisibleTicks( int ticks ) {
		this.majruszlibrary$invisibleTicks += ticks;
	}

	@Override
	public int majruszlibrary$getInvisibleTicks() {
		return this.majruszlibrary$invisibleTicks;
	}

	public @Nullable CompoundTag majruszlibrary$getExtraTag() {
		return this.majruszlibrary$extraTag;
	}

	public CompoundTag majruszlibrary$getOrCreateExtraTag() {
		if( this.majruszlibrary$extraTag == null ) {
			this.majruszlibrary$extraTag = new CompoundTag();
		}

		return this.majruszlibrary$extraTag;
	}
}
