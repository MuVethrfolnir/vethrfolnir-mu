/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir;

import org.fusesource.jansi.AnsiConsole;

import com.vethrfolnir.logging.MelloriLogHandler;
import com.vethrfolnir.services.DataMappingService;
import com.vethrfolnir.services.assets.AssetManager;

import corvus.corax.Corax;
import corvus.corax.CoraxSetupTemplate;
import corvus.corax.processing.ConfigAnnotations;
import corvus.corax.processing.PrimeAnnotations;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public abstract class MuSetupTemplate extends CoraxSetupTemplate {
	static { // Default Logging
		AnsiConsole.systemInstall();

		// Logging ----- Start
		java.util.logging.Logger logger = java.util.logging.LogManager.getLogManager().getLogger("");
		
		for (java.util.logging.Handler h : logger.getHandlers())
			logger.removeHandler(h);
		
		logger.addHandler(new MelloriLogHandler());
	}

	/* (non-Javadoc)
	 * @see corvus.corax.CoraxSetupTemplate#action()
	 */
	@Override
	public final void action() {
		installCorvusConfig("./config/");

		installProcessor(new ConfigAnnotations());
		installProcessor(new PrimeAnnotations());

		addSingleton(AssetManager.class);
		addSingleton(DataMappingService.class);
		addSingleton(CorvusThreadPool.class);
		setupAction();
	}
	
	/* (non-Javadoc)
	 * @see corvus.corax.CoraxSetupTemplate#clean()
	 */
	@Override
	public void clean() {
		Corax corax = Corax.instance();
		corax.disposeInstance(CorvusThreadPool.class);
		corax.disposeInstance(CorvusThreadPool.class);
		corax.disposeInstance(AssetManager.class);
		shutdown(corax);
	}
	
	public abstract void setupAction();
	public abstract void shutdown(Corax corax);

}
