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
