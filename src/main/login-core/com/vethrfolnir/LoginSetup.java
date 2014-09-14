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

import corvus.corax.Corax;
import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class LoginSetup extends MuSetupTemplate implements Runnable{

	//Test purpose
	static {
		if(!new File("config").exists()) {
			CorvusConfig.WorkingDirectory = "./dist/LoginServer";
		}
	}
	
	/* (non-Javadoc)
	 * @see com.vethrfolnir.MuSetupTemplate#setupAction()
	 */
	@Override
	public void setupAction() {
		addSingleton(GameNameService.class);
		addSingleton(LoginServerApplication.class);
		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	public static void main(String[] args) {
		Corax.create(new LoginSetup()).getInstanceImpl(LoginServerApplication.class);
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.MuSetupTemplate#shutdown(corvus.corax.Corax)
	 */
	@Override
	public void shutdown(Corax corax) {
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		clean(); // meh
	}
}
