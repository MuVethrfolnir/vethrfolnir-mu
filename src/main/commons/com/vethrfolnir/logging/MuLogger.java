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
package com.vethrfolnir.logging;

import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.AbstractLoggerWrapper;

/**
 * @author Vlad
 *
 */
public class MuLogger extends AbstractLoggerWrapper {

	private static final MuLogger log = MuLogger.getLogger("#SYSTEM");
	
	/**
	 * @param logger
	 * @param name
	 * @param messageFactory
	 */
	public MuLogger(AbstractLogger logger, String name, MessageFactory messageFactory)
	{
		super(logger, name, messageFactory);
	}
	
	public void warning(String msg) {
		warn(msg);
	}

	public void warning(String msg, Throwable t) {
		warn(msg, t);
	}
	
	public void severe(String msg) {
		error(msg);
	}

	public void severe(String msg, Throwable t) {
		error(msg, t);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.logging.log4j.spi.AbstractLogger#error(java.lang.Object, java.lang.Throwable)
	 */
	public static void e(Object message, Throwable t) {
		log.error(message, t);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.logging.log4j.spi.AbstractLogger#fatal(java.lang.Object, java.lang.Throwable)
	 */
	public static void f(Object message, Throwable t) {
		log.fatal(message, t);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.logging.log4j.spi.AbstractLogger#info(java.lang.Object, java.lang.Throwable)
	 */
	public static void i(Object message, Throwable t) {
		log.info(message, t);
	}

	/**
	 * @param message
	 * @param t
	 * @see org.apache.logging.log4j.spi.AbstractLogger#warn(java.lang.Object, java.lang.Throwable)
	 */
	public static void w(Object message, Throwable t) {
		log.warn(message, t);
	}

	/**
	 * @param message
	 * @see org.apache.logging.log4j.spi.AbstractLogger#error(java.lang.Object)
	 */
	public static void e(Object message) {
		log.error(message);
	}

	/**
	 * @param message
	 * @see org.apache.logging.log4j.spi.AbstractLogger#fatal(java.lang.Object)
	 */
	public static void f(Object message) {
		log.fatal(message);
	}

	/**
	 * @param message
	 * @see org.apache.logging.log4j.spi.AbstractLogger#info(java.lang.Object)
	 */
	public static void i(Object message) {
		log.info(message);
	}

	/**
	 * @param message
	 * @see org.apache.logging.log4j.spi.AbstractLogger#warn(java.lang.Object)
	 */
	public static void w(Object message) {
		log.warn(message);
	}

	/**
	 * @param level
	 * @param message
	 * @param e
	 */
	public void log(Level level, String message, Throwable e) {
		super.log(MelloriLogHandler.levels.get(level), message, e);
	}

	/**
	 * @param level
	 * @param message
	 */
	public void log(Level level, String message) {
		super.log(MelloriLogHandler.levels.get(level), message);
	}

	/**
	 * @param level
	 * @param message
	 * @param objects
	 */
	public void log(Level level, String message, Object... objects) {
		super.log(MelloriLogHandler.levels.get(level), message, objects);
	}

	/**
	 * @param level
	 * @return
	 */
	public boolean isLoggable(Level level) {
		return isEnabled(MelloriLogHandler.levels.get(level));
	}

	public static MuLogger getLogger(Class<?> type)
	{
		Logger logger = LogManager.getLogger(type);
		return new MuLogger((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());
	}
	
	public static MuLogger getLogger(String name)
	{
		Logger logger = LogManager.getLogger(name);
		return new MuLogger((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());
	}
}