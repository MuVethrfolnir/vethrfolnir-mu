/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
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
