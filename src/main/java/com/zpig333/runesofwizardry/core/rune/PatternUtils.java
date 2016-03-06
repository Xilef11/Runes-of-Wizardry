/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-06
 */
package com.zpig333.runesofwizardry.core.rune;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
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
	/** Rotates a pattern so that what was at the top (north) is now in the provided direction.
	 *  i.e if the facing is EAST, the east column will contain the "top" row of the pattern.
	 *  This effectively reverses {@link #rotateToFacing(ItemStack[][], EnumFacing)}  
	 * @param patternIn the pattern to rotate
	 * @param facing the direction in which to rotate the pattern
	 * @return a copy of {@code patternIn}, rotated in the {@code facing} direction
	 */
	public static ItemStack[][] rotateAgainstFacing(ItemStack[][] patternIn, EnumFacing facing){
		ItemStack[][] result;
		switch(facing){
		case NORTH: result = patternIn;
					break;//no need to do anything
		case WEST: result = ArrayUtils.rotateCCW(patternIn);
				   break;
		case SOUTH: result = ArrayUtils.rotate180(patternIn);
					break;
		case EAST: result = ArrayUtils.rotateCW(patternIn);
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
		if(first==second)return true;//for efficiency, but should not happen.
		//check the size of the patterns to make sure they match
		if(first.length!=second.length)return false;
		if(first[0].length!=second[0].length)return false;
		//check all dust sets in the pattern
		for(int r=0;r<first.length;r++){
			for(int c=0;c<first[0].length;c++){
				ItemStack secStack = second[r][c];
				ItemStack firstStack = first[r][c];
				if(firstStack!=secStack){//efficiency again
					//if one is null, its not equal to the other (because null==null above)
					if(firstStack==null || secStack == null)return false;
					IDust dust1 = DustRegistry.getDustFromItemStack(firstStack);
					IDust dust2 = DustRegistry.getDustFromItemStack(secStack);
					//if at least one of the dusts accepts the other as a match, its OK.
					if(!(dust1.dustsMatch(firstStack, secStack)
							||dust2.dustsMatch(secStack, firstStack))){
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * Checks if a pattern is empty
	 * @param pattern the pattern to check
	 * @return true if all ItemStacks are null
	 */
	public static boolean isEmpty(ItemStack[][] pattern){
		for(ItemStack[] i:pattern){
			for(ItemStack s:i){
				if(s!=null)return false;
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
		File exportFolder = new File(References.export_folder);
		exportFolder.mkdir();
		Gson gson = JsonUtils.getItemStackGson();
		if(!(name.endsWith(".json")||name.endsWith(".JSON")))name+=".json";
		File file = new File(exportFolder,name);
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
	/**
	 * Returns the ItemStack[][] pattern described by the json file at a ResourceLocation<br/>
	 * Note that the JSON file must have been created by PatternUtils#exportPatternJson (i.e the rw_export command)
	 * @param location the ResourceLocation of the json file, i.e: modid:patterns/pattern.json would point to assets/modid/patterns/pattern.json inside your jar
	 * @return the ItemStack[][] pattern described by the file at <location>
	 * @throws IOException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public static ItemStack[][] importFromJson(ResourceLocation location) throws IOException, JsonSyntaxException, JsonIOException{

		String path = "assets/"+location.getResourceDomain()+"/"+location.getResourcePath();

		//Supposedly the "normal" way to do it, but always return null
		//InputStream in = Loader.instance().getModClassLoader().getResourceAsStream(path);
		//This works (with no initial / only). (seems to work even if not our jar)
		InputStream in = PatternUtils.class.getClassLoader().getResourceAsStream(path);
		
		if(in==null)throw new FileNotFoundException("Could not find file: "+path);
		
		Gson gson = JsonUtils.getItemStackGson();
		Reader read = new BufferedReader(new InputStreamReader(in));
		ItemStack[][] stack = gson.fromJson(read, ItemStack[][].class);
		read.close();
		return stack;
	}
	/**
	 * Returns the ItemStack[][] pattern described by the json file {@code filename} in the export dir (runesofwizardry_patterns)
	 * Note that the JSON file must have been created by PatternUtils#exportPatternJson (i.e the rw_export command)
	 * 
	 * @param filename the name of the file to import. .json will be appended to it if it isn't already
	 * @return the ItemStack[][] pattern described by the file supplied
	 * @throws IOException If the file can't be closed after reading
	 * @throws FileNotFoundException if the supplied filename can't be found
	 */
	public static ItemStack[][] importFromJson(String filename)throws IOException,FileNotFoundException{
		File infile = new File(References.export_folder,filename);
		if(!infile.exists()){
			infile = new File(References.export_folder,filename+".json");
			if(!infile.exists()){
				infile = new File(References.export_folder,filename+".JSON");
				if(!infile.exists()){
					throw new FileNotFoundException("Could not find "+filename+", "+filename+".json, or "+filename+".JSON");
				}
			}
		}
		Reader read = new FileReader(infile);
		Gson gson = JsonUtils.getItemStackGson();
		ItemStack[][] stacks = gson.fromJson(read, ItemStack[][].class);
		read.close();
		return stacks;
	}
	/**
	 * Converts an ItemStack pattern to an array of TileEntityDustPlaced's contents that form the pattern.
	 * It should be assumed that the top-left contents is the north-west corner.
	 * @param pattern the pattern to convert
	 * @return the contents of the TileEntityDustPlaced required to form the pattern
	 */
	public static ItemStack[][][][] toContentsArray(ItemStack[][] pattern){
		int dustrows = pattern.length, dustcols = pattern[0].length;
		int rows = dustrows/TileEntityDustPlaced.ROWS, cols = dustcols/TileEntityDustPlaced.COLS;
		ItemStack[][][][] result = new ItemStack[rows][cols][TileEntityDustPlaced.ROWS][TileEntityDustPlaced.COLS];
		for(int r=0;r<dustrows;r++){
			for(int c=0;c<dustcols;c++){
				result[r/TileEntityDustPlaced.ROWS][c/TileEntityDustPlaced.COLS][r%TileEntityDustPlaced.ROWS][c%TileEntityDustPlaced.COLS]=pattern[r][c];
			}
		}
		return result;
	}
}
