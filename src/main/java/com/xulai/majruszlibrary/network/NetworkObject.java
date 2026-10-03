package com.xulai.majruszlibrary.network;

import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.platform.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NetworkObject< Type > {
	final NetworkHandler networkHandler;
	final ResourceLocation id;
	final Class< Type > clazz;
	final Supplier< Type > instance;
	final CustomPacketPayload.Type< NetworkPayload< Type > > payloadType;
	final List< Consumer< Type > > clientCallbacks = new ArrayList<>();
	final List< BiConsumer< Type, ServerPlayer > > serverCallbacks = new ArrayList<>();

	public NetworkObject( NetworkHandler networkHandler, ResourceLocation id, Class< Type > clazz, Supplier< Type > instance ) {
		this.networkHandler = networkHandler;
		this.id = id;
		this.clazz = clazz;
		this.instance = instance;
		this.payloadType = new CustomPacketPayload.Type<>( id );
	}

	public void register( PayloadRegistrar registrar ) {
		StreamCodec< RegistryFriendlyByteBuf, NetworkPayload< Type > > codec = StreamCodec.of(
			( buffer, payload )->Serializables.write( payload.value, buffer ),
			buffer->new NetworkPayload<>( this.payloadType, Serializables.read( this.instance.get(), buffer ) )
		);

		registrar.playBidirectional( this.payloadType, codec, ( payload, context )->{
			if( context.player() instanceof ServerPlayer sender ) {
				this.broadcastOnServer( payload.value, sender );
			} else {
				this.broadcastOnClient( payload.value );
			}
		} );
	}

	public void sendToClients( List< ServerPlayer > players, Type message ) {
		NetworkPayload< Type > payload = new NetworkPayload<>( this.payloadType, message );
		players.forEach( player->PacketDistributor.sendToPlayer( player, payload ) );
	}

	public void sendToClients( List< ServerPlayer > players ) {
		this.sendToClients( players, this.instance.get() );
	}

	public void sendToClients( Type message ) {
		this.sendToClients( Side.getServer().getPlayerList().getPlayers(), message );
	}

	public void sendToClients() {
		this.sendToClients( this.instance.get() );
	}

	public void sendToClient( ServerPlayer player, Type message ) {
		this.sendToClients( List.of( player ), message );
	}

	public void sendToClient( ServerPlayer player ) {
		this.sendToClient( player, this.instance.get() );
	}

	public void sendToServer( Type message ) {
		PacketDistributor.sendToServer( new NetworkPayload<>( this.payloadType, message ) );
	}

	public void sendToServer() {
		this.sendToServer( this.instance.get() );
	}

	public void broadcastOnClient( Type message ) {
		this.clientCallbacks.forEach( consumer->consumer.accept( message ) );
	}

	public void broadcastOnServer( Type message, ServerPlayer player ) {
		this.serverCallbacks.forEach( consumer->consumer.accept( message, player ) );
	}

	public void addClientCallback( Consumer< Type > callback ) {
		this.clientCallbacks.add( callback );
	}

	public void addClientCallback( Runnable callback ) {
		this.addClientCallback( data->callback.run() );
	}

	public void addServerCallback( BiConsumer< Type, ServerPlayer > callback ) {
		this.serverCallbacks.add( callback );
	}

	public void addServerCallback( Consumer< ServerPlayer > callback ) {
		this.addServerCallback( ( data, player )->callback.accept( player ) );
	}
}
