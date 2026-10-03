package com.xulai.majruszsaccessories.boosters;

import com.xulai.majruszlibrary.annotation.AutoInstance;
import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.events.OnLootGenerated;
import com.xulai.majruszlibrary.events.base.Condition;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.MajruszsAccessories;
import com.xulai.majruszsaccessories.boosters.components.LuckBonus;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.common.BoosterHandler;
import com.xulai.majruszsaccessories.common.components.TradeOffer;
import com.xulai.majruszsaccessories.items.BoosterItem;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;

@AutoInstance
public class Horseshoe extends BoosterHandler {
	public Horseshoe() {
		super( MajruszsAccessories.HORSESHOE, Horseshoe.class );

		this.add( LuckBonus.create( 1.0f ) )
			.add( SkeletonHorsemanDropChance.create() )
			.add( TradeOffer.create() );
	}

	static class SkeletonHorsemanDropChance extends BonusComponent< BoosterItem > {
		float chance = 0.334f;

		public static ISupplier< BoosterItem > create() {
			return SkeletonHorsemanDropChance::new;
		}

		protected SkeletonHorsemanDropChance( BonusHandler< BoosterItem > handler ) {
			super( handler );

			OnLootGenerated.listen( this::addToGeneratedLoot )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( Condition.chance( ()->this.chance ) )
				.addCondition( data->data.lastDamagePlayer != null )
				.addCondition( data->data.entity instanceof Skeleton skeleton && skeleton.getRootVehicle() instanceof SkeletonHorse );

			handler.getConfig()
				.define( "skeleton_horseman_drop_chance", Reader.number(), s->this.chance, ( s, v )->this.chance = Range.CHANCE.clamp( v ) );
		}
	}
}
