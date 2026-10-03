package com.xulai.majruszsaccessories.common;

import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.events.OnItemDecorationsRendered;
import com.xulai.majruszlibrary.events.base.Priority;
import com.xulai.majruszlibrary.registry.RegistryObject;
import com.xulai.majruszlibrary.text.TextHelper;
import com.xulai.majruszsaccessories.config.Config;
import com.xulai.majruszsaccessories.events.OnAccessoryTooltip;
import com.xulai.majruszsaccessories.items.AccessoryItem;
import net.minecraft.ChatFormatting;

public class AccessoryHandler extends BonusHandler< AccessoryItem > {
	public AccessoryHandler( RegistryObject< AccessoryItem > item, Class< ? extends AccessoryHandler > clazz ) {
		super( item, clazz, item.getId() );

		OnAccessoryTooltip.listen( this::addTooltip )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnAccessoryTooltip.listen( this::addEmptyBoostersTooltip )
			.priority( Priority.LOW )
			.addCondition( data->data.holder.getItem().equals( this.getItem() ) );

		OnItemDecorationsRendered.listen( this::addBoosterIcon )
			.addCondition( data->data.itemStack.is( this.getItem() ) );

		Serializables.getStatic( Config.Accessories.class )
			.define( item.getId(), clazz );
	}

	private void addEmptyBoostersTooltip( OnAccessoryTooltip data ) {
		for( int idx = 0; idx < data.holder.getBoosterSlotsLeft(); ++idx ) {
			data.components.add( TextHelper.translatable( "majruszsaccessories.items.booster_empty" ).withStyle( ChatFormatting.DARK_GRAY ) );
		}
	}
}
