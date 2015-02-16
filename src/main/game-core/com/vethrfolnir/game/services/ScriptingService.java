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
package com.vethrfolnir.game.services;

import groovy.util.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.vethrfolnir.logging.MuLogger;
import com.vethrfolnir.tools.Tools;

import corvus.corax.config.CorvusConfig;

/**
 * @author Vlad
 *
 */
public class ScriptingService {
	private static final MuLogger log = MuLogger.getLogger(ScriptingService.class);
	static {
		CorvusConfig.WorkingDirectory = new File("dist/GameServer");
	}
	public static final File ScriptDir = new File(CorvusConfig.WorkingDirectory, "system/scripts");
	
	private final GroovyScriptEngine engine;
	
	public ScriptingService() {
		engine = new GroovyScriptEngine(getPackageURLs());
		log.info("Ready.");
	}
	
	public void loadScripts() {
		reparsePackages();
	
		try {
			ArrayList<File> files = listScriptFiles(ScriptDir);
			for (int i = 0; i < files.size(); i++) {
				File l = files.get(i);
				engine.loadScriptByName(l.getName());
			}
			
			engine.run("MasterScript.groovy", "");
		}
		catch (Exception e) {
			log.fatal("Failed loading master script!", e);
		}
	}
	
	/**
	 * Creates an instance of the specified class, if the class has not been loaded yet, it will be now.
	 * @return the specified initiated object, with the default constructor
	 * @see GroovyScriptManager#loadClass(String)
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T load(String scriptName) {
		try
		{
			T obj = (T) loadClass(scriptName).newInstance();
			return obj;
			
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			log.warn("Failed loading script: "+scriptName+". ", e);
			return null;
		}
	}
	
	/**
	 * Creates an instance of the specified class, if the class has not been loaded yet, it will be now.<br>
	 * Supports native types.
	 * @return the specified initiated object
	 * @see GroovyScriptManager#loadClass(String)
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T loadNative(String scriptName, Object... objects) {
		try
		{
			Class<?>[] types = new Class<?>[objects.length] ;
			
			int pointer = 0;
			for(Object obj : objects) {
				types[pointer++] = Tools.identifyAndGet(obj.getClass());
			}
			
			T obj = (T) load(scriptName, types, objects);
			return obj;
		}
		catch (Exception e)
		{
			log.warn("Failed loading script: "+scriptName+". ", e);
			return null;
		}
	}
	
	/**
	 * Creates an instance of the specified class, if the class has not been loaded yet, it will be now.<br>
	 * <b><font color = #FF0000>Warning:</font></b> It dose not support native types. Use: {@link #loadNative(String, Object...)}
	 * @return the specified initiated object
	 * @see GroovyScriptManager#loadClass(String)
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T load(String scriptName, Object... objects) {
		try
		{
			Class<?>[] types = new Class<?>[objects.length] ;
			
			int pointer = 0;
			for(Object obj : objects) {
				types[pointer++] = obj.getClass();
			}
			
			T obj = (T) load(scriptName, types, objects);
			return obj;
		}
		catch (Exception e)
		{
			log.warn("Failed loading script: "+scriptName+". ", e);
			return null;
		}
	}
	
	/**
	 * Creates an instance of the specified class, if the class has not been loaded yet, it will be now.
	 * @return the specified initiated object
	 * @see GroovyScriptManager#loadClass(String)
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T load(String scriptName, Class<?>[] types, Object... objects) {
		try
		{
			T obj = (T) loadClass(scriptName).getConstructor(types).newInstance(objects);
			return obj;
		}
		catch (Exception e)
		{
			log.warn("Failed loading script: "+scriptName+". ", e);
			return null;
		}
	}
	
	/**
	 * Loads the specified script, will return null if the script is not a returnable object
	 * @param scriptName must not contain file extension
	 * @return class or null
	 */
	public synchronized Class<?> loadClass(String scriptName) {
		try
		{
			Class<?> clz = engine.loadScriptByName(scriptName+ (scriptName.endsWith(".groovy") ? "" : ".groovy"));
			return clz;
		}
		catch (ResourceException | ScriptException e)
		{
			log.warn("Failed loading script: ", e);
		}
		
		return null;
	}

	/**
	 * @return the engine
	 */
	public GroovyScriptEngine getEngine() {
		return engine;
	}
	
	public void reparsePackages() {
		URL[] urls = getPackageURLs();
		
		for(URL url : urls) {
			
			if(!isUsed(url)) {
				engine.getGroovyClassLoader().addURL(url);
			}
		}
	}
	
	private boolean isUsed(URL url) {
		URL[] usedURLS = engine.getGroovyClassLoader().getURLs();
		for(URL link : usedURLS) {
			if(link.equals(url)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static URL[] getPackageURLs() {

		ArrayList<File> files = listDirectories(ScriptDir);
		
		URL[] urls = new URL[files.size()];
		
		for (int i = 0; i < urls.length; i++) {
			File dir = files.get(i);
			try {
				urls[i] = dir.toURI().toURL();
			}
			catch (MalformedURLException e) {
				log.fatal("Failed converting[File="+dir+"] to url.", e);
			}
		}
		return urls;
	}
	
	
	public static ArrayList<File> listDirectories(File file) {
		ArrayList<File> files = new ArrayList<File>();
		
		File[] fs = file.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File dir = fs[i];
			
			if(dir.isDirectory()) {
				files.add(dir);
				files.addAll(listDirectories(dir));
			}
		}
		
		return files;
	}

	private static ArrayList<File> listScriptFiles(File dir) {
		ArrayList<File> files = new ArrayList<File>();
		
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File file = fs[i];
			
			if(file.isDirectory()) {
				files.addAll(listScriptFiles(file));
			}
			else if (file.getName().endsWith("groovy")){
				files.add(file);
			}
		}
		
		return files;
	}
}
