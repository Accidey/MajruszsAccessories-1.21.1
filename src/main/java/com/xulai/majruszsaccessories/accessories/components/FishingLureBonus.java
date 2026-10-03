package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.emitter.ParticleEmitter;
import com.xulai.majruszlibrary.events.OnFishingTimeGet;
import com.xulai.majruszlibrary.level.LevelHelper;
import com.xulai.majruszlibrary.math.AnyPos;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.core.BlockPos;

public class FishingLureBonus extends BonusComponent< AccessoryItem > {
	RangedFloat multiplier = new RangedFloat().id( "multiplier" ).maxRange( Range.of( 0.0f, 1.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new FishingLureBonus( handler, bonus );
	}

	protected FishingLureBonus( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.multiplier.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnFishingTimeGet.listen( this::decreaseFishingTime );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_lure", TooltipHelper.asPercent( this.multiplier ) );

		handler.getConfig()
			.define( "fishing_time", this.multiplier::define );
	}

	private void decreaseFishingTime( OnFishingTimeGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		data.time = Math.round( data.time * ( 1.0f - holder.apply( this.multiplier ) ) );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnFishingTimeGet data, AccessoryHolder holder ) {
		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );

		holder.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
