/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * <http://www.gnu.org/copyleft/gpl.html>
 */
package com.vethrfolnir.tools;

import java.io.File;
import java.net.*;
import java.util.*;

import javax.xml.bind.Unmarshaller.Listener;

/**
 * @author  Setekh
 */
public class Tools
{
	
    private static final HashMap<Class<?>, Class<?>> nativeTypes = new HashMap<>();

    static
    {
        nativeTypes.put(Boolean.class, boolean.class);
        nativeTypes.put(Character.class, char.class);
        nativeTypes.put(Byte.class, byte.class);
        nativeTypes.put(Short.class, short.class);
        nativeTypes.put(Integer.class, int.class);
        nativeTypes.put(Long.class, long.class);
        nativeTypes.put(Float.class, float.class);
        nativeTypes.put(Double.class, double.class);
    }

    /**
     * If it's not a primitive it will return the type parameter
     * @param type
     */
    public static Class<?> identifyAndGet(Class<?> type) {
        return nativeTypes.containsKey(type) ? nativeTypes.get(type) : type;
    }
    
	public static void printSection(String s)
	{
		int maxlength = 79;
		s = "-[ " + s + " ]";
		int slen = s.length();
		
		for (int i = 0; i < (maxlength - slen); i++)
			s = "=" + s;
		
		System.out.println(s);
	}
	
	public static int conMinToMilis(int val)
	{
		return val * 60000;
	}
	
	public static ArrayList<URL> resolvePathUrl(String startDir, String extension) {
		File dir = new File(startDir);
		String[] files = dir.list();
		
		ArrayList<URL> urls = new ArrayList<>();

		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			//System.out.println(f);
			if(f.isDirectory()) {
				ArrayList<URL> rls = resolvePathUrl(f.getPath(), extension);
				urls.addAll(rls);
				continue;
			}

			if(f.getName().endsWith(extension)) {
				try {
					urls.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return urls;
	}
	
	public static ArrayList<URL> resolvePathDirUrl(String startDir) {
		File dir = new File(startDir);
		String[] files = dir.list();
		
		ArrayList<URL> urls = new ArrayList<>();

		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			
			if(f.isDirectory() && f.list().length > 0) {
				ArrayList<URL> rls = resolvePathDirUrl(f.getPath());
				//System.out.println(f +" with: "+rls);
				urls.addAll(rls);
				continue;
			}

			if(f.getParentFile().isDirectory()) {
				//System.out.println("Got: "+f);
				try {
					urls.add(f.getParentFile().toURI().toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return urls;
	}
	
	public static ArrayList<URI> resolvePath(String startDir, String extension) {
		File dir = new File(startDir);
		String[] files = dir.list();
		
		ArrayList<URI> urls = new ArrayList<>();

		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			//System.out.println(f);
			if(f.isDirectory()) {
				ArrayList<URI> rls = resolvePath(f.getPath(), extension);
				urls.addAll(rls);
				continue;
			}

			if(f.getName().endsWith(extension)) {
				urls.add(f.toURI());
			}
		}
		
		return urls;
	}
	
	public static ArrayList<File> resolvePathFile(String startDir, String extension) {
		File dir = new File(startDir);
		String[] files = dir.list();
		
		ArrayList<File> urls = new ArrayList<>();

		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			//System.out.println(f);
			if(f.isDirectory()) {
				ArrayList<File> rls = resolvePathFile(f.getPath(), extension);
				urls.addAll(rls);
				continue;
			}

			if(f.getName().endsWith(extension)) {
				urls.add(f);
			}
		}
		
		return urls;
	}
	
    public static String slashify(String path, boolean isDirectory) {
        String p = path;
        if (File.separatorChar != '/')
            p = p.replace(File.separatorChar, '/');
        if (!p.startsWith("/"))
            p = "/" + p;
        if (!p.endsWith("/") && isDirectory)
            p = p + "/";
        return p;
    }

	/**
	 * @param file
	 * @return
	 * @throws MalformedURLException 
	 */
	public static URL toUrl(File file) throws MalformedURLException
	{
		return new URL("file", "", Tools.slashify(file.getAbsolutePath(), file.isDirectory()));
	}

	public static synchronized void afterUnmarshal(ArrayList<?> list) {
		for (Object object : list) {
			if(object instanceof Listener)
				((Listener) object).afterUnmarshal(null, null);
		}
	}
	public static synchronized void afterUnmarshal(Object object) {
		if(object instanceof Listener)
			((Listener) object).afterUnmarshal(null, null);
	}

	/**
	 * @param data
	 * @return
	 */
	public static byte[] toBytes(int[] data) {
		byte[] toData = new byte[data.length];
		
		for (int i = 0; i < toData.length; i++)
			toData[i] = (byte) data[i];

		return toData;
	}
	
}