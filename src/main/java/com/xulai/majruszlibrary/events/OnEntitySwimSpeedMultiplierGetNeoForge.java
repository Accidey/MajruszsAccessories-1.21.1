package com.xulai.majruszlibrary.events;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.entity.AttributeHandler;
import com.xulai.majruszlibrary.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForgeMod;

@AutoInstance
public class OnEntitySwimSpeedMultiplierGetNeoForge {
	private final AttributeHandler attribute = new AttributeHandler( "majrusz_library_swim_speed", ()->NeoForgeMod.SWIM_SPEED, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL );

	public OnEntitySwimSpeedMultiplierGetNeoForge() {
		OnEntityTicked.listen( data->{
			float multiplier = data.entity instanceof IMixinLivingEntity entity ? entity.majruszlibrary$getSwimSpeedMultiplier() : 1.0f;

			this.attribute.setValue( multiplier - 1.0f ).apply( data.entity );
		} );
	}
}
