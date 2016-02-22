/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-02-22
 */
package com.zpig333.runesofwizardry.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/** THis class manages the config for the mod
 * @author Xilef11
 *
 */
public class ConfigHandler {
	//dev options
	public static final String CAT_DEV="development";
	public static boolean showPlaceholders;
	
	public static Configuration config;
	public static void init(File configFile){
		if(config==null){
			config = new Configuration(configFile);
		}
		loadConfiguration();
	}
	private static void loadConfiguration() {
		//dev
		config.setCategoryComment(CAT_DEV, "Options for addon developers");
		showPlaceholders=config.getBoolean("show placeholders", CAT_DEV, false, "Show the placeholder dusts in the creative menu/JEI");
		
		if(config.hasChanged()){
			config.save();
		}		
	}
}