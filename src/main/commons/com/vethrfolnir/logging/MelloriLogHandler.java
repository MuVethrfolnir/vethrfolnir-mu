/*
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Neither the name of 'Corvus Corax' and 'Corvus Mellori' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (c) 2010-2011 Corvus Corax
 * All rights reserved.
 */
package com.vethrfolnir.logging;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.logging.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Seth
 */
public class MelloriLogHandler extends Handler
{
	public final static HashMap<Level, org.apache.logging.log4j.Level> levels = new HashMap<>();
	
	static {
		levels.put(Level.ALL, org.apache.logging.log4j.Level.ALL);
		levels.put(Level.OFF, org.apache.logging.log4j.Level.OFF);
		levels.put(Level.WARNING, org.apache.logging.log4j.Level.WARN);
		levels.put(Level.SEVERE, org.apache.logging.log4j.Level.ERROR);
		levels.put(Level.INFO, org.apache.logging.log4j.Level.INFO);
		levels.put(Level.CONFIG, org.apache.logging.log4j.Level.DEBUG);
		levels.put(Level.FINE, org.apache.logging.log4j.Level.DEBUG);
		levels.put(Level.FINER, org.apache.logging.log4j.Level.TRACE);
		levels.put(Level.FINEST, org.apache.logging.log4j.Level.TRACE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord record)
	{
		String loggerName = record.getLoggerName();
		if (loggerName == null)
		{
			loggerName = "";
		}
		
		Logger log = LogManager.getLogger(loggerName);
		
		org.apache.logging.log4j.Level level = levels.get(record.getLevel());
		
		if (level == null)
		{
			log.warn("Cannot find log4j level for: " + record.getLevel());
			level = org.apache.logging.log4j.Level.INFO;
		}
		
		String message = (record.getParameters() != null) && (record.getParameters().length > 0) ? MessageFormat.format(record.getMessage(), record.getParameters()) : record.getMessage();
		
		// Resource waster here
		// TODO: Finish all the logging then remove this
		String[] splits = record.getLoggerName().split("\\.");
		
		if(message.contains(splits[splits.length-1]+": "))
			message = message.replace(splits[splits.length-1]+":", "");
		
		log.log(level, message, record.getThrown());
	}
	
	@Override
	public void flush()
	{
	}
	
	@Override
	public void close() throws SecurityException
	{
	}
	
}
