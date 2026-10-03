package com.xulai.majruszsaccessories.accessories;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnItemDamaged;
import com.xulai.majruszlibrary.events.base.Priority;
import com.xulai.majruszlibrary.math.AnyPos;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.accessories.components.AccessoryIncompatibility;
import com.xulai.majruszsaccessories.accessories.components.MiningDurabilityBonus;
import com.xulai.majruszsaccessories.common.AccessoryHandler;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.events.base.CustomConditions;
import com.xulai.majruszsaccessories.items.AccessoryItem;

@AutoInstance
public class ToolScraps extends AccessoryHandler {
	public ToolScraps() {
		super( MajruszsAccessories.TOOL_SCRAPS, ToolScraps.class );

		this.add( MiningDurabilityBonus.create( 0.1f ) )
			.add( MiningDropChance.create() )
			.add( TradeOffer.create() )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.MINER_RUNE ) )
			.add( AccessoryIncompatibility.create( MajruszsAccessories.SOUL_OF_MINECRAFT ) );
	}

	static class MiningDropChance extends BonusComponent< AccessoryItem > {
		float multiplier = 0.0003f;

		public static ISupplier< AccessoryItem > create() {
			return MiningDropChance::new;
		}

		protected MiningDropChance( BonusHandler< AccessoryItem > handler ) {
			super( handler );

			OnItemDamaged.listen( this::spawnScraps )
				.priority( Priority.LOWEST )
				.addCondition( OnItemDamaged::isAboutToBroke )
				.addCondition( data->data.player != null )
				.addCondition( CustomConditions.dropChance( data->this.multiplier * data.itemStack.getMaxDamage(), data->data.player ) );

			handler.getConfig()
				.define( "durability_drop_chance_multiplier", Reader.number(), s->this.multiplier, ( s, v )->this.multiplier = Range.CHANCE.clamp( v ) );
		}

		private void spawnScraps( OnItemDamaged data ) {
			AnyPos pos = AnyPos.from( data.player.position() );

			this.spawnFlyingItem( data.getLevel(), pos.vec3(), pos.add( 0.0f, 1.0f, 0.0f ).vec3() );
		}
	}
}
