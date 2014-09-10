/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys;

/**
 * @author Vlad
 *
 */
public class ComponentIndex<T extends Component> {
	public final int value;

	/**
	 * @param index
	 */
	public ComponentIndex() {
		this.value = EntityWorld.componentIndexSize++;
	}

}
