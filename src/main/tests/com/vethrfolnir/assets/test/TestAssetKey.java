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
package com.vethrfolnir.assets.test;

import com.vethrfolnir.services.assets.AssetKey;
import com.vethrfolnir.services.assets.cache.AssetCache;
import com.vethrfolnir.services.assets.cache.SimpleCache;

/**
 * @author Vlad
 *
 */
public class TestAssetKey extends AssetKey
{
	
	/**
	 * @param path
	 */
	public TestAssetKey(String path)
	{
		super(path);
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.AssetKey#getCacheType()
	 */
	@Override
	public Class<? extends AssetCache> getCacheType() {
		return SimpleCache.class;
	}
	
}
