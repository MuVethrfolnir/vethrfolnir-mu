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
package com.vethrfolnir.services.assets.locator;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.assets.*;
import com.vethrfolnir.services.assets.AssetManager.AssetLoader;

/**
 * @author Vlad
 *
 */
public class ClassLocator implements AssetLocator {
	
	private static final MuLogger log = MuLogger.getLogger(ClassLocator.class);

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.locators.AssetLocator#locate(com.l2jarchid.assets.AssetManager, com.l2jarchid.assets.AssetKey)
	 */
	@Override
	public Object locate(AssetManager assetManager, AssetKey key, AssetLoader loader, AssetProcessor processor) {
		
		URL url =  Class.class.getResource("/"+key.getPath());

		if(url != null) {

			Object result = null;
			try (InputStream fis = url.openStream()) {
				result = processor.process(assetManager, key, loader, fis);
			}
			catch (FileNotFoundException e) {
				log.fatal("File not found!", e);
			}
			catch (Exception e) {
				log.fatal("Error!", e);
			}
			finally {
				if(result == null)
					throw new AssetParsingFailException();
			}
			
			return result;
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.locators.AssetLocator#getPriority()
	 */
	@Override
	public Priority getPriority() {
		return Priority.Low;
	}
	
}
