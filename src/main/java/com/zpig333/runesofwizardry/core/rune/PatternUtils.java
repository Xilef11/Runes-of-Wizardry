/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-06
 */
package com.zpig333.runesofwizardry.core.rune;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.util.ArrayUtils;
import com.zpig333.runesofwizardry.util.json.JsonUtils;

/** This class contains utility methods for managing the ItemStack[][] patterns
 * @author Xilef11
 *
 */
public class PatternUtils {

	/** Rotates a given pattern so the top is the facing direction, 
	 * i.e if the top row contains the northmost elements, 
	 * makes it so that the top row contains the [facing]most elements.
	 * @param patternIn an ItemStack[][] where the top row ([0][x]) represents NORTH
	 * @param facing the facing to rotate the pattern to 
	 * @return a new ItemStack[][] which is a rotation of {@code patternIn} so that the top row ([0][x]) represents {@code facing}
	 * @throws IllegalArgumentException if {@code facing} is not horizontal (NESW)
	 **/
	public static ItemStack[][] rotateToFacing(ItemStack[][] patternIn, EnumFacing facing){
		ItemStack[][] result;
		switch(facing){
		case NORTH: result = patternIn;
					break;//no need to do anything
		case WEST: result = ArrayUtils.rotateCW(patternIn);
				   break;
		case SOUTH: result = ArrayUtils.rotate180(patternIn);
					break;
		case EAST: result = ArrayUtils.rotateCCW(patternIn);
					break;
		default: throw new IllegalArgumentException("Facing: "+facing+" is not horizontal!");
		}
		return result;
	}
	/** Checks if two ItemStack[][] patterns of IDust are equal (for rune purposes)
	 * 
	 * @param first the first pattern to compare
	 * @param second the second pattern to compare
	 * @return true if the patterns match, false otherwise (including if any of the ItemStacks do not contain IDusts)
	 */
	public static boolean patternsEqual(ItemStack[][] first, ItemStack[][] second){
		if(first.length!=second.length)return false;
		if(first[0].length!=second[0].length)return false;
		for(int r=0;r<first.length;r++){
			for(int c=0;c<first[0].length;c++){
				ItemStack secStack = second[r][c];
				ItemStack firstStack = first[r][c];
				if(secStack!=null){
					IDust dust = DustRegistry.getDustFromItemStack(secStack);
					if(!dust.dustsMatch(secStack, firstStack))return false;
				}else if(firstStack!=null){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 *  Writes an ItemStack[][] pattern to a JSON file, which is <MC run dir>/runesofwizardry_exported_patterns/<name>.json.
	 *  <br/> Note that if a file with that name already exists, it is saved as <name>_n.json, i.e example_2.json
	 * @param pattern The pattern to save
	 * @param name the name of the file to write
	 * @return the File object representing the written JSON file 
	 * @throws JsonIOException
	 * @throws IOException
	 */
	public static File exportPatternJson(ItemStack[][] pattern, String name) throws JsonIOException, IOException{
		File exportFolder = new File(References.modid+"_exported_patterns");
		exportFolder.mkdir();
		Gson gson = JsonUtils.getItemStackGson();
		File file = new File(exportFolder,name+".json");
		//add file number if it exists
		int n=2;
		while(!file.createNewFile()){
			file = new File(exportFolder, name+"_"+n+".json");
		}
		Writer out = new BufferedWriter(new FileWriter(file)); 
		gson.toJson(pattern,out);
		out.close();
		return file;
	}
}
