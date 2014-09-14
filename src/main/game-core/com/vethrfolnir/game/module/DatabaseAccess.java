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
