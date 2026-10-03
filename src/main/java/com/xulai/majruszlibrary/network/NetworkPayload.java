package com.xulai.majruszlibrary.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class NetworkPayload< Type > implements CustomPacketPayload {
	final CustomPacketPayload.Type< NetworkPayload< Type > > type;
	final Type value;

	public NetworkPayload( CustomPacketPayload.Type< NetworkPayload< Type > > type, Type value ) {
		this.type = type;
		this.value = value;
	}

	@Override
	public CustomPacketPayload.Type< ? extends CustomPacketPayload > type() {
		return this.type;
	}
}
