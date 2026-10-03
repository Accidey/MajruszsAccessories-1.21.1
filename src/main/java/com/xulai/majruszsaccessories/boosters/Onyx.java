package com.xulai.majruszsaccessories.boosters;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnLootGenerated;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.boosters.components.EfficiencyBonus;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.BoosterHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.warden.Warden;

@AutoInstance
public class Onyx extends BoosterHandler {
	public Onyx() {
		super( MajruszsAccessories.ONYX, Onyx.class );

		this.add( EfficiencyBonus.create( 0.09f ) )
			.add( WardenDropChance.create() )
			.add( TradeOffer.create() );
	}

	static class WardenDropChance extends BonusComponent< BoosterItem > {
		float chance = 1.0f;

		public static ISupplier< BoosterItem > create() {
			return WardenDropChance::new;
		}

		protected WardenDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Warden );

			handler.getConfig()
				.define( "warden_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
