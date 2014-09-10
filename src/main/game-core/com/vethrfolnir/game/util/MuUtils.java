/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.util;

/**
 * @author Vlad
 *
 */
public class MuUtils {

	public static double distanceSquared(int x1, int y1, int x2, int y2)
	{
		int dx =  x1 - x2;
		int dy =  y1 - y2;

		return Math.sqrt((dx * dx) + (dy * dy));
	}
}
