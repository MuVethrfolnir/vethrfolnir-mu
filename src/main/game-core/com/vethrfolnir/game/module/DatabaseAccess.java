/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.module;

import com.vethrfolnir.game.services.DatabaseService;
import com.vethrfolnir.game.services.dao.*;

import corvus.corax.Corax;

/**
 * @author Vlad
 * Faster to have them mapped like this
 */
public class DatabaseAccess {

	private static DatabaseService service;

	//XXX Awaiting interfaces
	private static DAO player, account;

	public static PlayerDAO PlayerAccess() {
		if(player == null)
			player = getDatabaseService().getDAO(PlayerDAO.class);
		
		return (PlayerDAO) player;
	}
	
	public  static AccountDAO AccountAccess() {
		if(account == null)
			account = getDatabaseService().getDAO(AccountDAO.class);
					
		return (AccountDAO) account;
	}

	private static DatabaseService getDatabaseService() {
		if(service == null)
			service = Corax.getInstance(DatabaseService.class);
		
		return service;
	}

}
