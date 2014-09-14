/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.assets.test;

import java.io.InputStream;

import com.vethrfolnir.services.assets.AssetKey;
import com.vethrfolnir.services.assets.AssetManager;
import com.vethrfolnir.services.assets.AssetProcessor;
import com.vethrfolnir.services.assets.AssetManager.AssetLoader;

/**
 * @author Vlad
 *
 */
public class TestAssetProcessor implements AssetProcessor {

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.AssetProcessor#process(com.l2jarchid.assets.AssetManager, com.l2jarchid.assets.AssetKey, java.io.InputStream)
	 */
	@Override
	public Object process(AssetManager assetManager, AssetKey key, AssetLoader loader, InputStream stream) throws Exception {
		byte[] data = new byte[stream.available()];
		
		int available = stream.available();
		stream.read(data);
		return new Wrapper(available, loader.getOverride() == null ? key.getPath() : (key.getPath() + "/"+loader.getOverride()), new String(data, "utf-8"));
	}
	
	public class Wrapper {
		public int size;
		public String data;
		private String fileName;

		public Wrapper(int size, String fileName, String data) {
			this.size = size;
			this.fileName = fileName;
			this.data = data;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "File["+fileName+"] Size["+size+"] Data["+new String(data.getBytes(), 0, 2)+"...]";
		}
		
	}
}
