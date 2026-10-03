package com.xulai.majruszsaccessories.boosters.components;

import com.xulai.majruszlibrary.events.OnExpOrbPickedUp;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.BoosterItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;

public class ExperienceBonus extends BonusComponent< BoosterItem > {
	RangedFloat bonus = new RangedFloat().id( "experience_bonus" );

	public static ISupplier< BoosterItem > create( float bonus ) {
		return handler->new ExperienceBonus( handler, bonus );
	}

	protected ExperienceBonus( BonusHandler< BoosterItem > handler, float bonus ) {
		super( handler );

		this.bonus.set( bonus, Range.of( 0.0f, 1.0f ) );

		OnExpOrbPickedUp.listen( this::increaseExperience );

		this.addTooltip( "majruszsaccessories.boosters.experience_bonus", TooltipHelper.asBooster( this::getItem ), TooltipHelper.asFixedPercent( this.bonus ) );

		this.bonus.define( handler.getConfig() );
	}

	private void increaseExperience( OnExpOrbPickedUp data ) {
		data.experience += Random.round( data.original * this.bonus.get() * AccessoryHolders.get( data.player ).getBoostersCount( this::getItem ) );
	}
}
