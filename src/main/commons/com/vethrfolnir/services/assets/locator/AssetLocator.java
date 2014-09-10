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
package com.vethrfolnir.services.assets.locator;

import com.vethrfolnir.services.assets.*;
import com.vethrfolnir.services.assets.AssetManager.AssetLoader;

/**
 * @author Vlad
 *
 */
public interface AssetLocator {
	
	/**
	 * A low priority means that even tho we located the asset, we can still override it down the line <br>
	 * A high priority locator will stop the chain and use that locator's asset, if the asset is not located it continues down the chain.
	 */
	public enum Priority {
		Low, High
	}
	
	public Object locate(AssetManager assetManager, AssetKey key, AssetLoader loader, AssetProcessor processor);
	
	public Priority getPriority();
}
