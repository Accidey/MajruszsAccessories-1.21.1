package com.xulai.majruszlibrary;

import com.xulai.majruszlibrary.data.Serializables;
import com.xulai.majruszlibrary.entity.EntityHelper;
import com.xulai.majruszlibrary.modhelper.ModHelper;
import com.xulai.majruszlibrary.network.NetworkObject;

public class MajruszLibrary {
	public static final String MOD_ID = "majruszlibrary";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );

	// Configs
	static {
		HELPER.config( Config.class ).autoSync().create();
	}

	// Network
	public static final NetworkObject< EntityHelper.EntityAnimation > ENTITY_ANIMATION = HELPER.create( "entity_animation", EntityHelper.EntityAnimation.class );
	public static final NetworkObject< EntityHelper.EntityGlow > ENTITY_GLOW = HELPER.create( "entity_glow", EntityHelper.EntityGlow.class );
	public static final NetworkObject< EntityHelper.EntityInvisible > ENTITY_INVISIBLE = HELPER.create( "entity_invisible", EntityHelper.EntityInvisible.class );

	private MajruszLibrary() {}

	public static class Config {
		static {
			Serializables.getStatic( Config.class );
		}
	}
}
