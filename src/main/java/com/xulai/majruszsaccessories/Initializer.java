package com.xulai.majruszsaccessories;

import com.xulai.majruszlibrary.MajruszLibrary;
import com.xulai.majruszlibrary.platform.ModBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod( MajruszsAccessories.MOD_ID )
public class Initializer {
	public Initializer( IEventBus modEventBus ) {
		ModBus.BUS = modEventBus;
		MajruszLibrary.HELPER.register();
		MajruszsAccessories.HELPER.register();

		modEventBus.addListener( Initializer::onEnqueueIMC );
	}

	private static void onEnqueueIMC( InterModEnqueueEvent event ) {
		if( !MajruszsAccessories.SLOT_INTEGRATION.isInstalled() ) {
			return;
		}

		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket_left" )
			.priority( 220 )
			.icon( MajruszsAccessories.POCKET_SLOT_TEXTURE )
			.build()
		);
		InterModComms.sendTo( MajruszsAccessories.MOD_ID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder( "pocket_right" )
			.priority( 220 )
			.icon( MajruszsAccessories.POCKET_SLOT_TEXTURE )
			.build()
		);
	}
}
