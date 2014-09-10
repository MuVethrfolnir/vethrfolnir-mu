/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
