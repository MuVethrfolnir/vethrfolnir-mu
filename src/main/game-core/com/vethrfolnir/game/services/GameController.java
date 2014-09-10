/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.services;

import java.util.ArrayList;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Updatable;

import corvus.corax.processing.annotation.Config;
import corvus.corax.processing.annotation.Initiate;
import corvus.corax.threads.CorvusThreadPool;

/**
 * @author Vlad
 *
 */
public class GameController implements Runnable {
	private static final MuLogger log = MuLogger.getLogger(GameController.class);
	
	private final ArrayList<Updatable> subscribers = new ArrayList<>();
	private volatile boolean active = true;
	
	@Config(key = "Game.UpdateTicks", value = "10")
	private long GameTicks = 10;
	
	/**
	 * The time it took to update all subscribers on the previous pass.
	 */
	private float deltaTime = 0;
	
	@Initiate
	private void load() {
		GameTicks = 1000 / GameTicks; // XXX Find out what has the best precision.
		log.info("Initialized.");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int tick = 0;
		long lastSample = 0;
		
		while(active) {
        	try {

		        long time = System.currentTimeMillis();

		        if(time - lastSample < GameTicks) {
		        	Thread.sleep(GameTicks);
		            continue;
		        }

		        tick++;
		        lastSample = time;
				long start = System.currentTimeMillis();
		        
				for (int i = 0; i < subscribers.size(); i++) {
					Updatable updatable = subscribers.get(i);
					updatable.update(tick, deltaTime);
				}

				long end = System.currentTimeMillis();
				deltaTime = (end - start) / 1000f;
				//System.out.println("Tick: "+tick +" dt: "+deltaTime);
				if(tick == 10) {
		        	tick = 0;
				}
			}
			catch (Exception e) {
				log.fatal("Error in GameController loop!", e);
			}

		}
	}
	
	public void subscribe(Updatable updatable) {
		subscribers.add(updatable);
	}
	
	public void unsubscribe(Updatable updatable) {
		subscribers.remove(updatable);
	}

	public void start() {
		active = true;
		CorvusThreadPool.getInstance().execute(this);
		log.info("Started.");
	}
	
	public void stop() {
		active = false;
		log.info("Stoped.");
	}
	
	/**
	 * @return the deltaTime
	 */
	public float getDeltaTime() {
		return deltaTime;
	}
	
	/**
	 * @return the subscribers
	 */
	public ArrayList<Updatable> getSubscribers() {
		return subscribers;
	}

}