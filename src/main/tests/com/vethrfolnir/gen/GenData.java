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
package com.vethrfolnir.gen;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Lf2SpacesIndenter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.type.TypeFactory;

import de.undercouch.bson4jackson.BsonFactory;

/**
 * @author Vlad
 *
 */
public class GenData {

	public static DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
	public static TypeFactory defaultTypeFactory = TypeFactory.defaultInstance();
	
	static ObjectMapper mp = new ObjectMapper();
	static ObjectMapper bmp = new ObjectMapper(new BsonFactory());
	public static SimpleBeanPropertyFilter theFilter;
	
	
	static {
		defaultPrettyPrinter.indentArraysWith(new Lf2SpacesIndenter());

		mp.setVisibilityChecker(mp.getDeserializationConfig().getDefaultVisibilityChecker()
		        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
		        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
		        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
		
		mp.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		bmp.setVisibilityChecker(bmp.getDeserializationConfig().getDefaultVisibilityChecker()
		        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
		        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
		        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
		
		bmp.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		theFilter = new SimpleBeanPropertyFilter() {
			
			@Override
			public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer) throws Exception {
			    if (include(writer)) {
			    	Field[] fields = bean.getClass().getDeclaredFields();
			    	for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						
						if(field.getType() == Integer.TYPE) {
							int value = (int) field.get(bean);
							if(value <= 0) {
								writer.serializeAsField(bean, jgen, provider);
							}
							else
								super.serializeAsField(bean, jgen, provider, writer);
						}
						else {
							
						}
					}
			    }
			  }

			  @Override
			  protected boolean include(BeanPropertyWriter writer) {
			    return true;
			  }
			  
			};
	}

	public static ObjectMapper getMapper() {
		return mp;
	}
	
	public static ObjectMapper getBinaryMapper() {
		return bmp;
	}
	/**
	 * @return
	 */
	public static ObjectWriter getWriter() {
		return mp.writer(defaultPrettyPrinter);
	}

	public static ObjectWriter getBinaryWriter() {
		return bmp.writer(defaultPrettyPrinter);
	}
	
	public static <T> ArrayList<T> asArrayList(InputStream is, ObjectMapper mp, Class<? extends T> type) throws Exception {
		return mp.readValue(is, defaultTypeFactory.constructCollectionType(ArrayList.class, type));
	}
}
