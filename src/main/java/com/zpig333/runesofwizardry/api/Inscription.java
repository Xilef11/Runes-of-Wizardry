package com.zpig333.runesofwizardry.api;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	/**return extra information for charging (xp costs)**/
	@Nullable
	public String getExtraChargeInfo(){
		return null;
	}
	
	/** return the max/initial durability of the inscription
	 * Note: you should use this instead of ItemStack#getMaxDamage() or else things won't work
	 * **/
	public abstract int getMaxDurability();
	
	/**
	 * Returns the unlocalized short (tooltip) description of this inscription
	 * @return the unlocalized form of the description shown in Runic Dictionary and inscription item tooltip
	 */
	public String getShortDesc(){
		return getName()+".shortdesc";
	}
	/** allows to add info to the ItemStack tooltip **/ 
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world,List<String> tooltip, ITooltipFlag advanced){
		
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
	public abstract void onWornTick(World world, EntityPlayer player,ItemStack stack);
	
	/** called when the inscription is charged. return false to cancel charging**/
	public boolean onInscriptionCharged(EntityPlayer player, ItemStack[] sacrifice, boolean negated){
		return true;
	}

	public boolean allowOredictSacrifice() {
		return true;
	}
	/** Allows to control what happens when the inscription is right-clicked while sneaking **/
	public ActionResult<ItemStack> handleRightClick(ItemStack itemStackIn,	World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return new ActionResult<>(EnumActionResult.PASS,itemStackIn);
	}
}
