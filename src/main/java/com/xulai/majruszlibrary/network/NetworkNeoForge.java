package com.xulai.majruszlibrary.network;

import com.xulai.majruszlibrary.modhelper.ModHelper;
import com.xulai.majruszlibrary.platform.ModBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.List;

public class NetworkNeoForge implements INetworkPlatform {
	final String protocolVersion = "1";

	@Override
	public void register( ModHelper helper, List< NetworkObject< ? > > objects ) {
		ModBus.BUS.addListener( ( final RegisterPayloadHandlersEvent event )->{
			var registrar = event.registrar( helper.getModId() ).versioned( this.protocolVersion ).optional();
			objects.forEach( object->object.register( registrar ) );
		} );
	}
}
