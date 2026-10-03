package com.xulai.majruszsaccessories.boosters;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnLootGenerated;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.boosters.components.ExperienceBonus;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.BoosterHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.Vex;

@AutoInstance
public class OwlFeather extends BoosterHandler {
	public OwlFeather() {
		super( MajruszsAccessories.OWL_FEATHER, OwlFeather.class );

		this.add( ExperienceBonus.create( 0.15f ) )
			.add( VexDropChance.create() )
			.add( TradeOffer.create() );
	}

	static class VexDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.1f;

		public static ISupplier< BoosterItem > create() {
			return VexDropChance::new;
		}

		protected VexDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Vex );

			handler.getConfig()
				.define( "vex_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
