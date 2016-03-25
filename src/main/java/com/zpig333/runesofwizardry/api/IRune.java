/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-08
 */
package com.zpig333.runesofwizardry.api;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.util.Utils;

/** This Abstract class defines a rune created by placing patterns of arcane dust.<br>
 * This should be a singleton class like Items and Blocks.
 * @author Xilef11
 *
 */
public abstract class IRune {
	/** Returns the name of this rune
	 * 
	 * @return the (unlocalized) name of this rune
	 */
	public abstract String getName();
//	/**
//	 * Returns a mod-unique identifier for this type of rune. it will be prefixed with your modid.
//	 * @return the unique ID for this rune
//	 */
//	public abstract String getRuneID();
//	
	/** Returns a n*4 by m*4 array of ItemStacks, where n and m are the number of 
	 *  rows and columns of Minecraft Blocks that make up this Rune.<br/>
	 *  Note that the ItemStacks MUST be stacks of IDust.
	 *  <br/>
	 *  Example: This array would represent the outer border of a block<br/>
	 *  	ItemStack dust = new ItemStack(my_IDust)
	 *  	 {
	 *  		{dust,dust,dust,dust},<br/>
	 *  		{dust,null,null,dust},<br/>
	 *  		{dust,null,null,dust},<br/>
	 *  		{dust,dust,dust,dust}<br/>
	 *  	} 
	 * 
	 *  @Note You can also override IRune#sacrificeMatches(List<ItemStack>) for more complex sacrifice mechanics and oreDictionnary support
	 * @return an ItemStack(IDust) matrix that represents the pattern to place to create the rune
	 */
	public abstract ItemStack[][] getPattern();
	/** returns the position of the entity for this Rune, 
	 * as an offset from the top-left corner (0,0) in the pattern<br/>
	 * Note that these are NOT using the same axis as Minecraft.
	 * <br/>Returning (0,0,0) will place the entity at the top-left corner of the top-left block 
	 * of dust, on the same level as the ground. The z element will be ignored.
	 * @return a vector where the X element is the horizontal offset from the top-left
	 * corner of the pattern, the y element is the vertical offset from that corner and
	 * the z element is the offset on the axis normal to the pattern
	 */
	public abstract Vec3i getEntityPosition();
	/** Returns the items needed to activate this Rune. a negative stack size matches any amount
	 * 
	 * @return a 2D array of ItemStacks, where each row (first coordinate) is a possible combination of ItemStacks that activate the rune.
	 */
	public abstract ItemStack[][] getSacrifice();
	/**
	 * Returns a new instance of the RuneEntity that is created when this rune is formed and activated.
	 *<br/> Note that the sacrifice Items will be consumed before the Entity is created.
	 * @return
	 */
	public abstract RuneEntity createRune(ItemStack[][] actualPattern,EnumFacing front, Set<BlockPos> dusts, TileEntityDustActive entity);
	/**
	 * This Method checks if the dropped ItemStacks match the sacrifice for this rune.
	 * <br/>The given list and required sacrifice are sorted to improve performance.
	 * @param droppedItems the items that were found on the rune
	 * @return true if the sacrifice may activate the rune
	 */
	public boolean sacrificeMatches(List<ItemStack> droppedItems){
		if(this.getSacrifice()==null)return true;//if there is absolutely no sacrifice wanted
		if(droppedItems!=null)droppedItems = Utils.sortAndMergeStacks(droppedItems);
		for(ItemStack[] possibility:this.getSacrifice()){
			if(droppedItems!=null){
				List<ItemStack> wanted = Arrays.asList(possibility);
				wanted = Utils.sortAndMergeStacks(wanted);
				WizardryLogger.logInfo("Comparing sacrifices: "+Arrays.deepToString(wanted.toArray(new ItemStack[0]))+" and "+Arrays.deepToString(droppedItems.toArray(new ItemStack[0])));
				boolean match=true;
				int j=0;
				for(int i=0;i<wanted.size()&&match;i++){
					ItemStack wantStack = wanted.get(i);
					boolean partial=false;
					do{
						ItemStack foundStack = droppedItems.get(i+j);
						partial=Utils.stacksEqualWildcardSize(wantStack, foundStack, allowOredictSacrifice());
						if(!partial&&j==0)match=false;
						j++;
					}while(wantStack.stackSize<0 && partial);//while the found list has items that match the current wildcard item
					j-=2;
				}
				if(match)return true;//if the whole list matched
			}else if(possibility==null){
				return true;//if no sacrifice is an option and we sacrificed nothing
			}
		}
		return false;//no possibility made us return true
	}
	/**
	 * This method checks if the pattern found in world is valid for this rune. This allows to add extra conditions to matching. /!\ DO NOT override unless you REALLY really know what you're doing!
	 * If we ever switch to Java 8 only, this will no longer be necessary because of default methods in interfaces
	 * @param foundPattern the pattern found, rotated so the top matches the top of this rune's pattern
	 * @return true to validate the pattern
	 */
	public boolean patternMatchesExtraCondition(ItemStack[][] foundPattern){
		return true;
	}
	/**
	 * Returns the unlocalized short (tooltip) description of this rune
	 * @return the unlocalized form of the description shown in Runic Dictionary tooltip
	 */
	public String getShortDesc(){
		return getName()+".shortdesc";
	}
	/**
	 * Returns the unlocalized form of information to be added in the "sacrifice" section
	 * @return null for no info.
	 */
	public String getExtraSacrificeInfo(){
		return null;
	}
	/**
	 * Should the OreDictionnary be used to determine if a sacrifice is valid?
	 * @return true by default
	 */
	public boolean allowOredictSacrifice(){
		return true;
	}
	/**
	 * Basic permissions/special conditions handling for the rune. will be checked before consuming the sacrifice
	 * @param player the player attempting to activate the rune
	 * @param world the world in which the rune is being activated
	 * @param activationPos the block right-clicked by the player
	 * @return true by default.
	 */
	public boolean canBeActivatedByPlayer(EntityPlayer player, World world, BlockPos activationPos){
		return true;
	}
}
