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
package com.vethrfolnir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.xml.bind.Unmarshaller.Listener;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Vlad
 * Which it doesn't have
 */
public class TestJsonAfterUnmarshal {

	
	
	public static void main(String[] args) throws Exception {
		ArrayList<TestThing> tsts = new ArrayList<>();
		
		for (int i = 0; i < 21; i++) {

			final int nr = i;
			tsts.add(new TestThing() {
				{ id = 1028 * nr + 256; name = "Name-"+nr; }
			});
		}
		
		ObjectMapper mp = new ObjectMapper();
		mp.setVisibilityChecker(mp.getDeserializationConfig().getDefaultVisibilityChecker()
		        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
		        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
		        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
		
		mp.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		ByteArrayOutputStream br = new ByteArrayOutputStream();
		mp.writeValue(System.err, tsts);
		mp.writeValue(br, tsts);
		

		ByteArrayInputStream in = new ByteArrayInputStream(br.toByteArray());
		tsts = mp.readValue(in, new TypeReference<ArrayList<TestThing>>() {});

		System.err.println();
		System.out.println("Got: "+tsts);
	}
	
	public static class TestThing extends Listener {

		protected int id = 2;
		protected String name = "def";

		@Override
		public void afterUnmarshal(Object target, Object parent) {
			System.out.println("called!");
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "{Id["+id+"] Name["+name+"]}";
		}
	}
}
