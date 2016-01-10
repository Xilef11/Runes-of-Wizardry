/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-14
 */
package com.zpig333.runesofwizardry.core.rune;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
import com.zpig333.runesofwizardry.util.ArrayUtils;

/** internal Utility/logic methods for Runes
 * @author Xilef11
 *
 */
public class RunesUtil {
	//no instance of this
	private RunesUtil(){}
	/**
	 * Checks if a Rune is properly defined. Will throw an exception if something is wrong 
	 * @param rune the IRune to validate
	 * @throws InvalidRuneException when the given rune is not correctly defined
	 */
	public static void validateRune(IRune rune){
		ItemStack[][] pattern = rune.getPattern();
		int rows = pattern.length;
		//rows must be a multiple of 4
		if(rows % 4 !=0) throw new InvalidRuneException(rune,"The number of rows ("+rows+") is not a multiple of 4");
		
		StringBuilder badRowsBuilder = new StringBuilder();
		for(int i=0;i<rows;i++){
			ItemStack[] row = pattern[i];
			if(row ==null)throw new InvalidRuneException(rune, "Found a null row: "+i);
			//columns must be a multiple of 4
			if((row.length % 4) ==0){
				//Make sure every stack is an IDust and contains 1 item
				for(int j=0;j<row.length;j++){
					ItemStack stack = row[j];
					if(stack!=null){//null stacks are OK
						if(!(stack.getItem() instanceof IDust)) throw new InvalidRuneException(rune,"The Item at position "+i+", "+j+" is not an IDust");
						if(stack.stackSize!=1) throw new InvalidRuneException(rune,"The number of dusts at position "+i+", "+j+" must be 1");
					}
				}
			}else{
				//if multiple rows have a bad # of columns, only 1 exception will be thrown for all of them
				badRowsBuilder.append(i);
				badRowsBuilder.append(" ");
			}
		}
		String badRows = badRowsBuilder.toString();
		if(!badRows.equals("")){
			throw new InvalidRuneException(rune, "The number of columns is not a multiple of 4 for the rows # "+badRows);
		}
		
	}
	/**
	 * Finds and activates (if appropriate) a rune starting at pos
	 * @param pos the position around which to search for a rune
	 * @param world the world in which to search for a rune
	 */
	public static void activateRune(World world, BlockPos pos, EntityPlayer player){
		if(world.isRemote)return;//work on the server only
		TileEntity initial = world.getTileEntity(pos);
		if(initial instanceof TileEntityDustPlaced){
			TileEntityDustPlaced ted = (TileEntityDustPlaced) initial;
			if(ted.isInRune())return;//maybe add message or something
		}else{
			WizardryLogger.logError("activateRune was called on a BlockPos that isn't placed dust!");
			//return;
		}
		PatternFinder finder = new PatternFinder(world, pos);
		finder.search();
		ItemStack pattern[][] = finder.toArray();
		RuneFacing match = matchPattern(pattern);
		if(match==null)return;//might want to eventually do something
		//TODO sacrifice
		ItemStack[] sacrifice=null;
		//OreDictionary.itemMatches(target, input, strict)
		//find the "top-left" corner
		BlockPos topLeft;
		BlockPos entityPos;//BlockPos seems to only have ints, maybe we need to use something else?
		//NORTH is Z-, EAST is X+, UP is Y+
		Vec3i offset = match.rune.getEntityPosition();
		switch(match.top){
		case NORTH: topLeft = finder.getNW();
					entityPos = topLeft.add(offset.getX(), 0, offset.getY());
			break;
		case EAST: topLeft = finder.getNE();
				   entityPos = topLeft.add(-(offset.getY()), 0, offset.getX());
			break;
		case SOUTH: topLeft = finder.getSE();
					entityPos = topLeft.add(-(offset.getX()),0,-(offset.getY()));
			break;
		case WEST:topLeft = finder.getSW();
				  entityPos = topLeft.add(offset.getY(),0,-(offset.getX()));
			break;
		default: throw new IllegalStateException("A rune is facing in an invalid direction: "+match.rune.getName()+" at "+pos+" facing "+match.top);
		}
		WizardryLogger.logInfo("Top-left block is :"+topLeft+" and entity Pos is: "+entityPos);
		//check that the entity position is valid
		if(!finder.getDustPositions().contains(entityPos)){
			throw new IllegalStateException("Tried to create a Rune with invalid entity position"); 
		}
		TileEntity tile = world.getTileEntity(entityPos);
		if(!(tile instanceof TileEntityDustPlaced)){
			throw new IllegalStateException("The TileEntity at "+entityPos+" isn't placed dust!");
		}
		TileEntityDustPlaced toReplace = (TileEntityDustPlaced)tile;
		ItemStack[][] contents = toReplace.getContents();
		//place the rune
		world.removeTileEntity(entityPos);
		
		world.setBlockState(entityPos, WizardryRegistry.dust_placed.getDefaultState().withProperty(BlockDustPlaced.PROPERTYACTIVE, true));
		TileEntity te = world.getTileEntity(entityPos);
		if(!(te instanceof TileEntityDustActive))throw new IllegalStateException("TileEntity not formed!");
		TileEntityDustActive entity = (TileEntityDustActive)te;
		entity.setContents(contents);
		//create the entity
		RuneEntity runeEnt = match.rune.createRune(pattern,match.top, finder.getDustPositions(), entity);
		entity.setRune(runeEnt);
		for(BlockPos p:finder.getDustPositions()){
			TileEntityDustPlaced t = (TileEntityDustPlaced)world.getTileEntity(p);
			t.setRune(runeEnt);
		}
		//entity.setRune(runeEnt);
		entity.updateRendering();
		player.addChatMessage(new ChatComponentText("Formed Rune: "+match.rune.getName()+" facing "+match.top));
		runeEnt.onRuneActivatedbyPlayer(player,sacrifice);
	}
	/**
	 * Returns the IRune that matches a given ItemStack[][] pattern, or null if there isn't one
	 * @return null if there is no match, the IRune match otherwise.
	 */
	private static RuneFacing matchPattern(ItemStack[][] dusts){
		for(IRune rune : DustRegistry.getAllRunes()){
			ItemStack[][] pattern = rune.getPattern();
			//NORTH check
			if(PatternUtils.patternsEqual(pattern, dusts)) return new RuneFacing(rune, EnumFacing.NORTH);
			//EAST
			pattern = ArrayUtils.rotateCW(pattern);
			if(PatternUtils.patternsEqual(pattern, dusts)) return new RuneFacing(rune, EnumFacing.EAST);
			//SOUTH
			pattern = ArrayUtils.rotateCW(pattern);
			if(PatternUtils.patternsEqual(pattern, dusts)) return new RuneFacing(rune, EnumFacing.SOUTH);
			//WEST
			pattern = ArrayUtils.rotateCW(pattern);
			if(PatternUtils.patternsEqual(pattern, dusts)) return new RuneFacing(rune, EnumFacing.WEST);
		}
		return null;
	}
	/**
	 * Represents a pair of IRune and EnumFacing, where the EnumFacing represents the direction of the "top" of the IRune pattern
	 */
	private static class RuneFacing{
		public IRune rune;
		public EnumFacing top;
		public RuneFacing(IRune rune, EnumFacing top){
			this.rune=rune;
			this.top=top;
		}
	}
	
	/** This exception is thrown by {@link RunesUtil#validateRune(IRune)} when the rune is invalid
	 * 
	 * @author Xilef11
	 *
	 */
	public static class InvalidRuneException extends RuntimeException{
		/** constructs an InvalidException with the message and name of the rune
		 * 
		 * @param rune the rune that caused the exception
		 * @param message details on the error
		 */
		public InvalidRuneException(IRune rune, String message){
			super(rune.getName()+": "+message);
		}
	}
}
