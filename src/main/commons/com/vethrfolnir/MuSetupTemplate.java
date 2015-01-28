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
package com.vethrfolnir;

import org.fusesource.jansi.AnsiConsole;

import com.vethrfolnir.logging.MelloriLogHandler;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.services.assets.AssetManager;
import com.vethrfolnir.services.threads.CorvusThreadPool;

import corvus.corax.*;

/**
 * @author Vlad
 *
 */
public abstract class MuSetupTemplate extends CoraxBinder {
	static { // Default Logging
		AnsiConsole.systemInstall();

		// Logging ----- Start
		java.util.logging.Logger logger = java.util.logging.LogManager.getLogManager().getLogger("");
		
		for (java.util.logging.Handler h : logger.getHandlers())
			logger.removeHandler(h);
		
		logger.addHandler(new MelloriLogHandler());
	}

	@Override
	public final void build(Corax corax) {
		Corax.config().loadDirectory("./config/");

		bind(AssetManager.class).as(Scope.Singleton);
		bind(CorvusThreadPool.class).as(Scope.Singleton);
		bind(DataMappingService.class).as(Scope.Singleton);
		setupAction();
	}
	
	@Override
	public void clean() {
		shutdown(Corax.instance());
	}
	
	public abstract void setupAction();
	public abstract void shutdown(Corax corax);
}
