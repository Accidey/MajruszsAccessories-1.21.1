package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.events.OnEntitySwimSpeedMultiplierGet;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszlibrary.time.TimeHelper;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.server.level.ServerLevel;

public class SwimmingSpeedBonus extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new SwimmingSpeedBonus( handler, bonus );
	}

	protected SwimmingSpeedBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.multiplier.set( bonus, Range.of( 0.0f, 10.0f ) );

		OnEntitySwimSpeedMultiplierGet.listen( this::increaseSwimSpeed );

		this.addTooltip( "majruszsaccessories.bonuses.swim_bonus", TooltipHelper.asPercent( this.multiplier ) );

		handler.getConfig()
			.define( "swim_speed", this.multiplier::define );
	}

	private void increaseSwimSpeed( OnEntitySwimSpeedMultiplierGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.entity ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		data.multiplier *= 1.0f + holder.apply( this.multiplier );
		if( data.entity.isInWater() && data.getLevel() instanceof ServerLevel && TimeHelper.haveTicksPassed( 5 ) ) {
			this.spawnEffects( data, holder );
		}
	}

	private void spawnEffects( OnEntitySwimSpeedMultiplierGet data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 1 )
			.sizeBased( data.entity )
			.emit( data.getServerLevel() );
	}
}
