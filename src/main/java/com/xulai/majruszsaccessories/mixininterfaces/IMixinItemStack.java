package com.xulai.majruszsaccessories.mixininterfaces;

import com.xulai.majruszsaccessories.common.AccessoryHolder;

public interface IMixinItemStack {
	void majruszsaccessories$setAccessoryHolder( AccessoryHolder holder );

	AccessoryHolder majruszsaccessories$getOrCreateAccessoryHolder();
}
