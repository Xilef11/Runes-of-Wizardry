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

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDead;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
import com.zpig333.runesofwizardry.util.ArrayUtils;
import com.zpig333.runesofwizardry.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
	public static RuneStats validateRune(IRune rune){
		ItemStack[][] pattern = rune.getPattern();
		int rows = pattern.length;
		List<ItemStack> dusts = new LinkedList<>();
		//rows must be a multiple of 4
		if(rows % TileEntityDustPlaced.ROWS !=0) throw new InvalidRuneException(rune,"The number of rows ("+rows+") is not a multiple of "+TileEntityDustPlaced.ROWS);

		StringBuilder badRowsBuilder = new StringBuilder();
		for(int i=0;i<rows;i++){
			ItemStack[] row = pattern[i];
			if(row ==null)throw new InvalidRuneException(rune, "Found a null row: "+i);
			//columns must be a multiple of 4
			if((row.length % TileEntityDustPlaced.COLS) ==0){
				//Make sure every stack is an IDust and contains 1 item
				for(int j=0;j<row.length;j++){
					ItemStack stack = row[j];
					if(stack==null) throw new InvalidRuneException(rune, "Some ItemStacks in this Rune's pattern are null. please use ItemStack.EMPTY");
					if(!stack.isEmpty()){//null stacks are OK
						if(!(stack.getItem() instanceof IDust)) throw new InvalidRuneException(rune,"The Item at position "+i+", "+j+" is not an IDust");
						if(stack.getCount()!=1) throw new InvalidRuneException(rune,"The number of dusts at position "+i+", "+j+" must be 1");
						//add to dust cost calculation
						if(!stack.isEmpty())dusts.add(stack.copy());
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
			throw new InvalidRuneException(rune, "The number of columns is not a multiple of "+TileEntityDustPlaced.COLS+" for the rows # "+badRows);
		}
		//stats
		dusts = Utils.sortAndMergeStacks(dusts);
		return new RuneStats(dusts, pattern[0].length/TileEntityDustPlaced.COLS, pattern.length/TileEntityDustPlaced.ROWS, rune.getEntityPosition().getX(), rune.getEntityPosition().getY());
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
			RunesOfWizardry.log().error("activateRune was called on a BlockPos that isn't placed dust!");
			//return;
		}
		PatternFinder finder = new PatternFinder(world, pos);
		finder.search();
		ItemStack pattern[][] = finder.toArray();
		RuneFacing match = matchPattern(pattern);
		if(match==null){
			player.sendMessage(new TextComponentTranslation("runesofwizardry.message.norune"));
			if(ConfigHandler.hardcoreActivation){
				for(BlockPos p:finder.getDustPositions()){
					killDusts(world, p);
				}
			}
			return;
		}
		if(!match.rune.canBeActivatedByPlayer(player, world, pos)){
			RunesOfWizardry.log().info("Player "+player.getName()+" did not have permission to activate "+match.rune.getName()+" at "+world+" pos "+pos);
			return;
		}
		//sacrifice
		ItemStack[] sacrifice=null;
		boolean negated=false;
		Set<EntityItem> sacList=new HashSet<>();
		for(BlockPos p: finder.getDustPositions()){
			sacList.addAll(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p,p.add(1,1,1))));
		}
		List<ItemStack> stacks= new LinkedList<>();
		for(EntityItem e: sacList){
			ItemStack s =e.getItem(); 
			if(s.getItem()==WizardryRegistry.sacrifice_negator){
				negated=true;
			}else{
				//add all items that are not the sacrifice negator
				stacks.add(s.copy());//copy the stack just in case a rune needs it
			}
		}
		RunesOfWizardry.log().info("Found sacrifice: "+Arrays.deepToString(stacks.toArray(new ItemStack[0])));
		//check if sacrifice matches rune
		if(stacks.isEmpty())stacks=null;
		if(!negated){
			if(!match.rune.sacrificeMatches(stacks)){
				//translation OK
				player.sendMessage(new TextComponentTranslation("runesofwizardry.message.badsacrifice", new TextComponentTranslation(match.rune.getName())));
				if(ConfigHandler.hardcoreSacrifices){
					for(EntityItem e:sacList){
						if(world instanceof WorldServer){
							((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, e.posX, e.posY, e.posZ, 1, 0d, 0.5d, 0d, 0d);
						}
						e.setDead();
					}
					for(BlockPos p:finder.getDustPositions()){
						killDusts(world, p);
					}
				}
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
			//if(!sacList.isEmpty())world.playSoundAtEntity(player, "mob.chicken.plop", 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			if(!sacList.isEmpty())world.playSound(null,player.posX, player.posY, player.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.AMBIENT, 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}
		sacrifice = stacks==null? null : stacks.toArray(new ItemStack[stacks.size()]);
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
		RunesOfWizardry.log().info("Top-left block is :"+topLeft+" and entity Pos is: "+entityPos);
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

		world.setBlockState(entityPos, WizardryRegistry.dust_placed.getDefaultState().withProperty(BlockDustPlaced.PROPERTYSTATE, BlockDustPlaced.STATE_ACTIVE));
		TileEntity te = world.getTileEntity(entityPos);
		if(!(te instanceof TileEntityDustActive))throw new IllegalStateException("TileEntity not formed!");
		TileEntityDustActive entity = (TileEntityDustActive)te;
		entity.setContents(contents);
		//create the entity
		RuneEntity runeEnt = match.rune.createRune(match.rotatedPattern,match.top, finder.getDustPositions(), entity);
		entity.setRune(runeEnt);
		for(BlockPos p:finder.getDustPositions()){
			TileEntityDustPlaced t = (TileEntityDustPlaced)world.getTileEntity(p);
			t.setRune(runeEnt);
		}
		//entity.setRune(runeEnt);
		entity.updateRendering();
		RunesOfWizardry.log().info("Formed Rune: "+match.rune.getName()+" facing "+match.top+" by "+player.getDisplayNameString());
		runeEnt.onRuneActivatedbyPlayer(player,sacrifice,negated);
	}
	/**
	 * Returns the IRune that matches a given ItemStack[][] pattern, or null if there isn't one
	 * @return null if there is no match, the IRune match otherwise.
	 */
	private static RuneFacing matchPattern(ItemStack[][] dusts){
		for(IRune rune : DustRegistry.getAllRunes()){
			ItemStack[][] pattern = rune.getPattern();
			//NORTH check
			if(PatternUtils.patternsEqual(pattern, dusts)&&rune.patternMatchesExtraCondition(dusts)) return new RuneFacing(rune, EnumFacing.NORTH,dusts);
			//EAST
			dusts = ArrayUtils.rotateCCW(dusts);
			if(PatternUtils.patternsEqual(pattern, dusts)&&rune.patternMatchesExtraCondition(dusts)) return new RuneFacing(rune, EnumFacing.EAST,dusts);
			//SOUTH
			dusts = ArrayUtils.rotateCCW(dusts);
			if(PatternUtils.patternsEqual(pattern, dusts)&&rune.patternMatchesExtraCondition(dusts)) return new RuneFacing(rune, EnumFacing.SOUTH,dusts);
			//WEST
			dusts = ArrayUtils.rotateCCW(dusts);
			if(PatternUtils.patternsEqual(pattern, dusts)&&rune.patternMatchesExtraCondition(dusts)) return new RuneFacing(rune, EnumFacing.WEST,dusts);
			//rotate the dusts back to north - this is what was causing the wierdness...
			dusts = ArrayUtils.rotateCCW(dusts);
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
			//throw new IllegalStateException("TileEntity wasn't placed dust: "+te);
			Throwable t = new IllegalStateException("TileEntity wasn't placed dust: "+te);
			RunesOfWizardry.log().error("Error deactivating rune (1)",t);
		}
		//set all entities as not in a rune
		for(BlockPos p:rune.dustPositions){
			TileEntity te1 = world.getTileEntity(p);
			if(te1 instanceof TileEntityDustPlaced){
				((TileEntityDustPlaced)te1).setRune(null);
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 3);
			}else{
				//throw new IllegalStateException("TileEntity wasn't placed dust: "+te1);
				Throwable t = new IllegalStateException("TileEntity wasn't placed dust: "+te1);
				RunesOfWizardry.log().error("Error deactivating rune (2)",t);
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
					if(!contents[i][j].isEmpty())contents[i][j]=new ItemStack(WizardryRegistry.dust_dead);
				}
			}
			if(ConfigHandler.deadDustDecay){
				worldIn.removeTileEntity(pos);
				worldIn.setBlockState(pos, WizardryRegistry.dust_placed.getDefaultState().withProperty(BlockDustPlaced.PROPERTYSTATE, BlockDustPlaced.STATE_DEAD));
				en = worldIn.getTileEntity(pos);
				if(!(en instanceof TileEntityDustDead))throw new IllegalStateException("TileEntity not formed!");
				TileEntityDustDead ded = (TileEntityDustDead)en;
				ded.setContents(contents);
			}
			//TODO particles?
			IBlockState state = worldIn.getBlockState(pos);
			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}else{
			RunesOfWizardry.log().error("killDustForEntity was called with a BlockPos that does not have a TileEntityDustPlaced! :"+pos);
		}
	}
	/**
	 * Represents a pair of IRune and EnumFacing, where the EnumFacing represents the direction of the "top" of the IRune pattern
	 */
	private static class RuneFacing{
		public IRune rune;
		public EnumFacing top;
		public ItemStack[][] rotatedPattern;
		public RuneFacing(IRune rune, EnumFacing top,ItemStack[][] rotatedPattern){
			this.rune=rune;
			this.top=top;
			this.rotatedPattern=rotatedPattern;
		}
	}
	/**
	 * This class serves to document various properties of the rune calculated during validation for efficiency
	 * @author Xilef11
	 *
	 */
	public static class RuneStats{
		public final List<ItemStack> dustCosts;
		public final int xsize,ysize;
		public final int centerx,centery;
		private RuneStats(List<ItemStack> dustCosts, int xsize, int ysize,
				int centerx, int centery) {
			this.dustCosts = dustCosts;
			this.xsize = xsize;
			this.ysize = ysize;
			this.centerx = centerx;
			this.centery = centery;
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
