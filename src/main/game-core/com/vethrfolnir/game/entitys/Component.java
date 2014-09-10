/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys;

import com.vethrfolnir.tools.Disposable;

/**
 * @author Vlad
 *
 */
public interface Component extends Disposable {
	public void initialize(GameObject entity);
}
