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
package com.vethrfolnir.tools;

import java.util.Collection;
import java.util.Map;

import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public interface Disposable {

	public void dispose();
	
	/**
	 * Will clean later on a different thread, use only if the object should be junked.
	 * Unnecessary considering gc, but someone might get paranoid.
	 * @param obj
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void dispose(final Object obj) {
		CorvusThreadPool.getInstance().execute(()-> {
			if(obj instanceof Disposable)
				((Disposable) obj).dispose();
			
			if(obj instanceof Collection)
				((Collection) obj).clear();

			if(obj instanceof Map)
				((Map) obj).clear();
		});
	}

}
