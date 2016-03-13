/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-06
 */
package com.zpig333.runesofwizardry.util.json;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zpig333.runesofwizardry.core.ConfigHandler;

/**
 * @author Xilef11
 *
 */
public class JsonUtils {
	private JsonUtils(){}
	private static Gson itemStackGson = null;
	/**
	 * Returns a Gson configured to work with ItemStacks
	 * @return a new instance if one dosen't exist
	 */
	public static Gson getItemStackGson(){
		if(itemStackGson==null){
			GsonBuilder builder = new GsonBuilder();
			builder.serializeNulls();
			if(ConfigHandler.exportPretty)builder.setPrettyPrinting();
			builder.registerTypeAdapter(ItemStack.class, new ItemStackJson());
			builder.registerTypeAdapter(NBTTagCompound.class, new NBTJson());
			itemStackGson = builder.create();
		}
		return itemStackGson;
	}
	/**
	 * deletes the known instance of the Gson configured for ItemStacks (to free memory)
	 */
	public static void clearItemStackJson(){
		itemStackGson=null;
	}
	
}
