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
package com.vethrfolnir.services.assets.cache;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

import com.vethrfolnir.services.assets.AssetKey;

/**
 * @author Vlad
 */
public class SmartCache implements AssetCache {

	private final ReferenceQueue<AssetKey> refQueue = new ReferenceQueue<AssetKey>();
	
	private final HashMap<AssetKey, Object> assets = new HashMap<>();
	
	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#setAsset(com.l2jarchid.assets.AssetKey, java.lang.Object)
	 */
	@Override
	public Object setAsset(AssetKey key, Object value) {
		return assets.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#getAsset(com.l2jarchid.assets.AssetKey)
	 */
	@Override
	public Object getAsset(AssetKey key) {
		return assets.get(key);
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#clearAsset(com.l2jarchid.assets.AssetKey)
	 */
	@Override
	public Object clearAsset(AssetKey key) {
		return assets.remove(key);
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#clear()
	 */
	@Override
	public void clear() {
		assets.clear();
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#size()
	 */
	@Override
	public int size() {
		return assets.size();
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#setAssets(com.l2jarchid.assets.AssetKey, java.lang.Object[])
	 */
	@Override
	public Object setAssets(AssetKey key, Object... values)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.cache.AssetCache#getAssets(com.l2jarchid.assets.AssetKey)
	 */
	@Override
	public Object[] getAssets(AssetKey key)
	{
		return null;
	}
	
}