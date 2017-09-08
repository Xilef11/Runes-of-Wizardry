package com.zpig333.runesofwizardry.item;

import java.util.List;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;

import amerifrance.guideapi.api.GuideAPI;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemDummyGuide extends WizardryItem {
	private final String name="dummyguide";
	
	private static final String TOOLTIP=References.modid+".lang.dummyguide.tooltip";

	public ItemDummyGuide(){
		super();
		this.setMaxStackSize(1);
	}
	@Override
	public String getName(){
		return name;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag){
		if(!RunesOfWizardry.guideApiLoaded){
			tooltip.add(TOOLTIP);
		}
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if(RunesOfWizardry.guideApiLoaded){
			//trade this for the Guide-API version, and open it.
			ItemStack guideBook = GuideAPI.getStackFromBook(WizardryGuide.BOOK);
			playerIn.setHeldItem(hand, guideBook);
			return guideBook.getItem().onItemRightClick(worldIn,playerIn,hand);
		}
		return super.onItemRightClick(worldIn,playerIn,hand);
	}
	
	
	

}
