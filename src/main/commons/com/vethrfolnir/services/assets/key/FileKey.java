/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.services.assets.key;

import com.vethrfolnir.services.assets.AssetKey;
import com.vethrfolnir.services.assets.AssetProcessor;
import com.vethrfolnir.services.assets.cache.AssetCache;
import com.vethrfolnir.services.assets.processors.InputStreamProcessor;

/**
 * @author Vlad
 *
 */
public class FileKey extends AssetKey {

	/**
	 * @param path
	 */
	public FileKey(String path) {
		super(path);
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.services.assets.AssetKey#getCacheType()
	 */
	@Override
	public Class<? extends AssetCache> getCacheType() {
		return null; // Not cache
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.services.assets.AssetKey#getProcessorType()
	 */
	@Override
	public Class<? extends AssetProcessor> getProcessorType() {
		return InputStreamProcessor.class;
	}
}
