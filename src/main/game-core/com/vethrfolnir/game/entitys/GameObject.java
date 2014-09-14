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
package com.vethrfolnir.game.entitys;

import com.vethrfolnir.game.network.mu.MuClient;
import com.vethrfolnir.game.util.SimpleArray;
import com.vethrfolnir.network.WritePacket;
import com.vethrfolnir.tools.Updatable;

/**
 * @author Vlad
 *
 */
public final class GameObject implements Updatable {

	private final EntityWorld world;
	private final SimpleArray<Component> components = new SimpleArray<>(7);
	
	private String name;
	private MuClient client;
	
	protected long lastUsed;
	protected int index, freeIndex;
	
	private boolean initialized;
	private boolean isVisible;
	
	protected volatile boolean isVoid;
	
	protected GameObject(EntityWorld world) {
		this.world = world;
		setVisible(true);
	}

	/**
	 * Commits all the components that have been added, initializing them.
	 */
	public void commit() {
		
		if(!initialized) {
			for (int i = 0; i < components.getCapacity(); i++) {
				Component component = components.get(i);

				if(component != null)
					component.initialize(this);
			}
			initialized = true;
			setVisible(true);
			isVoid = false;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.tools.Updatable#update(int, float)
	 */
	@Override
	public void update(int tick, float deltaTime) {

		if(!initialized)
			return;
		
		for (int i = 0; i < components.getCapacity(); i++) {
			Component component = components.get(i);
			
			if(component != null && component instanceof Updatable)
				((Updatable) component).update(tick, deltaTime);
		}
	}

	public void add(Component component) {
		int index = EntityWorld.getComponentIndex(component.getClass()).value;

		if(this.isPlayer())
			System.out.println("Adding: "+component+ " in index: "+index);

		//components.ensureCapacity(EntityWorld.componentIndexSize);
		components.set(index, component);
	}
	
	public void remove(Component component) {
		components.remove(component);
		component.dispose();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T get(ComponentIndex<T> index) {
		return (T) components.get(index.value);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T get(ComponentIndex<T> index, Class<T> type) {
		return (T) components.get(index.value);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T get(Class<T> type) {
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			if(component != null && component.getClass() == type)
				return (T) component;
		}
		
		return null;
	}

	/**
	 * @param light option only for players, to reset the client's entity data.
	 * It will clear and dispose all components, but don't destroy the entity reference from the world.
	 */
	public void destroy(boolean light) {
		isVoid = true;
		
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);

			if(component != null)
				component.dispose();
			
			components.set(i, null);
		}

		initialized = false;
		
		if(!light)
			world.free(this);
	}

	/**
	 * @return the client
	 */
	public MuClient getClient() {
		return client;
	}
	
	/**
	 * @param client the client to set
	 */
	protected void setClient(MuClient client) {
		this.client = client;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public int getWorldIndex() {
		return index;
	}

	/**
	 * @return the world
	 */
	public EntityWorld getWorld() {
		return world;
	}
	
	/**
	 * @return
	 */
	public boolean isPlayer() {
		return getClient() != null;
	}

	/**
	 * Delegate from client
	 */
	public void sendPacket(WritePacket packet, Object... params) {
		if(isPlayer())
			getClient().sendPacket(packet, params);
	}
	
	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Used to set an npc dead or GM hidden
	 * @return
	 */
	public boolean isVisable() {
		return isVisible;
	}

	/**
	 * Used to set an npc dead or GM hidden
	 * @param isVisible the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return the isVoid
	 */
	public boolean isVoid() {
		return isVoid;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
}
