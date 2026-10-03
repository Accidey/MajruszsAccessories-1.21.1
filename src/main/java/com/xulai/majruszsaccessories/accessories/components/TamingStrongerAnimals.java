package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.entity.AttributeHandler;
import com.xulai.majruszlibrary.events.OnAnimalTamed;
import com.xulai.majruszlibrary.events.OnBabySpawned;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;

public class TamingStrongerAnimals extends BonusComponent< AccessoryItem > {
	final AttributeHandler health;
	final AttributeHandler damage;
	final AttributeHandler speed;
	final AttributeHandler jumpHeight;
	RangedFloat bonus = new RangedFloat().id( "bonus" ).maxRange( Range.of( 0.0f, 10.0f ) );

	public static ISupplier< AccessoryItem > create( float bonus ) {
		return handler->new TamingStrongerAnimals( handler, bonus );
	}

	protected TamingStrongerAnimals( BonusHandler< AccessoryItem > handler, float bonus ) {
		super( handler );

		this.health = new AttributeHandler( "%s_health_multiplier".formatted( handler.getId() ), ()->Attributes.MAX_HEALTH, AttributeModifier.Operation.ADD_MULTIPLIED_BASE );
		this.damage = new AttributeHandler( "%s_damage_multiplier".formatted( handler.getId() ), ()->Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE );
		this.speed = new AttributeHandler( "%s_speed_multiplier".formatted( handler.getId() ), ()->Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.ADD_MULTIPLIED_BASE );
		this.jumpHeight = new AttributeHandler( "%s_jump_height_multiplier".formatted( handler.getId() ), ()->Attributes.JUMP_STRENGTH, AttributeModifier.Operation.ADD_MULTIPLIED_BASE );
		this.bonus.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnAnimalTamed.listen( this::applyBonuses )
			.addCondition( Condition.isLogicalServer() );

		OnBabySpawned.listen( this::applyBonuses )
			.addCondition( Condition.isLogicalServer() )
			.addCondition( data->this.hasModifier( data.parentA ) || this.hasModifier( data.parentB ) );

		this.addTooltip( "majruszsaccessories.bonuses.animal_attributes", TooltipHelper.asPercent( this.bonus ) );

		handler.getConfig()
			.define( "animal_bonus", this.bonus::define );
	}

	private void applyBonuses( OnAnimalTamed data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.tamer ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() ) {
			return;
		}

		this.applyBonuses( holder.apply( this.bonus ), data.animal );
		this.spawnEffects( data, holder );
	}

	private void applyBonuses( OnBabySpawned data ) {
		this.applyBonuses( ( float )Math.max( this.getModifierValueSafe( data.parentA ), this.getModifierValueSafe( data.parentB ) ), data.child );
	}

	private void applyBonuses( float bonus, LivingEntity entity ) {
		this.health.setValue( bonus ).apply( entity );
		if( this.damage.hasAttribute( entity ) ) {
			this.damage.setValue( bonus ).apply( entity );
		}
		if( entity instanceof Horse horse ) {
			this.jumpHeight.setValue( bonus ).apply( horse );
			this.speed.setValue( bonus ).apply( horse );
		}
		entity.setHealth( entity.getMaxHealth() );
	}

	private void spawnEffects( OnAnimalTamed data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 4 )
			.sizeBased( data.animal )
			.emit( data.getServerLevel() );
	}

	private double getModifierValueSafe( LivingEntity entity ) {
		return this.hasModifier( entity ) ? this.getModifierValue( entity ) : 0.0;
	}

	private double getModifierValue( LivingEntity entity ) {
		return entity.getAttributes().getModifierValue( Attributes.MAX_HEALTH, this.health.getId() );
	}

	private boolean hasModifier( LivingEntity entity ) {
		return entity.getAttributes().hasModifier( Attributes.MAX_HEALTH, this.health.getId() );
	}
}
