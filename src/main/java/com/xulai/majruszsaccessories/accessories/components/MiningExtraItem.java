package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.collection.DefaultMap;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.emitter.ParticleEmitter;
import com.xulai.majruszlibrary.events.OnLootGenerated;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.events.base.Event;
import com.xulai.majruszlibrary.item.LootHelper;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.function.Consumer;

public class MiningExtraItem extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );
	Map< String, ResourceLocation > lootIds = DefaultMap.of(
		DefaultMap.defaultEntry( MajruszsAccessories.HELPER.getLocation( "gameplay/lucky_rock_default" ) ),
		DefaultMap.entry( "minecraft:the_nether", MajruszsAccessories.HELPER.getLocation( "gameplay/lucky_rock_nether" ) ),
		DefaultMap.entry( "minecraft:the_end", MajruszsAccessories.HELPER.getLocation( "gameplay/lucky_rock_end" ) )
	);

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new MiningExtraItem( handler, chance );
	}

	protected MiningExtraItem( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnStoneMined.listen( this::addExtraLoot );

		this.addTooltip( "majruszsaccessories.bonuses.extra_stone_loot", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "extra_mining_item", subconfig->{
				this.chance.define( subconfig );
				subconfig.define( "loot_ids", Reader.map( Reader.location() ), s->this.lootIds, ( s, v )->this.lootIds = DefaultMap.of( v ) );
			} );
	}

	private void addExtraLoot( OnLootGenerated data ) {
		AccessoryHolder holder = AccessoryHolders.get( ( LivingEntity )data.entity ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() || !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		LivingEntity entity = ( LivingEntity )data.entity;
		ResourceLocation id = this.lootIds.get( entity.level().dimension().location().toString() );

		data.generatedLoot.addAll( LootHelper.getLootTable( id ).getRandomItems( LootHelper.toGiftParams( entity ) ) );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnLootGenerated data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 3 )
			.offset( ParticleEmitter.offset( 0.2f ) )
			.position( data.origin )
			.emit( data.getServerLevel() );
	}

	public static class OnStoneMined {
		public static Event< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
			return OnLootGenerated.listen( consumer )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->data.blockState != null )
				.addCondition( data->data.blockState.is( BlockTags.BASE_STONE_OVERWORLD ) || data.blockState.is( BlockTags.BASE_STONE_NETHER ) || data.blockState.is( Blocks.END_STONE ) )
				.addCondition( data->data.entity instanceof LivingEntity )
				.addCondition( data->data.origin != null );
		}
	}
}
