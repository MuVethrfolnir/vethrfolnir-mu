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
package com.vethrfolnir.services.assets;

import com.vethrfolnir.services.assets.cache.AssetCache;


/**
 * @author Vlad
 *
 */
public abstract class AssetKey {
	private String path, override;
	private long lastModified;
	
	public AssetKey(String path) {
		this.path = path;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		if(getOverride() != null)
			return override;

		return path;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}
	
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * @return the override
	 */
	public String getOverride()
	{
		return override;
	}
	
	/**
	 * @param override the override to set
	 */
	public void setOverride(String override)
	{
		this.override = override;
	}
	
	/**
	 * @return the cacheType
	 */
	public abstract Class<? extends AssetCache> getCacheType();

	public Class<? extends AssetProcessor> getProcessorType() {
		return null;
	}
	
}
