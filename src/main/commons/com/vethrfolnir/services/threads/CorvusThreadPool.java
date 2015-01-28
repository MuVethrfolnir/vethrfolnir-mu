/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.services.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import corvus.corax.Corax;
import corvus.corax.config.CorvusConfig;
import corvus.corax.inject.Inject;

/**
 * @author Seth
 */
public final class CorvusThreadPool
{
	private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;
	private static final Logger _log = Logger.getLogger(CorvusThreadPool.class.getName());
	
	private ScheduledThreadPoolExecutor _scheduleExecutor;
	private ThreadPoolExecutor _imidiatExecutor;
	private ThreadPoolExecutor _longExecutor;
	
	@Inject
	public void load()
	{
		try
		{
			CorvusConfig config = Corax.config();
			int scheduleCoreSize = config.getProperty("CoraxTScheduleCoreSize", 10);
			int coreSize = config.getProperty("CoraxTImidiatCoreSize", Runtime.getRuntime().availableProcessors());
			int poolSize = config.getProperty("CoraxTImidiatPoolSize", 10);
			int longCoreSize = config.getProperty("CoraxTLongCoreSize", 0);
			
			_scheduleExecutor = new ScheduledThreadPoolExecutor(scheduleCoreSize);
			_imidiatExecutor = new ThreadPoolExecutor(coreSize, poolSize, 5L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
			_longExecutor = new ThreadPoolExecutor(longCoreSize, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
			
			scheduleAtFixedRate(new PurgeTask(_scheduleExecutor, _imidiatExecutor, _longExecutor), 60000, 60000);
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, "Cannot creat thread pool manager!", e);
		}
	}
	
	private final long validate(long delay)
	{
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}

	public final ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period)
	{
		delay = validate(delay);
		period = validate(period);
		return _scheduleExecutor.scheduleAtFixedRate(new ExecutionWrapper(r), delay, period, TimeUnit.MILLISECONDS);
	}

	/**
	 * @param callable
	 * @param later
	 * @param milliseconds
	 * @return 
	 */
	public ScheduledFuture<?> schedule(Runnable callable, long later)
	{
		return _scheduleExecutor.schedule(callable, later, TimeUnit.MILLISECONDS);
	}

	public final void execute(Runnable r)
	{
		_imidiatExecutor.execute(new ExecutionWrapper(r));
	}

	public final void executeLongRunning(Runnable r)
	{
		_longExecutor.execute(new ExecutionWrapper(r));
	}

	public final Future<?> submit(Runnable r)
	{
		return _imidiatExecutor.submit(new ExecutionWrapper(r));
	}

	public final Future<?> submitLongRunning(Runnable r)
	{
		return _longExecutor.submit(new ExecutionWrapper(r));
	}

	public List<String> getStats()
	{
		List<String> list = new ArrayList<String>();
		
		list.add("");
		list.add("Scheduled pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + _scheduleExecutor.getActiveCount());
		list.add("\tgetCorePoolSize: ..... " + _scheduleExecutor.getCorePoolSize());
		list.add("\tgetPoolSize: ......... " + _scheduleExecutor.getPoolSize());
		list.add("\tgetLargestPoolSize: .. " + _scheduleExecutor.getLargestPoolSize());
		list.add("\tgetMaximumPoolSize: .. " + _scheduleExecutor.getMaximumPoolSize());
		list.add("\tgetCompletedTaskCount: " + _scheduleExecutor.getCompletedTaskCount());
		list.add("\tgetQueuedTaskCount: .. " + _scheduleExecutor.getQueue().size());
		list.add("\tgetTaskCount: ........ " + _scheduleExecutor.getTaskCount());
		list.add("");
		list.add("Instant pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + _imidiatExecutor.getActiveCount());
		list.add("\tgetCorePoolSize: ..... " + _imidiatExecutor.getCorePoolSize());
		list.add("\tgetPoolSize: ......... " + _imidiatExecutor.getPoolSize());
		list.add("\tgetLargestPoolSize: .. " + _imidiatExecutor.getLargestPoolSize());
		list.add("\tgetMaximumPoolSize: .. " + _imidiatExecutor.getMaximumPoolSize());
		list.add("\tgetCompletedTaskCount: " + _imidiatExecutor.getCompletedTaskCount());
		list.add("\tgetQueuedTaskCount: .. " + _imidiatExecutor.getQueue().size());
		list.add("\tgetTaskCount: ........ " + _imidiatExecutor.getTaskCount());
		list.add("");
		list.add("Long running pool:");
		list.add("=================================================");
		list.add("\tgetActiveCount: ...... " + _longExecutor.getActiveCount());
		list.add("\tgetCorePoolSize: ..... " + _longExecutor.getCorePoolSize());
		list.add("\tgetPoolSize: ......... " + _longExecutor.getPoolSize());
		list.add("\tgetLargestPoolSize: .. " + _longExecutor.getLargestPoolSize());
		list.add("\tgetMaximumPoolSize: .. " + _longExecutor.getMaximumPoolSize());
		list.add("\tgetCompletedTaskCount: " + _longExecutor.getCompletedTaskCount());
		list.add("\tgetQueuedTaskCount: .. " + _longExecutor.getQueue().size());
		list.add("\tgetTaskCount: ........ " + _longExecutor.getTaskCount());
		list.add("");
		
		return list;
	}

	public void shutdown()
	{
		final long begin = System.currentTimeMillis();

		_log.info("ThreadPoolManager: Shutting down.");
		_log.info("\t... executing " + getTaskCount(_scheduleExecutor) + " scheduled tasks.");
		_log.info("\t... executing " + getTaskCount(_imidiatExecutor) + " instant tasks.");
		_log.info("\t... executing " + getTaskCount(_longExecutor) + " long running tasks.");

		_scheduleExecutor.shutdown();
		_imidiatExecutor.shutdown();
		_longExecutor.shutdown();

		boolean success = false;
		try
		{
			success |= awaitTermination(5000);

			_scheduleExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			_scheduleExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);

			success |= awaitTermination(10000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		_log.info("\t... success: " + success + " in " + (System.currentTimeMillis() - begin) + " msec.");
		_log.info("\t... " + getTaskCount(_scheduleExecutor) + " scheduled tasks left.");
		_log.info("\t... " + getTaskCount(_imidiatExecutor) + " instant tasks left.");
		_log.info("\t... " + getTaskCount(_longExecutor) + " long running tasks left.");
	}

	private int getTaskCount(ThreadPoolExecutor tp)
	{
		return tp.getQueue().size() + tp.getActiveCount();
	}

	private boolean awaitTermination(long timeoutInMillisec) throws InterruptedException
	{
		final long begin = System.currentTimeMillis();

		while(System.currentTimeMillis() - begin < timeoutInMillisec)
		{
			if(!_scheduleExecutor.awaitTermination(10, TimeUnit.MILLISECONDS) && _scheduleExecutor.getActiveCount() > 0)
				continue;

			if(!_imidiatExecutor.awaitTermination(10, TimeUnit.MILLISECONDS) && _imidiatExecutor.getActiveCount() > 0)
				continue;

			if(!_longExecutor.awaitTermination(10, TimeUnit.MILLISECONDS) && _longExecutor.getActiveCount() > 0)
				continue;

			return true;
		}

		return false;
	}

	/**
	 * @return the imidiatExecutor
	 */
	public ThreadPoolExecutor getImidiatExecutor()
	{
		return _imidiatExecutor;
	}
	
	/**
	 * @return the longExecutor
	 */
	public ThreadPoolExecutor getLongExecutor()
	{
		return _longExecutor;
	}
	
	/**
	 * @return the scheduleExecutor
	 */
	public ScheduledThreadPoolExecutor getScheduleExecutor()
	{
		return _scheduleExecutor;
	}

}
