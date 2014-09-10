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
package com.vethrfolnir.services;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Lf2SpacesIndenter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.services.assets.AssetManager;
import com.vethrfolnir.services.assets.key.FileKey;

import corvus.corax.CorvusConfig;
import corvus.corax.processing.annotation.Initiate;
import corvus.corax.processing.annotation.Inject;

/**
 * @author Vlad
 * This class is dependent on Asset Manager!
 */
public class DataMappingService {
	private static final MuLogger log = MuLogger.getLogger(DataMappingService.class);
	
	@Inject
	private AssetManager assetManager;

	private ObjectMapper jsonMapper = new ObjectMapper();
	private DefaultPrettyPrinter defaultPrettyPrinter;
	private TypeFactory defaultTypeFactory;
	
	@Initiate
	private void load() {
		if(assetManager == null) {
			throw new RuntimeException("AssetManaget has not been set in your setup! Mapping service cannot be performed!");
		}
		
		defaultTypeFactory = TypeFactory.defaultInstance();

		jsonMapper.setVisibilityChecker(jsonMapper.getDeserializationConfig().getDefaultVisibilityChecker()
		        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
		        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
		        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
		        .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
		
		jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		defaultPrettyPrinter = new DefaultPrettyPrinter();
		defaultPrettyPrinter.indentArraysWith(new Lf2SpacesIndenter());
	}

	public <T> ArrayList<T> asArrayList(Class<? extends T> type, String path) throws Exception {
		InputStream is = assetManager.loadAsset(FileKey.class, path);
		return jsonMapper.readValue(is, defaultTypeFactory.constructCollectionType(ArrayList.class, type));
	}
	
	public <T> T asSimple(Class<? extends T> type, String path) {
		InputStream is = assetManager.loadAsset(FileKey.class, path);
		try {
			return jsonMapper.readValue(is, type);
		}
		catch (Exception e) {
			log.fatal("Failed deserialization of "+type.getSimpleName()+"!", e);
		}
		
		return null;
	}

	public ObjectWriter writer() {
		return jsonMapper.writer(defaultPrettyPrinter);
	}

	/**
	 * @param obj
	 * @param path
	 */
	public void saveSimple(Object obj, String path) {
		try {
			writer().writeValue(new File(CorvusConfig.WorkingDirectory, path), obj);
		}
		catch (Exception e) {
			log.fatal("Failed saving object["+obj.getClass().getSimpleName()+"]!", e);
		}
	}
}
