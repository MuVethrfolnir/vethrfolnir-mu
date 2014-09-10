/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir;

import corvus.corax.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class RunLoginServerInternal {

	public static void main(String[] args) {
		CorvusConfig.WorkingDirectory = "./dist/LoginServer/";
		LoginSetup.main(args);
	}
}
