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

import java.io.File;
import java.io.InputStream;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.assets.*;
import com.vethrfolnir.services.assets.AssetManager.AssetLoader;
import com.vethrfolnir.tools.Tools;

import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class FileLocator implements AssetLocator {

	private static final MuLogger log = MuLogger.getLogger(FileLocator.class);
	
	private final File root;

	public FileLocator() {
		root = new File(CorvusConfig.WorkingDirectory);
	}
	
	public FileLocator(File root) {
		this.root = root;
	}

	/* (non-Javadoc)
	 * @see com.l2jarchid.assets.locators.AssetLocator#locate(com.l2jarchid.assets.AssetManager, com.l2jarchid.assets.AssetKey)
	 */
	@Override
	public Object locate(AssetManager assetManager, AssetKey key, AssetLoader loader, AssetProcessor processor) {

		File file = new File(root, key.getPath());

		if(file.exists()) {
			Object result = null;
			try (InputStream fis = Tools.toUrl(file).openStream()) {
				result = processor.process(assetManager, key, loader, fis);
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
		return Priority.High;
	}
	
}
