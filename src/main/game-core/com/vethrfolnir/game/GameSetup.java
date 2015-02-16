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
package com.vethrfolnir.game;

import java.io.File;
import java.io.IOException;

import com.vethrfolnir.MuSetupTemplate;
import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.game.controllers.NpcController;
import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.annotation.EntitySystemProcessor;
import com.vethrfolnir.game.module.StaticData;
import com.vethrfolnir.game.network.LoginServerClient;
import com.vethrfolnir.game.network.MuNetworkServer;
import com.vethrfolnir.game.network.mu.crypt.MuKeyFactory;
import com.vethrfolnir.game.services.*;
import com.vethrfolnir.services.threads.CorvusThreadPool;
import com.vethrfolnir.tools.Tools;

import corvus.corax.Corax;
import corvus.corax.Scope;
import corvus.corax.config.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class GameSetup extends MuSetupTemplate {

	//Test purpose
	static {
		if(!new File("config").exists()) {
			CorvusConfig.WorkingDirectory = new File("./dist/GameServer");
		}
	}

	@Override
	public void setupAction() {
		Corax.instance().addProcessor(new EntitySystemProcessor());

		setDefaultScope(Scope.Singleton);
		bind(MuNetworkServer.class);
		bind(DatabaseService.class);
		bind(DatabaseFactory.class);
		bind(LoginServerClient.class);
		bind(ScriptingService.class);
		
		bind(EntityWorld.class);
		bind(IdFactory.class);
		bind(GameController.class);

		// Controllers
		bind(NpcController.class);
		
		Runtime.getRuntime().addShutdownHook(new Thread(()-> {
			Corax.instance().destroy();
		}));
	}

	@Override
	public void ready() {
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
		Corax.fetch(DatabaseFactory.class);
		
		Tools.printSection("Services");
		Corax.fetch(IdFactory.class);
		Corax.fetch(DatabaseService.class);
		
		EntityWorld world = Corax.fetch(EntityWorld.class);
		GameController gc = Corax.fetch(GameController.class);
		gc.subscribe(world);
		
		Tools.printSection("Static Data");
		MuKeyFactory.parse();
		StaticData.loadData();
		
		Tools.printSection("Scripts");
		ScriptingService scripting = Corax.fetch(ScriptingService.class);
		scripting.loadScripts();
		
		Tools.printSection("Network");
		LoginServerClient client = Corax.fetch(LoginServerClient.class);
		MuNetworkServer server = Corax.fetch(MuNetworkServer.class);

		Tools.printSection("Status");
		client.start();
		server.start();
		gc.start();
	}
	
	private void firstInstall(File cacheDir) throws IOException {
	}
	
	public static void main(String[] args) {
		Corax.Install(new GameSetup());
	}

	@Override
	public void shutdown(Corax corax) {
		corax.getInstance(CorvusThreadPool.class).shutdown();
	}
	
}
