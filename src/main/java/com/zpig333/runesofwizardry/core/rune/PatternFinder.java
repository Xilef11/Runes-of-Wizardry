/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-12-14
 */
package com.zpig333.runesofwizardry.core.rune;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

/**
 * This class Finds a pattern of dust around a block in the World.
 * @author Xilef11
 *
 */
public class PatternFinder {
	//easy reference to the World
	private World world;
	private DustElement initial;
	private BlockPos	northMost,
						eastMost,
						southMost,
						westMost;
	private Map<BlockPos, DustElement> map;	
	public PatternFinder(World world,BlockPos initialBlock){
			this.world=world;
			initial = new DustElement(initialBlock);
			map = new HashMap<BlockPos, DustElement>();
			map.put(initialBlock, initial);
			northMost=eastMost=westMost=southMost=initialBlock;
	}
	/**
	 * Launches the search for a dust pattern
	 */
	public void search(){
		try{
			recursiveSearch(initial);
		}catch(StackOverflowError e){
			WizardryLogger.logError("Stack Overflowed");
		}
		WizardryLogger.logInfo("Finished finding dust pattern. Found "+map.size()+" blocks in "+n+" calls");
	}
	//actually creates the structure for dusts
	private int n=0;
	private void recursiveSearch(DustElement current){
		n++;
		if(current.isDusts()){
			//set NESWmost
			BlockPos pos = current.getPos();
			//north is negative Z
			if(pos.getZ()<northMost.getZ())northMost=pos;
			if(pos.getZ()>southMost.getZ())southMost=pos;
			//EAST is X+
			if(pos.getX()>eastMost.getX())eastMost=pos;
			if(pos.getX()<westMost.getX())westMost=pos;
		}
		//find all neighbouring blocks
		for(EnumFacing face:EnumFacing.HORIZONTALS){
			if(current.getNeighbour(face)==null){//if we don't already have a neighbour in that direction
				//get the neighbour in the appropriate direction
				BlockPos next = current.pos.offset(face);
				DustElement neigh = new DustElement(next);
				
				/*this means some blocks (corners between two "live" blocks) will get checked twice,
				 * but ensures we will detect all dusts correctly (and also cleans up the map)
				 */
				if(neigh.isDusts())map.put(next, neigh);
				//max of 1 block gap for the search.
				if(current.isDusts()||neigh.isDusts()){
					recursiveSearch(neigh);
				}
			}
		}
	}
	
	public ItemStack[][] toArray(){
		BlockPos nwCorner = getNW();
		BlockPos seCorner = getSE();
		//horizontal number of blocks
		int blocksX = (seCorner.getX() - nwCorner.getX())+1;
		//vertical number of blocks
		int blocksZ = (seCorner.getZ() - nwCorner.getZ())+1;
		WizardryLogger.logInfo("Converting to array: there are "+blocksX+" horizontal blocks and "+blocksZ+" vertical blocks.\nNW corner is "+nwCorner+" and SE corner is "+seCorner);
		ItemStack[][] result = new ItemStack[blocksZ*TileEntityDustPlaced.ROWS][blocksX*TileEntityDustPlaced.COLS];
		for(int i=0;i<blocksZ;i++){//for each row of blocks
			for(int j=0;j<blocksX;j++){//for each column of blocks
				BlockPos currentPos = nwCorner.east(j).south(i);
				DustElement elem = map.get(currentPos);
				
				for(int r=0;r<TileEntityDustPlaced.ROWS;r++){//each row in the contents
					for(int c=0;c<TileEntityDustPlaced.COLS;c++){//each column in the contents
						int row = i*TileEntityDustPlaced.ROWS + r;
						int col = j*TileEntityDustPlaced.COLS +c;
						//put what dust we found, or nothing if there is nothing
						result[row][col] = elem!=null? elem.dusts[r][c] : null;
					}
				}
			}
		}
		
		return result;
	}
	/**returns the north-west corner of the detected pattern**/
	public BlockPos getNW(){
		return new BlockPos(westMost.getX(), westMost.getY(),northMost.getZ());
	}
	/**returns the South-East corner of the detected pattern**/
	public BlockPos getSE(){
		return new BlockPos(eastMost.getX(), westMost.getY(),southMost.getZ());
	}
	/**returns the South-West corner of the detected pattern**/
	public BlockPos getSW(){
		return new BlockPos(westMost.getX(), westMost.getY(),southMost.getZ());
	}
	/**returns the North-East corner of the detected pattern**/
	public BlockPos getNE(){
		return new BlockPos(eastMost.getX(), westMost.getY(),northMost.getZ());
	}
	
	/**
	 * Represents an element in the doubly linked structure that finds dust patterns
	 * @author Xilef11
	 *
	 */
	private class DustElement{
		//the position of this element
		private final BlockPos pos;
		//contents of the TileEntityDustPlaced
		private final ItemStack[][] dusts;
		
		private DustElement(BlockPos pos){
			this.pos=pos;
			TileEntity ent = world.getTileEntity(pos);
			if(ent!=null && ent instanceof TileEntityDustPlaced){
				TileEntityDustPlaced ted = (TileEntityDustPlaced)ent;
				//XXX this if might not be necessary, because the TE will be removed if it's empty. remove it if performance problems
				dusts= !ted.isEmpty()? ted.getContents() : null;
			}else{
				dusts=null;
			}
		}
		/**
		 * Returns true if the block described by this element is Dust
		 * @return
		 */
		protected boolean isDusts(){
			return dusts!=null;
		}
		protected BlockPos getPos(){
			return pos;
		}
		
		protected DustElement getNeighbour(EnumFacing face){
			return map.get(pos.offset(face));
		}
		
	}
}
