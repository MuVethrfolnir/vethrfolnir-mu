/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys.annotation;

import java.lang.annotation.*;

import com.vethrfolnir.game.entitys.Component;

/**
 * @author Vlad
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FetchIndex {
	Class<? extends Component> value() default Component.class;
}
