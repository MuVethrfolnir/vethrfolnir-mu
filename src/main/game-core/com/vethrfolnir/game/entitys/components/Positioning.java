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
package com.vethrfolnir.game.entitys.components;

import com.vethrfolnir.game.entitys.Component;
import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.module.StaticData;
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
		currentRegion = StaticData.getRegion(mapId);
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
