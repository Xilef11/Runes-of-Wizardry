/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-14
 */
package com.zpig333.runesofwizardry.core.rune;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
		if(match==null){
			player.addChatComponentMessage(new ChatComponentTranslation("runesofwizardry.message.norune"));
			return;
		}
		//sacrifice
		ItemStack[] sacrifice=null;
		boolean negated=false;
		Set<EntityItem> sacList=new HashSet<EntityItem>();
		for(BlockPos p: finder.getDustPositions()){
			sacList.addAll(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p,p.add(1,1,1))));
		}
		List<ItemStack> stacks= new LinkedList<ItemStack>();
		for(EntityItem e: sacList){
			ItemStack s =e.getEntityItem(); 
			stacks.add(s);
			if(s.getItem()==WizardryRegistry.sacrifice_negator){
				negated=true;
			}
		}
		WizardryLogger.logInfo("Found sacrifice: "+Arrays.deepToString(stacks.toArray(new ItemStack[0])));
		//check if sacrifice matches rune
		if(!negated){
			if(!match.rune.sacrificeMatches(stacks)){
				player.addChatComponentMessage(new ChatComponentTranslation("runesofwizardry.message.badsacrifice", StatCollector.translateToLocal(match.rune.getName())));
				return;
			}
			//kill the items
			for(EntityItem e:sacList){
				if(world instanceof WorldServer){
					//SPELL_MOB or SPELL_WITCH or SMOKE_LARGE are also options
					((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, e.posX, e.posY, e.posZ, 1, 0d, 0.5d, 0d, 0d);
				}
				e.setDead();
			}
			if(!sacList.isEmpty())world.playSoundAtEntity(player, "mob.chicken.plop", 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}
		
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
		WizardryLogger.logInfo("Formed Rune: "+match.rune.getName()+" facing "+match.top+" by "+player.getDisplayNameString());
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
	 * This method changes the TileEntityDustActive associated to a rune to a TileEntityDustPlaced with the same contents, effectively deactivating the rune.
	 * @param rune the rune to deactivate
	 */
	public static void deactivateRune(RuneEntity rune){
		ItemStack[][] contents = rune.entity.getContents();
		BlockPos pos = rune.getPos();
		World world = rune.entity.getWorld();
		world.removeTileEntity(pos);
		world.setBlockState(pos,WizardryRegistry.dust_placed.getDefaultState());
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityDustPlaced){
			((TileEntityDustPlaced)te).setContents(contents);
		}else{
			throw new IllegalStateException("TileEntity wasn't placed dust: "+te);
		}
		//set all entities as not in a rune
		for(BlockPos p:rune.dustPositions){
			TileEntity te1 = world.getTileEntity(p);
			if(te1 instanceof TileEntityDustPlaced){
				((TileEntityDustPlaced)te1).setRune(null);
			}else{
				throw new IllegalStateException("TileEntity wasn't placed dust: "+te1);
			}
		}
	}
	/**
	 * Sets all the dust blocks connected to a Rune to dead dust
	 * @param rune the rune for wich to kill all dusts
	 */
	public static void killAllDustsInRune(RuneEntity rune){
		World world = rune.entity.getWorld();
		if(!world.isRemote){
			for(BlockPos p: rune.dustPositions){
				killDusts(world, p);
			}
		}
	}
	/**
	 * Replaces all dusts in the TileEntityDustPlaced given by {@code worldIn} and {@code pos} to dead dust
	 * @param worldIn
	 * @param pos
	 */
	public static void killDusts(World worldIn,BlockPos pos){
		if(worldIn.isRemote)return;//no need to do work on both client and server if we're going to update
		TileEntity en = worldIn.getTileEntity(pos);
		if(en instanceof TileEntityDustPlaced){
			TileEntityDustPlaced ted = (TileEntityDustPlaced)en;
			ItemStack[][] contents = ted.getContents();
			for(int i=0;i<contents.length;i++){
				for(int j=0;j<contents[i].length;j++){
					if(contents[i][j]!=null)contents[i][j]=new ItemStack(WizardryRegistry.dust_dead);
				}
			}
			//TODO particles?
			worldIn.markBlockForUpdate(pos);
		}else{
			WizardryLogger.logError("killDustForEntity was called with a BlockPos that does not have a TileEntityDustPlaced! :"+pos);
		}
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
		private static final long serialVersionUID = -2125761965795670536L;

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
