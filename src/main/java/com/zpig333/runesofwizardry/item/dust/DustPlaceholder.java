/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-02-22
 */
package com.zpig333.runesofwizardry.item.dust;

import java.util.List;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.References;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** This class is used for dusts that are to serve as constants/placeholders, i.e "dead", "magic" and "any" dusts
 *	These can be replaced directly in the world and may not be used in runes.
 *  Note that this is not very useful if it dosen't override dustsMatch, because the default will only match the EXACT itemStack.
 * @author Xilef11
 *
 */
public abstract class DustPlaceholder extends IDust {
	private final int color;
	private final String name;
	private final boolean magic;
	public DustPlaceholder(String name,int color, boolean magic){
		this.name=name;
		this.color=color;
		this.magic=magic;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getDustName()
	 */
	@Override
	public String getDustName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getPrimaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getPrimaryColor(ItemStack stack) {
		return color;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getSecondaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getSecondaryColor(ItemStack stack) {
		return color;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getInfusionItems(net.minecraft.item.ItemStack)
	 */
	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#isMagicDust(net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean isMagicDust(ItemStack stack) {
		return magic;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#hasCustomBlock()
	 */
	@Override
	public boolean hasCustomBlock() {
		return true;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getCustomBlock()
	 */
	@Override
	public IDustStorageBlock getCustomBlock() {
		return null;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag){
		tooltip.add(RunesOfWizardry.proxy.translate(References.Lang.PLACEHOLDER));
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#creativeTab()
	 */
	@Override
	public CreativeTabs creativeTab() {
		return ConfigHandler.showPlaceholders? super.creativeTab() : null;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#dustsMatch(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
	 */
	@Override
	public abstract boolean dustsMatch(ItemStack thisDust, ItemStack other);


}
