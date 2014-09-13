/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.util;

import java.util.Collection;
import java.util.Map;

import com.vethrfolnir.tools.Disposable;

import corvus.corax.threads.CorvusThreadPool;


/**
 * @author Vlad
 *
 */
public class CleanUtil {

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
