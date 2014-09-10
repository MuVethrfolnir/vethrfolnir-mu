/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.game.entitys.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import com.vethrfolnir.game.entitys.*;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.Corax;
import corvus.corax.CoraxProcessor;
import corvus.corax.tools.Tools;

/**
 * @author Vlad
 *
 */
public class EntitySystemProcessor implements CoraxProcessor {

	/* (non-Javadoc)
	 * @see corvus.corax.CoraxProcessor#process(java.lang.Object, corvus.corax.Corax)
	 */
	@Override
	public void process(Object obj, Corax corax) {
		Class<?> type = obj.getClass();
		
		Field[] fields = Tools.getFieldsWithAnnotation(FetchIndex.class, type);
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			
			if(!field.isAccessible())
				field.setAccessible(true);

			FetchIndex anno = field.getAnnotation(FetchIndex.class);

			try {
				if(anno.value() != Component.class) {
					ComponentIndex<?> index = EntityWorld.getComponentIndex(anno.value());
					field.set(obj, index);
				}
				else {
					ParameterizedType pt = (ParameterizedType)field.getGenericType();
					
					@SuppressWarnings("unchecked")
					Class<? extends Component> at = (Class<? extends Component>) pt.getActualTypeArguments()[0];
					
					ComponentIndex<?> index = EntityWorld.getComponentIndex(at);
					
					if(index == null)
						MuLogger.f("Failed to parse component["+at.getClass().getSimpleName()+"] index annotation!", new RuntimeException("Assign fail"));
					
					field.set(obj, index);
				}
			}
			catch (Exception e) {
				MuLogger.f("Failed to parse component index annotation!", e);
			}
		}
	}
}
