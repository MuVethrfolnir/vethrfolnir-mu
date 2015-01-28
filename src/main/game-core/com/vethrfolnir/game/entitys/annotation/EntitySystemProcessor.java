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
package com.vethrfolnir.game.entitys.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import com.vethrfolnir.game.entitys.*;
import com.vethrfolnir.logging.MuLogger;

import corvus.corax.*;
import corvus.corax.util.Tools;

/**
 * @author Vlad
 *
 */
public class EntitySystemProcessor implements CoraxProcessor {

	@Override
	public void process(Describer describer, Corax corax) {
		Object obj = describer.value;
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
	
	@Override
	public boolean isInitializer() {
		return false;
	}
}
