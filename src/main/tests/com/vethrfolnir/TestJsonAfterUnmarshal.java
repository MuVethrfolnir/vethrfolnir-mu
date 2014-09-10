/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.xml.bind.Unmarshaller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import corvus.corax.interfaces.AfterUnmarshal;

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
	
	public static class TestThing implements AfterUnmarshal {

		protected int id = 2;
		protected String name = "def";
		
		/* (non-Javadoc)
		 * @see corvus.corax.interfaces.AfterUnmarshal#afterUnmarshal(javax.xml.bind.Unmarshaller, java.lang.Object)
		 */
		@Override
		public void afterUnmarshal(Unmarshaller u, Object parent) {
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
