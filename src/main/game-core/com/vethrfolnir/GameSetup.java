/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir;

import java.io.File;

import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.game.GameServerApplication;
import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.annotation.EntitySystemProcessor;
import com.vethrfolnir.game.network.LoginServerClient;
import com.vethrfolnir.game.network.MuNetworkServer;
import com.vethrfolnir.game.services.*;

import corvus.corax.Corax;
import corvus.corax.CorvusConfig;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public class GameSetup extends MuSetupTemplate {

	//Test purpose
	static {
		if(!new File("config").exists()) {
			CorvusConfig.WorkingDirectory = "./dist/GameServer";
		}
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.MuSetupTemplate#setupAction()
	 */
	@Override
	public void setupAction() {
		
		installProcessor(new EntitySystemProcessor());
		
		CorvusThreadPool pool = getEngine().getInstanceImpl(CorvusThreadPool.class);
		getEngine().openMonitor(pool.getLongExecutor(), pool.getScheduleExecutor());
		
		addSingleton(MuNetworkServer.class);
		addSingleton(DatabaseService.class);
		addSingleton(DatabaseFactory.class);
		addSingleton(LoginServerClient.class);
		addSingleton(GameServerApplication.class);
		
		addSingleton(EntityWorld.class);
		addSingleton(IdFactory.class);
		addSingleton(GameController.class);
		
	}

	public static void main(String[] args) {
		GameServerApplication app = Corax.create(new GameSetup()).getInstanceImpl(GameServerApplication.class);
		Runtime.getRuntime().addShutdownHook(new Thread(app));
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.MuSetupTemplate#shutdown(corvus.corax.Corax)
	 */
	@Override
	public void shutdown(Corax corax) {
		corax.disposeInstance(MuNetworkServer.class);
		corax.disposeInstance(DatabaseService.class);
		corax.disposeInstance(DatabaseFactory.class);
		corax.disposeInstance(LoginServerClient.class);
		corax.disposeInstance(GameServerApplication.class);
	}
	
}
