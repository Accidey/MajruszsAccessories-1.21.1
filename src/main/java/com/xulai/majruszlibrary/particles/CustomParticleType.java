package com.xulai.majruszlibrary.particles;

import com.xulai.majruszlibrary.data.Serializables;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class CustomParticleType< Type extends ParticleOptions > extends ParticleType< Type > {
	private final MapCodec< Type > codec;
	private final StreamCodec< ? super RegistryFriendlyByteBuf, Type > streamCodec;

	public CustomParticleType( Supplier< Type > instance ) {
		super( true );

		this.codec = MapCodec.unit( instance.get() );
		this.streamCodec = StreamCodec.of(
			( buffer, value )->Serializables.write( value, buffer ),
			buffer->Serializables.read( instance.get(), buffer )
		);
	}

	@Override
	public MapCodec< Type > codec() {
		return this.codec;
	}

	@Override
	public StreamCodec< ? super RegistryFriendlyByteBuf, Type > streamCodec() {
		return this.streamCodec;
	}
}
