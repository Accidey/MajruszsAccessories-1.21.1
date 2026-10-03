package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.entity.EffectDef;
import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.events.OnPlayerWakedUp;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszlibrary.time.TimeHelper;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.config.RangedInteger;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class SleepingBonuses extends BonusComponent< AccessoryItem > {
	RangedFloat count = new RangedFloat().id( "count" ).maxRange( Range.of( 1.0f, 100.0f ) );
	RangedInteger duration = new RangedInteger().id( "duration" ).maxRange( Range.of( 1, 10000 ) );
	List< EffectDef > effects = List.of(
		new EffectDef( MobEffects.REGENERATION, 0 ),
		new EffectDef( MobEffects.SATURATION, 0 ),
		new EffectDef( MobEffects.ABSORPTION, 1 ),
		new EffectDef( MobEffects.DAMAGE_RESISTANCE, 0 ),
		new EffectDef( MobEffects.FIRE_RESISTANCE, 0 ),
		new EffectDef( MobEffects.MOVEMENT_SPEED, 0 ),
		new EffectDef( MobEffects.DIG_SPEED, 0 ),
		new EffectDef( MobEffects.DAMAGE_BOOST, 0 )
	);

	public static ISupplier< AccessoryItem > create( float count, int duration ) {
		return handler->new SleepingBonuses( handler, count, duration );
	}

	protected SleepingBonuses( BonusHandler< AccessoryItem > handler, float count, int duration ) {
		super( handler );

		this.count.set( count, Range.of( 1.0f, 10.0f ) );
		this.duration.set( duration, Range.of( 1, 10000 ) );

		OnPlayerWakedUp.listen( this::applyBonuses )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( data->!data.wasSleepStoppedManually );

		this.addTooltip( "majruszsaccessories.bonuses.sleep_bonuses", TooltipHelper.asValue( this.count ).scaleOnlyOnDetailed(), TooltipHelper.asValue( this.duration ) );

		handler.getConfig()
			.define( "sleep_bonuses", subconfig->{
				this.count.define( subconfig );
				this.duration.define( subconfig );
				subconfig.define( "effects", Reader.list( Reader.custom( EffectDef::new ) ), s->this.effects, ( s, v )->this.effects = v );
			} );
	}

	private void applyBonuses( OnPlayerWakedUp data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		int count = Math.round( holder.apply( this.count ) );
		int duration = TimeHelper.toTicks( holder.apply( this.duration ) );
		this.getRandomMobEffects( data.player, count )
			.forEach( effect->data.player.addEffect( new MobEffectInstance( effect.effect, duration, effect.amplifier ) ) );
		this.spawnEffects( data, holder );
	}

	private List< EffectDef > getRandomMobEffects( Player player, int count ) {
		List< EffectDef > missingEffects = this.effects.stream().filter( effect->!player.hasEffect( effect.effect ) ).toList();
		if( missingEffects.isEmpty() ) {
			missingEffects = this.effects;
		}

		return Random.next( missingEffects, count );
	}

	private void spawnEffects( OnPlayerWakedUp data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 5 )
			.position( data.player.position() )
			.emit( data.getServerLevel() );
	}

	private static class EffectDef {
		static {
			Serializables.get( EffectDef.class )
				.define( "id", Reader.mobEffect(), s->s.effect == null ? null : s.effect.value(), ( s, v )->s.effect = BuiltInRegistries.MOB_EFFECT.wrapAsHolder( v ) )
				.define( "amplifier", Reader.integer(), s->s.amplifier, ( s, v )->s.amplifier = v );
		}

		private Holder< MobEffect > effect;
		private int amplifier;

		public EffectDef( Holder< MobEffect > effect, int amplifier ) {
			this.effect = effect;
			this.amplifier = amplifier;
		}

		public EffectDef() {}
	}
}
