package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.emitter.ParticleEmitter;
import com.xulai.majruszlibrary.events.OnFishingExtraItemsGet;
import com.xulai.majruszlibrary.item.LootHelper;
import com.xulai.majruszlibrary.level.LevelHelper;
import com.xulai.majruszlibrary.math.AnyPos;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class FishingExtraTreasure extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new FishingExtraTreasure( handler, chance );
	}

	protected FishingExtraTreasure( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnFishingExtraItemsGet.listen( this::addExtraTreasure );

		this.addTooltip( "majruszsaccessories.bonuses.extra_fishing_treasure", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "extra_fishing_treasure", this.chance::define );
	}

	private void addExtraTreasure( OnFishingExtraItemsGet data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() || !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		data.extraItems.addAll( LootHelper.getLootTable( BuiltInLootTables.FISHING_TREASURE ).getRandomItems( LootHelper.toGiftParams( data.player ) ) );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnFishingExtraItemsGet data, AccessoryHolder holder ) {
		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );

		holder.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
