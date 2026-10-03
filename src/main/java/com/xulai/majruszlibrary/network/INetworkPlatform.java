package com.xulai.majruszlibrary.network;

import com.xulai.majruszlibrary.modhelper.ModHelper;

import java.util.List;

public interface INetworkPlatform {
	void register( ModHelper helper, List< NetworkObject< ? > > objects );
}
