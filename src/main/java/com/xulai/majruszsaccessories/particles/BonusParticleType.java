package com.xulai.majruszsaccessories.particles;

import com.xulai.majruszlibrary.data.Reader;
import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.particles.CustomParticleOptions;
import com.xulai.majruszlibrary.particles.CustomParticleType;
import com.xulai.majruszsaccessories.MajruszsAccessories;

public class BonusParticleType extends CustomParticleType< BonusParticleType.Options > {
	static {
		Serializables.get( Options.class )
			.define( "color", Reader.integer(), s->s.color, ( s, v )->s.color = v );
	}

	public BonusParticleType() {
		super( Options::new );
	}

	public static class Options extends CustomParticleOptions< Options > {
		public int color;

		public Options() {
			super( MajruszsAccessories.BONUS_PARTICLE );
		}

		public Options( int color ) {
			this();

			this.color = color;
		}
	}
}
