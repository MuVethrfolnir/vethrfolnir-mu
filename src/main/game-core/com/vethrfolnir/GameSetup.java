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

import java.io.File;

import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.game.GameServerApplication;
import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.annotation.EntitySystemProcessor;
import com.vethrfolnir.game.network.LoginServerClient;
import com.vethrfolnir.game.network.MuNetworkServer;
import com.vethrfolnir.game.services.*;

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

	/* (non-Javadoc)
	 * @see com.vethrfolnir.MuSetupTemplate#setupAction()
	 */
	@Override
	public void setupAction() {
		Corax.instance().addProcessor(new EntitySystemProcessor());
		
		bind(MuNetworkServer.class).as(Scope.Singleton);;
		bind(DatabaseService.class).as(Scope.Singleton);;
		bind(DatabaseFactory.class).as(Scope.Singleton);;
		bind(LoginServerClient.class).as(Scope.Singleton);;
		
		bind(EntityWorld.class).as(Scope.Singleton);;
		bind(IdFactory.class).as(Scope.Singleton);;
		bind(GameController.class).as(Scope.Singleton);;

		bind(GameServerApplication.class).as(Scope.EagerSingleton);;
	}

	public static void main(String[] args) {
		Corax.Install(new GameSetup());
	}

	@Override
	public void shutdown(Corax corax) {
	}
	
}
