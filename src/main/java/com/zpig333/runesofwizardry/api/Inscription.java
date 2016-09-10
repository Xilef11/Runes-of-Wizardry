package com.zpig333.runesofwizardry.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Inscription {
	public static final String NBT_ID="inscription_id";
	/**Returns the unlocalised name for this inscription**/
	public abstract String getName();
	
	/** returns the pattern for this inscription, as per the specifications of the patterns for runes.
	 * Note that this may change in the future to use runic ink
	 * @see IRune#getPattern()
	 */
	public abstract ItemStack[][] getPattern();
	
	/** Returns the items needed to charge this inscription.
	 * 
	 * @return an array of ItemStacks that will charge this inscription up to full durability
	 */
	public abstract ItemStack[] getChargeItems();
	
	/** return the max/initial durability of the inscription**/
	public abstract int getMaxDurability();
	
	/**
	 * Returns the unlocalized short (tooltip) description of this inscription
	 * @return the unlocalized form of the description shown in Runic Dictionary and inscription item tooltip
	 */
	public String getShortDesc(){
		return getName()+".shortdesc";
	}
	
	/**
	 * Basic permissions/special conditions handling for the inscription. will be checked for both giving the inscription item and charging it.
	 * @param player the player attempting to activate the rune
	 * @param world the world in which the rune is being activated
	 * @param activationPos the block right-clicked by the player
	 * @return true by default.
	 */
	public boolean canBeActivatedByPlayer(EntityPlayer player, World world, BlockPos activationPos){
		return true;
	}
	/** This is called on every tick when the inscription is worn (in either baubles or chestplate slot)**/
	public abstract void onWornTick(World world, EntityPlayer player,ItemStack itemStack);
	
	
}
