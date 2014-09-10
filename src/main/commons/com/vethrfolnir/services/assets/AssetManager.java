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
package com.vethrfolnir.services.assets;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.assets.cache.AssetCache;
import com.vethrfolnir.services.assets.locator.*;
import com.vethrfolnir.services.assets.processors.InputStreamProcessor;

import corvus.corax.processing.annotation.Finalize;
import corvus.corax.processing.annotation.Initiate;

/**
 * @author Vlad
 *
 * Originally meant for Corvus-Engine
 */
public final class AssetManager {
	private static final MuLogger log = MuLogger.getLogger(AssetManager.class);
	
	private final ArrayList<AssetLocator> locators = new ArrayList<>();
	private final ConcurrentHashMap<Class<? extends AssetCache>, AssetCache> caches = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Class<? extends AssetProcessor>, AssetProcessor> processors = new ConcurrentHashMap<>();
	
	private ExecutorService threadPool;
	private AtomicInteger threadsInUse = new AtomicInteger(0);
	private int nrOfThreads = Runtime.getRuntime().availableProcessors();
	
	@Initiate
	private void load() {
		locators.add(new ClassLocator());
		locators.add(new FileLocator());

		registerProcessor(new InputStreamProcessor());
		
		// init it;
		threadPool();
	}
	
	public <T extends Object> T loadAsset(Class<? extends AssetKey> key, String path) {
		return loadAsset(key, path, null);
	}
	
	public <T extends Object> T loadAsset(Class<? extends AssetKey> key, String path, Class<T> as) {
		try {
			Constructor<? extends AssetKey> cons = key.getConstructor(String.class);
			AssetKey instance = cons.newInstance(path);
			
			return loadAsset(instance, instance.getProcessorType());
		}
		catch (Exception e) {
			log.fatal("Failed creating dynamic key!", e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T loadAsset(final AssetKey key, final Class<? extends AssetProcessor> procType) {

		AssetCache cache = getCache(key.getCacheType());
		
		Object asset;
		if(cache != null && (asset = cache.getAsset(key)) != null) {
			return (T) asset;
		}

		Future<AssetLoader> task = threadPool().submit(new Callable<AssetLoader>() {
			@Override
			public AssetLoader call() throws Exception { // get the result
				AssetLoader loader = new AssetLoader(key, null);
				AssetProcessor processor = processors.get(procType);
				
				if(processor == null)
					throw new RuntimeException("No Asset  Processor for key["+key.getPath()+"]");
				
				top:
				// if its not in a cache trigger this
				for (int i = 0; i < locators.size(); i++) {
					AssetLocator locator = locators.get(i);
					
					Object result = locator.locate(AssetManager.this, key, loader, processor);
					
					if(result != null) {
						loader.result = result;

						switch (locator.getPriority()) {
							case Low:
								continue;
							case High:
								break top;
						}
					}
				}

				return loader;
			}
		});
		
		try
		{
			AssetLoader loader = task.get();

			cache = getCache(key.getCacheType());
			
			if(cache != null) // may be asses with no caches at all.
				cache.setAsset(key, loader.result);

			if(loader.result == null)
				throw new RuntimeException("Asset not found in path = "+key.getPath()+"!");
			
			return (T) loader.result;
		}
		catch (InterruptedException | ExecutionException e) {
			throw new AssetParsingFailException(e);
		}
	}
	
	/**
	 * <strong>Warning!</strong> This method is meant for resources that are outside jars or any type of archives
	 * @param useCache if true The whole list will be cached. It also affects usage, if useCache cache is set as false it wont try to get anything from cache
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> ArrayList<T> loadAssets(final AssetKey key, Class<? extends AssetProcessor> procType, boolean useCache) {
		
		if(useCache) {
			AssetCache cache = getCache(key.getCacheType());
			
			if(cache != null) {
				return new ArrayList<T>((Collection<? extends T>) Arrays.asList(cache.getAssets(key)));
			}
		}

		ArrayList<T> list = new ArrayList<>();
		final AssetProcessor processor = processors.get(procType);
		
		final File root = new File(key.getPath());
		final String[] files = root.list();
		
		ArrayList<Future<AssetLoader>> tasks = new ArrayList<>();
		
		for (int i = 0; i < files.length; i++) {
			String fileName = new String(files[i]);
			final AssetLoader loader = new AssetLoader(key, fileName);
			
			Future<AssetLoader> task = threadPool().submit(new Callable<AssetLoader>() { //TODO a wrapper, maybe, or leave it closure for java 8
				@Override
				public AssetLoader call() throws Exception { // get the result
					top:
					// if its not in a cache trigger this
					for (int j = 0; j < locators.size(); j++) {
						AssetLocator locator = locators.get(j);
						
						Object result = locator.locate(AssetManager.this, key, loader, processor);
						
						if(result != null) {
							loader.result = result;
							
							switch (locator.getPriority()) {
								case Low: // Irrelevant.
									continue;
								case High:
									break top;
							}
						}
					}
					
					if(loader.result == null)
						log.fatal("Failed loading an asset file! File["+key.getPath()+"] not found!");

					return loader;
				}
			});
			
			tasks.add(task);
		}
		
		threadsInUse.addAndGet(tasks.size());
		
		int passes = 0;

		while(!tasks.isEmpty()) {
			for(int i = 0; i < tasks.size(); i++) {
				Future<AssetLoader> task = tasks.get(i);
				
				if(task.isDone() && !task.isCancelled()) {
					try {
						AssetLoader shell = task.get();
						
						if(shell.result != null) {
							list.add((T) shell.result);
						} // else warn?
					}
					catch (InterruptedException | ExecutionException e) {
						log.error("Failed getting asset result! key["+key.getPath()+"]", e);
					}
					tasks.remove(i);
					threadsInUse.decrementAndGet();
				}
				else if(task.isCancelled()) {
					log.fatal("Thread failed completion! "+key);
					tasks.remove(i);
					threadsInUse.decrementAndGet();
				}
				
				// so it wont lock
				try { Thread.sleep(60); } catch (InterruptedException e) { log.error("", e); }
			}

			if(passes == 1000) {
				threadsInUse.set(threadsInUse.get() - tasks.size());
				tasks.clear();
				log.info("Failed loading "+key+". Time out.");
				break;
			}
			passes++;
		}
		
		if(useCache) {
			AssetCache cache = getCache(key.getCacheType());

			if(cache != null) // may be asses with no caches at all.
				cache.setAssets(key, list.toArray());
		}
		
		return list;
	}
	
	@Finalize
	private void clean() {
		locators.clear();
		processors.clear();
		
		for(AssetCache cache : caches.values())
			cache.clear();
		
		caches.clear();
		log.info("Cleanup complete, Preparing threadpool shutdown.");

		if(threadPool != null && !threadPool.isShutdown())
			threadPool.shutdown();
		
		log.info("Threadpool is shutdown.");
	}

	public void registerProcessor(AssetProcessor assetProcessor) {
		processors.put(assetProcessor.getClass(), assetProcessor);
	}

	public void removeProcessor(AssetProcessor assetProcessor) {
		processors.remove(assetProcessor.getClass());
	}

	public void removeProcessor(Class<? extends AssetProcessor> type) {
		processors.remove(type);
	}

	public void registerCache(AssetCache cache) {
		caches.put(cache.getClass(), cache);
	}
	
	public void removeCache(AssetCache cache) {
		caches.remove(cache.getClass());
	}
	
	public void removeCache(Class<? extends AssetCache> type) {
		caches.remove(type);
	}
	
	/**
	 * @return the threadPool
	 */
	private ExecutorService threadPool() {
		if(threadPool == null || !threadPool.isShutdown())
			threadPool = Executors.newFixedThreadPool(nrOfThreads, new ThreadLoader());

		return threadPool;
	}
	
	public boolean checkIsThreadPoolNeeded() {
		if(threadsInUse.get() < 0) {
			log.fatal("The thread counter is out of range! threadsInUse = "+threadsInUse);
			threadsInUse.set(0);
		}
		
		if(threadPool != null && threadsInUse.get() <= 0) {
			try {
				threadPool.shutdown();
				threadPool.awaitTermination(5, TimeUnit.SECONDS);
			}
			catch (InterruptedException e)
			{
				log.fatal("Failed shutting down threadpool!", e);
				return false;
			}
			threadPool = null;
			log.info("Shutting down Asset threadpool! No need for it at the moment.");
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return the threadsInUse
	 */
	public int getThreadsInUse() {
		return threadsInUse.get();
	}
	
	public AssetCache getCache(Class<? extends AssetCache> type) {
		if(type == null)
			return null;

		return caches.get(type);
	}
	
	/**
	 * @return the locators
	 */
	public ArrayList<AssetLocator> getLocators() {
		return locators;
	}
	
	static class ThreadLoader implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
		
		@Override
		public Thread newThread(Runnable r) {
			Thread th = new Thread(r, "AssetLoadingThread-"+(threadNumber.incrementAndGet()+"-Pool-"+poolNumber.incrementAndGet()));
			//System.out.println(th.getName());
			th.setDaemon(true);
			th.setPriority(Thread.MIN_PRIORITY);
			return th;
		}
	}
	
	public class AssetLoader {
		private final AssetKey key;
		private final String override; // if any

		protected Object result;
		
		public AssetLoader(AssetKey key, String override) {
			this.key = key;
			this.override = override;
		}
		
		/**
		 * @return the key
		 */
		public AssetKey getKey() {
			return key;
		}
		
		/**
		 * @return the override
		 */
		public String getOverride() {
			return override;
		}
	}

}
