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
package com.vethrfolnir.services.assets.cache;

import com.vethrfolnir.services.assets.AssetKey;


/**
 * @author Vlad
 *
 */
public interface AssetCache {
	
	public Object setAsset(AssetKey key, Object value);
	public Object setAssets(AssetKey key, Object... values);
	
	public Object getAsset(AssetKey key);
	public Object[] getAssets(AssetKey key);
	public Object clearAsset(AssetKey key);
	
	public void clear();
	public int size();
}
