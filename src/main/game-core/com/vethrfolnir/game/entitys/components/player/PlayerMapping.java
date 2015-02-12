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
package com.vethrfolnir.game.entitys.components.player;

import com.vethrfolnir.game.entitys.ComponentIndex;
import com.vethrfolnir.game.entitys.EntityWorld;
import com.vethrfolnir.game.entitys.components.Positioning;

/**
 * @author Vlad
 *
 */
public class PlayerMapping {

	public static final ComponentIndex<Positioning> Positioning = EntityWorld.getComponentIndex(Positioning.class);
	public static final ComponentIndex<PlayerState> PlayerState = EntityWorld.getComponentIndex(PlayerState.class);
	public static final ComponentIndex<PlayerStats> PlayerStats = EntityWorld.getComponentIndex(PlayerStats.class);
	public static final ComponentIndex<KnownCreatures> KnownCreatures = EntityWorld.getComponentIndex(KnownCreatures.class);
	public static final ComponentIndex<ItemsVewport> ItemsVewport = EntityWorld.getComponentIndex(ItemsVewport.class);
}
