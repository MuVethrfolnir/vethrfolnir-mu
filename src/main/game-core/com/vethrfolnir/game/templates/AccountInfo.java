/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
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
