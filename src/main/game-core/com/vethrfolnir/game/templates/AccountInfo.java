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
package com.vethrfolnir.game.templates;

import java.sql.Date;

/**
 * @author Vlad
 *
 */
public final class AccountInfo {

	public final Date creationDate;
	public final Date lastAccess;
	public final int accessLevel;

	public byte[] passwordHash;
	
	/**
	 * @param accountName
	 * @param passData
	 * @param accessLevel
	 * @param lastAccess
	 */
	public AccountInfo(byte[] passData, int accessLevel, Date lastAccess, Date creationDate) {
		passwordHash = passData;
		this.accessLevel = accessLevel;
		this.lastAccess = lastAccess;
		this.creationDate = creationDate;
	}

	public void clear() {
		passwordHash = null;
	}
}
