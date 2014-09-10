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
