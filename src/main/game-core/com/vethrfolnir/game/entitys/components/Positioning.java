/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys.components;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.module.Regions;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.staticdata.world.Region;

/**
 * @author Vlad
 *
 */
public class Positioning implements Component {

	protected int x, y, oldX, oldY, heading, mapId;
	private Region currentRegion;
	private GameObject entity;
	
	/**
	 * @param x
	 * @param y
	 * @param mapId
	 */
	public Positioning(int x, int y, int mapId) {
		this.x = x;
		this.y = y;
		this.mapId = mapId;
		currentRegion = Regions.getRegion(mapId);
	}

	@Override
	public void initialize(GameObject entity) {
		this.entity = entity;
		currentRegion.enter(entity);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param heading
	 */
	public void moveTo(int x, int y, int heading) {
		oldX = this.x;
		oldY = this.y;

		this.x = x;
		this.y = y;
		this.heading = heading;

		currentRegion.broadcast(MuPackets.MoveObject, entity);
	}

	public void updateHeading(int heading) {
		this.heading = heading;
		//TODO maybe a heads up packet?
	}
	
	@Override
	public void dispose() {

		if(currentRegion != null) {
			currentRegion.exit(entity);
		}
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return the oldX
	 */
	public int getOldX() {
		return oldX;
	}
	
	/**
	 * @return the oldY
	 */
	public int getOldY() {
		return oldY;
	}
	
	/**
	 * @return the heading
	 */
	public int getHeading() {
		return heading;
	}

	/**
	 * @return
	 */
	public int getMapId() {
		return mapId;
	}
	
	/**
	 * @return the currentRegion
	 */
	public Region getCurrentRegion() {
		return currentRegion;
	}

}
