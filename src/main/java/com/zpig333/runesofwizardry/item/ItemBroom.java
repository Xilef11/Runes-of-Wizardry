/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-04
 */
package com.zpig333.runesofwizardry.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


/**
 * @author Xilef11
 *
 */
public class ItemBroom extends WizardryItem {
	private final String name="broom";

	public ItemBroom(){
		super();
		this.setMaxDamage(63);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		//XXX UPDATE
		//playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}
	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 500;
	}
}
