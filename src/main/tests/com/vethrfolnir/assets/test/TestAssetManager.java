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
package com.vethrfolnir.assets.test;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vethrfolnir.services.assets.AssetManager;
import com.vethrfolnir.services.assets.cache.SimpleCache;

import corvus.corax.Corax;
import corvus.corax.processing.PrimeAnnotations;

/**
 * @author Vlad
 *
 */
public class TestAssetManager {

	@BeforeClass
	public static void before() {
		Logger rootLogger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
		((org.apache.logging.log4j.core.Logger)rootLogger).setLevel(Level.INFO);
		

		Logger assetLogger = LogManager.getLogger("com.l2jarchid.assets.AssetManager");
		((org.apache.logging.log4j.core.Logger)assetLogger).setLevel(Level.INFO);

		Corax corax = Corax.create(new PrimeAnnotations());
		corax.addSingleton(AssetManager.class);

		AssetManager assetManager = Corax.getInstance(AssetManager.class);

		assetManager.registerCache(new SimpleCache());
		assetManager.registerProcessor(new TestAssetProcessor());
	}
	
	@Test
	public void Test() {
		final AssetManager assetManager = Corax.getInstance(AssetManager.class);
		
		TestAssetKey key = new TestAssetKey("com/vethrfolnir/MuSetupTemplate.class");
		
		final TestAssetKey keys = new TestAssetKey("src/main/commons/com/vethrfolnir/services/assets");

		// Print result
		System.out.println("Result = "+assetManager.loadAsset(key, TestAssetProcessor.class));
		System.out.println("============================");

		for (int i = 0; i < 128; i++) {
			new Thread(){
				@Override
				public void run() {
					// Print result
					//ArrayList<String> result = 
							assetManager.loadAssets(keys, TestAssetProcessor.class, false);
					//System.out.println("Result = "+result);
					//System.out.println("============================");
				}
			}.start();
		}
		
		System.out.println("Current queued threads ? "+assetManager.getThreadsInUse());
		while(assetManager.getThreadsInUse() > 0);
		assetManager.checkIsThreadPoolNeeded();
	}

	@Test
	public void TestNoLogs() {
		final AssetManager assetManager = Corax.getInstance(AssetManager.class);
		
		TestAssetKey key = new TestAssetKey("com/vethrfolnir/MuSetupTemplate.class");
		
		final TestAssetKey keys = new TestAssetKey("src/main/commons/com/vethrfolnir/services/assets");

		// Print result
		assetManager.loadAsset(key, TestAssetProcessor.class);

		for (int i = 0; i < 128; i++) {
			new Thread(){
				@Override
				public void run() {
					// Print result
					assetManager.loadAssets(keys, TestAssetProcessor.class, false);
				}
			}.start();
			
		}
	}
}
