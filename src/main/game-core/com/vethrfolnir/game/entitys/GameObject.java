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
	private final SimpleArray<Component> components = new SimpleArray<>();
	
	private String name;
	private MuClient client;
	
	protected long lastUsed;
	protected int index;
	
	private boolean initialized;
	
	protected GameObject(EntityWorld world) {
		this.world = world;
	}

	/**
	 * Commits all the components that have been added, initializing them.
	 */
	public void commit() {
		
		if(!initialized) {
			for (int i = 0; i < components.size(); i++) {
				Component component = components.get(i);

				if(component != null)
					component.initialize(this);
			}
			initialized = true;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.vethrfolnir.tools.Updatable#update(int, float)
	 */
	@Override
	public void update(int tick, float deltaTime) {
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			if(component != null && component instanceof Updatable)
				((Updatable) component).update(tick, deltaTime);
		}
	}

	public void add(Component component) {
		int index = EntityWorld.getComponentIndex(component.getClass()).value;

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
	
	public void destroy() {
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);

			if(component != null)
				component.dispose();
		}

		initialized = false;
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
}
