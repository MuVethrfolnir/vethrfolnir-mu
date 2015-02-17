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

import groovy.util.GroovyScriptEngine;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.vethrfolnir.logging.MuLogger;

import corvus.corax.config.CorvusConfig;

/**
 * @author Vlad
 *
 * GroovyScriptEngine is basically a handler for GroovyClassLoader, we should 
 * make our system around GroovyClassLoader than GroovyScriptEngine
 */
public class ScriptingService {
	private static final MuLogger log = MuLogger.getLogger(ScriptingService.class);
	public static final File ScriptDir = new File(CorvusConfig.WorkingDirectory, "system/scripts");
	
	private final GroovyScriptEngine engine;
	
	private Field ScriptEngineRoots;
	
	public ScriptingService() {
		engine = new GroovyScriptEngine(getPackageURLs());
		
		try { // so bad
			ScriptEngineRoots = engine.getClass().getDeclaredField("roots");
			ScriptEngineRoots.setAccessible(true);
		}
		catch (Exception e) {
			log.warn("Failed getting Script engine roots field, new paths wont be added at runtime.", e);
		}
		
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
	 * @return the engine
	 */
	public GroovyScriptEngine getEngine() {
		return engine;
	}
	
	public void reparsePackages() {
		URL[] urls = getPackageURLs();
		
		try { ScriptEngineRoots.set(engine, urls); } catch (Exception e) { log.warn("Hm..", e); }
		
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
			else if (file.getName().endsWith("groovy") || file.getName().endsWith("java")){
				files.add(file);
			}
		}
		
		return files;
	}
}
