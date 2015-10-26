package com.zpig333.runesofwizardry.core;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class WizardryLogger {

	public static void log(Level level, Object obj){
		FMLLog.log(References.modid, level, String.valueOf(obj));
	}

	public static void logFatal(Object obj){
		log(Level.FATAL, obj);
	}

	public static void logError(Object obj){
		log(Level.ERROR, obj);
	}

	public static void logDebug(Object obj){
		log(Level.DEBUG, obj);
	}

	public static void logInfo(Object obj){
		log(Level.INFO, obj);
	}

	public static void logTrace(Object obj){
		log(Level.TRACE, obj);
	}

	public static void logException(Level level, Throwable ex, Object message){
		FMLLog.log(References.modid, level, ex, String.valueOf(message));
	}
}
