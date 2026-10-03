package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.events.OnBabySpawned;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.events.base.Events;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.entity.AgeableMob;

public class BreedingTwins extends BonusComponent< AccessoryItem > {
	static AgeableMob LAST_CHILD = null;
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new BreedingTwins( handler, chance );
	}

	protected BreedingTwins( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnBabySpawned.listen( this::spawnTwins )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( data->data.player != null )
			.addCondition( data->data.child != LAST_CHILD );

		this.addTooltip( "majruszsaccessories.bonuses.spawn_twins", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "breeding_twins", this.chance::define );
	}

	private void spawnTwins( OnBabySpawned data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() || !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		LAST_CHILD = data.parentA.getBreedOffspring( data.getServerLevel(), data.parentB );
		if( LAST_CHILD == null ) {
			return;
		}

		LAST_CHILD.setBaby( true );
		LAST_CHILD.absMoveTo( data.parentA.getX(), data.parentA.getY(), data.parentA.getZ(), 0.0f, 0.0f );
		data.getLevel().addFreshEntity( LAST_CHILD );
		Events.dispatch( new OnBabySpawned( data.parentA, data.parentB, LAST_CHILD, data.player ) );
		this.spawnEffects( data, LAST_CHILD, holder );
	}

	private void spawnEffects( OnBabySpawned data, AgeableMob child, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 4 )
			.sizeBased( child )
			.emit( data.getServerLevel() );
	}
}
