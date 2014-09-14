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
package com.vethrfolnir.game.services.dao;

import java.sql.Connection;
import java.util.concurrent.*;

import com.vethrfolnir.database.DatabaseFactory;
import com.vethrfolnir.game.services.DatabaseService;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.processing.annotation.Inject;
import corvus.corax.threads.CorvusThreadPool;


/**
 * @author Vlad
 *
 * TODO: We need to megrate to Corvus Engine 5, this DAO thingie is really bad :(
 */
public abstract class DAO {
	protected static final MuLogger log = MuLogger.getLogger(DAO.class);
	
	@Inject
	protected DatabaseService service;
	
	@Inject 
	private DatabaseFactory factory;

	
	@SuppressWarnings("unchecked")
	protected <T> T as(Object obj) {
		return (T)obj;
	}

	@SuppressWarnings("unchecked")
	protected <T> T as(Object obj, Class<T> type) {
		return (T)obj;
	}

	/**
	 * Returning null means failed execution
	 * @param proc
	 * @param buff
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T execute(final Procedure<?> proc, final Object... buff) {
		try (Connection con = factory.getConnection()) {
			return (T) proc.perform(con, proc, buff);
		}
		catch (Exception e) {
			log.fatal("Failed enqueue!", e);
		}
		return null;
	}
	
	protected Future<?> enqueue(final VoidProcedure proc, final Object... buff) {
		ThreadPoolExecutor exec = Corax.getInstance(CorvusThreadPool.class).getImidiatExecutor();
		
		Future<?> f = exec.submit(new Callable<Object>() {
			
			@Override
			public Object call() {
				
				try (Connection con = factory.getConnection()) {
					proc.perform(con, buff);
				}
				catch (Exception e) {
					log.fatal("Failed enqueue!", e);
				}
				
				return null;
			}
		});
		
		return f;
	}
	
	/**
	 * @param proc
	 * @param parallel
	 * @return
	 */
	protected Future<?> enqueue(final Procedure<?> proc, final Object... buff) {

		ThreadPoolExecutor exec = Corax.getInstance(CorvusThreadPool.class).getImidiatExecutor();
		
		Future<?> f = exec.submit(new Callable<Object>() {
			
			@Override
			public Object call() {
				return execute(proc, buff);
			}
		});
		
		return f;
	}
	
	protected Future<?> enqueueVoid(final VoidProcedure proc, final Object... buff) {
		return enqueue(proc, buff);
	}

	protected <T> T enqueueAndWait(final Procedure<T> proc, final Object... buff) {
		return awaitFuture(enqueue(proc, buff));
	}
	
	protected <T> T enqueueVoidAndWait(final VoidProcedure proc, final Object... buff) {
		return awaitFuture(enqueue(proc, buff));
	}
	
	/**
	 * @param future
	 * @return
	 */
	protected <T> T awaitFuture(Future<?> future) {
		return awaitFuture(future, null);
	}
	
	/**
	 * @param future
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T awaitFuture(Future<?> future, Class<T> type) {
		try {
			return (T) future.get(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.fatal("Failed awaiting future!", e);
		}
		
		return null;
	}
	
	protected interface VoidProcedure {
		void perform(Connection con, Object... buff) throws Exception;
	}
	
	protected interface Procedure<V> {
		public V perform(Connection con, Procedure<?> proc, Object... buff) throws Exception;
	}
}
