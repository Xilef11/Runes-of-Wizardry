/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-02-22
 */
package com.zpig333.runesofwizardry.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** THis class manages the config for the mod
 * @author Xilef11
 *
 */
public class ConfigHandler {
	//dev options
	public static final String CAT_DEV="development";
	public static boolean showPlaceholders;
	public static boolean registerTestRunes;
	//permissions for commands
	public static final String PERMISSIONS_ALL="ALL", PERMISSIONS_OP="OP", PERMISSIONS_NONE="NONE";
	public static String commandImportPermission;
	//xp required to import via dictionary
	public static int dictionaryImportXP;
	public static int dictionaryImportHunger;
	
	public static boolean hardcoreSacrifices;
	public static boolean hardcoreActivation;
	public static boolean deadDustDecay;
	
	//pretty print exported json
	public static boolean exportPretty;
	public static Configuration config;
	public static void init(File configFile){
		if(config==null){
			config = new Configuration(configFile);
		}
		loadConfiguration();
	}
	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.getModID().equals(References.modid)){
			//resync configs
			loadConfiguration();
		}
	}
	private static void loadConfiguration() {
		//dev
		config.setCategoryComment(CAT_DEV, "Options for addon developers");
		showPlaceholders=config.getBoolean("show placeholders", CAT_DEV, false, "Show the placeholder dusts in the creative menu/JEI");
		registerTestRunes=config.getBoolean("register test runes", CAT_DEV, false, "Should the testing runes be registered?");
		config.getCategory(CAT_DEV).get("register test runes").setRequiresMcRestart(true);
		//pretty export
		exportPretty=config.getBoolean("prettyExport", Configuration.CATEGORY_CLIENT, false, "If set to true, exported patterns (rw_export command) will be more readable, but files will be larger");
		//permissions
		commandImportPermission = config.getString("Import pattern command permissions", Configuration.CATEGORY_GENERAL, PERMISSIONS_ALL, "Who can use the import pattern (rw_import) command. [ALL, OP, NONE]", new String[]{PERMISSIONS_ALL,PERMISSIONS_OP,PERMISSIONS_NONE});
		
		dictionaryImportXP = config.getInt("PlaceRuneXP", Configuration.CATEGORY_GENERAL, 0, -1, Integer.MAX_VALUE, "The number of experience levels required to place a rune with the Runic Dictionary. -1 disables placing");
		dictionaryImportXP = config.getInt("PlaceRuneHunger", Configuration.CATEGORY_GENERAL, 0, 0, 40, "The number of hunger points required to place a rune with the Runic Dictionary (2 points per hunger bar).");
		hardcoreSacrifices = config.getBoolean("hardcore sacrifices", Configuration.CATEGORY_GENERAL, false, "If enabled, sacrificing the wrong items will burn up the rune and items.");
		hardcoreActivation=config.getBoolean("hardcore activation", Configuration.CATEGORY_GENERAL, false, "If enabled, attempting to activate a pattern that is not a rune will burn up the pattern.");
		deadDustDecay = config.getBoolean("Decay Dead dust", Configuration.CATEGORY_GENERAL, true, "If enabled, dusts will dissapear gradually after a rune has been deactivated");
		if(config.hasChanged()){
			config.save();
		}		
	}
}
