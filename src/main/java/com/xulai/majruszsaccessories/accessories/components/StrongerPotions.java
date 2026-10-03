package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.events.OnItemBrewed;
import com.xulai.majruszlibrary.item.ItemHelper;
import com.xulai.majruszlibrary.level.LevelHelper;
import com.xulai.majruszlibrary.math.AnyPos;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.config.RangedInteger;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StrongerPotions extends BonusComponent< AccessoryItem > {
	RangedFloat durationPenalty = new RangedFloat().id( "duration_penalty" ).maxRange( Range.of( 0.0f, 1.0f ) );
	RangedFloat amplifier = new RangedFloat().id( "amplifier" ).maxRange( Range.of( 1.0f, 20.0f ) );

	public static ISupplier< AccessoryItem > create( float durationPenalty, float amplifier ) {
		return handler->new StrongerPotions( handler, durationPenalty, amplifier );
	}

	protected StrongerPotions( BonusHandler< AccessoryItem > handler, float durationPenalty, float amplifier ) {
		super( handler );

		this.durationPenalty.set( durationPenalty, Range.of( 0.0f, 1.0f ) );
		this.amplifier.set( amplifier, Range.of( 1.0f, 10.0f ) );

		OnItemBrewed.listen( this::boostPotions )
			.addCondition( data->data.items.subList( 0, 3 ).stream().anyMatch( itemStack->!itemStack.getOrDefault( DataComponents.POTION_CONTENTS, PotionContents.EMPTY ).getAllEffects().iterator().hasNext() ) );

		this.addTooltip( "majruszsaccessories.bonuses.potion_amplifier", TooltipHelper.asValue( this.amplifier ).scaleOnlyOnDetailed() );
		this.addTooltip( "majruszsaccessories.bonuses.potion_duration", TooltipHelper.asPercent( this.durationPenalty ).bonusMultiplier( -1.0f ) );

		handler.getConfig()
			.define( "stronger_potion", subconfig->{
				this.durationPenalty.define( subconfig );
				this.amplifier.define( subconfig );
			} );
	}

	private void boostPotions( OnItemBrewed data ) {
		Player player = LevelHelper.getNearestPlayer( data.level, data.blockPos, 10.0f );
		AccessoryHolder holder = AccessoryHolders.get( player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		data.mapPotions( potions->{
			float durationMultiplier = 1.0f - holder.apply( this.durationPenalty, -1.0f );
			int extraAmplifier = Math.round( holder.apply( this.amplifier ) );

			return potions.stream()
				.map( itemStack->{
					PotionContents contents = itemStack.getOrDefault( DataComponents.POTION_CONTENTS, PotionContents.EMPTY );
					List< MobEffectInstance > effects = new ArrayList<>();
					contents.getAllEffects().forEach( effects::add );
					if( effects.isEmpty() ) {
						return itemStack;
					}

				ItemStack potion = new ItemStack( itemStack.getItem() );
				ItemHelper.modifyTag( potion, tag -> Serializables.write( new Data(), tag ) );

					potion.set( DataComponents.POTION_CONTENTS, new PotionContents( contents.potion(), Optional.empty(), effects.stream()
						.map( effect->new MobEffectInstance( effect.getEffect(), Math.max( 40, ( int )( effect.getDuration() * durationMultiplier ) ), effect.getAmplifier() + extraAmplifier ) )
						.toList() ) );

					return potion;
				} )
				.toList();
		} );
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnItemBrewed data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 6 )
			.position( AnyPos.from( data.blockPos ).center().vec3() )
			.emit( data.getServerLevel() );
	}

	private static class Data {
		static {
			Serializables.get( Data.class )
				.define( "display", config->{
					config.define( "Name", Reader.string(), s->s.name, ( s, v )->s.name = v );
				} );
		}

		private String name = "{\"translate\":\"majruszsaccessories.bonuses.potion_name\",\"italic\":false}";
	}
}
