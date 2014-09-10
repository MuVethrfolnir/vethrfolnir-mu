/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.services.assets.processors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vethrfolnir.services.assets.*;
import com.vethrfolnir.services.assets.AssetManager.AssetLoader;

/**
 * @author Vlad
 *
 */
public class InputStreamProcessor implements AssetProcessor {

	/* (non-Javadoc)
	 * @see com.vethrfolnir.services.assets.AssetProcessor#process(com.vethrfolnir.services.assets.AssetManager, com.vethrfolnir.services.assets.AssetKey, com.vethrfolnir.services.assets.AssetManager.AssetLoader, java.io.InputStream)
	 */
	@Override
	public Object process(AssetManager assetManager, AssetKey key, AssetLoader loader, InputStream stream) throws Exception {
		byte[] buff = new byte[stream.available()];
		stream.read(buff);
		
		//TODO IMPORTANT! Wrapper and check if it gets closed!!!
		return new ByteArrayInputStream(buff); // in cases like json mapper
	}

}
