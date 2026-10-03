package com.xulai.majruszlibrary.entity;

import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.math.Range;
import com.xulai.majruszlibrary.text.TextHelper;
import com.xulai.majruszlibrary.time.TimeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Supplier;

public class EffectDef {
	public Supplier< Holder< MobEffect > > effect;
	public int amplifier;
	public float duration;

	static {
		Serializables.get( EffectDef.class )
			.define( "id", Reader.optional( Reader.mobEffect() ), s->s.effect.get() == null ? null : s.effect.get().value(), ( s, v )->s.effect = v == null ? ()->null : ()->BuiltInRegistries.MOB_EFFECT.wrapAsHolder( v ) )
			.define( "amplifier", Reader.integer(), s->s.amplifier, ( s, v )->s.amplifier = Range.of( 0, 10 ).clamp( v ) )
			.define( "duration", Reader.number(), s->s.duration, ( s, v )->s.duration = Range.of( 1.0f, 1000.0f ).clamp( v ) );
	}

	public EffectDef( Holder< MobEffect > effect, int amplifier, float duration ) {
		this.effect = ()->effect;
		this.amplifier = amplifier;
		this.duration = duration;
	}

	public EffectDef( Holder< MobEffect > effect, int amplifier ) {
		this( effect, amplifier, 1.0f );
	}



	public MobEffectInstance toEffectInstance() {
		return new MobEffectInstance( this.effect.get(), TimeHelper.toTicks( this.duration ), this.amplifier );
	}

	public MutableComponent toComponent() {
		Component effectName = this.effect.get().value().getDisplayName();
		Component fullName = this.amplifier > 0 ? TextHelper.translatable( "potion.withAmplifier", effectName.getString(), TextHelper.toRoman( this.amplifier + 1 ) ) : TextHelper.literal( effectName.getString() );

		return TextHelper.translatable( "potion.withDuration", fullName.getString(), TextHelper.toEffectDuration( this.duration ) );
	}
}
