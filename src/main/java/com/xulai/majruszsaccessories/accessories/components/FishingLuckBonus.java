package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.emitter.ParticleEmitter;
import com.xulai.majruszlibrary.entity.AttributeHandler;
import com.xulai.majruszlibrary.events.OnItemFished;
import com.xulai.majruszlibrary.events.OnPlayerTicked;
import com.xulai.majruszlibrary.events.base.Condition;
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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class FishingLuckBonus extends BonusComponent< AccessoryItem > {
	final AttributeHandler attribute;
	RangedFloat luck = new RangedFloat().id( "bonus" ).maxRange( Range.of( 0.0f, 100.0f ) );

	public static ISupplier< AccessoryItem > create( float luck ) {
		return handler->new FishingLuckBonus( handler, luck );
	}

	protected FishingLuckBonus( BonusHandler< AccessoryItem > handler, float luck ) {
		super( handler );

		this.attribute = new AttributeHandler( "%s_fishing_luck_bonus".formatted( handler.getId() ), ()->Attributes.LUCK, AttributeModifier.Operation.ADD_VALUE );
		this.luck.set( luck, Range.of( 0.0f, 10.0f ) );

		OnPlayerTicked.listen( this::updateLuck )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( Condition.cooldown( 4 ) );

		OnItemFished.listen( this::spawnEffects )
			.addCondition( Condition.isLogicalServer() );

		this.addTooltip( "majruszsaccessories.bonuses.fishing_luck", TooltipHelper.asValue( this.luck ) );

		handler.getConfig()
			.define( "fishing_luck", this.luck::define );
	}

	private void updateLuck( OnPlayerTicked data ) {
		this.attribute.setValue( this.getLuck( data.player ) ).apply( data.player );
	}

	private float getLuck( Player player ) {
		if( player.fishing == null ) {
			return 0.0f;
		}

		AccessoryHolder holder = AccessoryHolders.get( player ).get( this::getItem );
		return holder.isValid() && !holder.isBonusDisabled() ? holder.apply( this.luck ) : 0.0f;
	}

	private void spawnEffects( OnItemFished data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );
		holder.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
