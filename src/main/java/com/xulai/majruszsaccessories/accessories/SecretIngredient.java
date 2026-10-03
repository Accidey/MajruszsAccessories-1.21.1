package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnItemBrewed;
import com.xulai.majruszlibrary.level.LevelHelper;
import com.xulai.majruszlibrary.math.AnyPos;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.StrongerPotions;
import com.xulai.majruszsaccessories.common.AccessoryHandler;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.events.base.CustomConditions;
import com.xulai.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class SecretIngredient extends AccessoryHandler {
	public SecretIngredient() {
		super( MajruszsAccessories.SECRET_INGREDIENT, SecretIngredient.class );

		this.add( StrongerPotions.create( 0.6f, 1 ) )
			.add( BrewingDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.HOUSEHOLD_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class BrewingDropChance extends BonusComponent< AccessoryItem > {
		float chance = 0.02f;

		public static ISupplier< AccessoryItem > create() {
			return BrewingDropChance::new;
		}

		protected BrewingDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnItemBrewed.listen( this::spawnAccessory )
				.addCondition( CustomConditions.dropChance( s->this.chance, data->LevelHelper.getNearestPlayer( data.getLevel(), data.blockPos, 30.0f ) ) );

			handler.getConfig()
				.define( "brewing_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}

		private void spawnAccessory( OnItemBrewed data ) {
			AnyPos pos = AnyPos.from( data.blockPos ).center();

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
