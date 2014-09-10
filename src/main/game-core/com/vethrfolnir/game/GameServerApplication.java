/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game;

import java.io.File;
import java.io.IOException;

import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.module.Regions;
import com.vethrfolnir.game.network.LoginServerClient;
import com.vethrfolnir.game.network.MuNetworkServer;
import com.vethrfolnir.game.network.mu.crypt.MuKeyFactory;
import com.vethrfolnir.game.services.*;
import com.vethrfolnir.tools.Tools;

import corvus.corax.Corax;
import corvus.corax.CorvusConfig;
import corvus.corax.processing.annotation.Initiate;

/**
 * @author Vlad
 *
 */
public class GameServerApplication implements Runnable {

	@Initiate
	private void load() {
		
		try {
			File file = new File(CorvusConfig.WorkingDirectory, "./cache/");
			if(!file.exists()) { // first installment
				file.mkdirs();
				firstInstall(file);
			}
		}
		catch(Exception e) { // Doesn't need logging
			e.printStackTrace();
		}

		Tools.printSection("Database");
		Corax.getInstance(DatabaseFactory.class);
		
		Tools.printSection("Services");
		Corax.getInstance(IdFactory.class);
		Corax.getInstance(DatabaseService.class);
		Corax.getInstance(EntityWorld.class);
		GameController gc = Corax.getInstance(GameController.class);

		
		Tools.printSection("Static Data");
		MuKeyFactory.parse();
		Regions.loadRegions();
		
		Tools.printSection("Scripts");
		
		Tools.printSection("Network");
		LoginServerClient client = Corax.getInstance(LoginServerClient.class);
		MuNetworkServer server = Corax.getInstance(MuNetworkServer.class);

		Tools.printSection("Status");
		client.start();
		server.start();
		gc.start();
	}
	
	private void firstInstall(File cacheDir) throws IOException {
//		DataMappingService service = Corax.getInstance(DataMappingService.class);
//		File file = new File(CorvusConfig.WorkingDirectory, EntityFactory.EntityStatistics);
//		
//		if(!file.exists()) {
//			service.saveSimple(new Statistics(), EntityFactory.EntityStatistics);
//		}
	}

	@Override
	public void run() { // shutdown
		// Clean and save factorys
		Corax.clean();
	}
}
