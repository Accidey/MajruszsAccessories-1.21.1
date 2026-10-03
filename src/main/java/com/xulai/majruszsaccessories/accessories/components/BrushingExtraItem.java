package com.xulai.majruszsaccessories.accessories.components;

import com.xulai.majruszlibrary.events.OnItemBrushed;
import com.xulai.majruszlibrary.item.LootHelper;
import com.xulai.majruszlibrary.level.LevelHelper;
import com.xulai.majruszlibrary.math.AnyPos;
import com.xulai.majruszlibrary.math.Random;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszsaccessories.common.AccessoryHolder;
import com.xulai.majruszsaccessories.common.AccessoryHolders;
import com.xulai.majruszsaccessories.common.BonusComponent;
import com.xulai.majruszsaccessories.common.BonusHandler;
import com.xulai.majruszsaccessories.config.RangedFloat;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import com.xulai.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BrushingExtraItem extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new BrushingExtraItem( handler, chance );
	}

	protected BrushingExtraItem( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnItemBrushed.listen( this::addExtraLoot );

		this.addTooltip( "majruszsaccessories.bonuses.extra_archaeology_item", TooltipHelper.asPercent( this.chance ) );

		handler.getConfig()
			.define( "extra_archaeology_item", this.chance::define );
	}

	private void addExtraLoot( OnItemBrushed data ) {
		AccessoryHolder holder = AccessoryHolders.get( data.player ).get( this::getItem );
		if( !holder.isValid() || holder.isBonusDisabled() || !Random.check( holder.apply( this.chance ) ) ) {
			return;
		}

		float width = EntityType.ITEM.getWidth();
		float height = EntityType.ITEM.getHeight();
		Vec3 itemOffset = AnyPos.from( data.direction ).mul( width, height, width ).mul( 0.5f ).vec3();
		Vec3 start = AnyPos.from( data.blockEntity.getBlockPos() ).center().add( data.direction ).add( itemOffset ).vec3();
		Vec3 end = AnyPos.from( data.blockEntity.getBlockPos() ).center().add( AnyPos.from( data.direction ).mul( 1.5 ) ).vec3();
		List< ItemStack > extraItems = LootHelper.getLootTable( data.location ).getRandomItems( LootHelper.toGiftParams( data.player ) );

		for( ItemStack itemStack : extraItems ) {
			LevelHelper.spawnItemEntityFlyingTowardsDirection( itemStack, data.getLevel(), start, end );
		}
		this.spawnEffects( data, holder );
	}

	private void spawnEffects( OnItemBrushed data, AccessoryHolder holder ) {
		holder.getParticleEmitter()
			.count( 8 )
			.position( AnyPos.from( data.blockEntity.getBlockPos() ).center().add( data.direction ).vec3() )
			.emit( data.getServerLevel() );
	}
}
