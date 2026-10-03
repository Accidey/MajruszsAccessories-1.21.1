package com.xulai.majruszsaccessories.boosters;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnLootGenerated;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.boosters.components.AccessoryDropChance;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.BoosterHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.monster.Guardian;

@AutoInstance
public class Dice extends BoosterHandler {
	public Dice() {
		super( MajruszsAccessories.DICE, Dice.class );

		this.add( AccessoryDropChance.create( 0.2f ) )
			.add( GuardianDropChance.create() )
			.add( TradeOffer.create() );
	}

	static class GuardianDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.05f;

		public static ISupplier< BoosterItem > create() {
			return GuardianDropChance::new;
		}

		protected GuardianDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Guardian );

			handler.getConfig()
				.define( "guardian_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
