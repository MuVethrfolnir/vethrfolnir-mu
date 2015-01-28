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

import com.vethrfolnir.login.LoginServerApplication;
import com.vethrfolnir.login.services.GameNameService;

import corvus.corax.*;
import corvus.corax.config.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class LoginSetup extends MuSetupTemplate implements Runnable{

	//Test purpose
	static {
		if(!new File("config").exists()) {
			CorvusConfig.WorkingDirectory = new File("./dist/LoginServer");
		}
	}
	
	@Override
	public void setupAction() {
		bind(GameNameService.class).as(Scope.Singleton);;
		bind(LoginServerApplication.class).as(Scope.EagerSingleton);
		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	public static void main(String[] args) {
		Corax.Install(new LoginSetup());
	}

	@Override
	public void shutdown(Corax corax) {
		
	}

	@Override
	public void run() {
		clean(); // meh
	}
}
