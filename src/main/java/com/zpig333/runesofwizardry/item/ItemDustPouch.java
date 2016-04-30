package com.zpig333.runesofwizardry.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.References;

public class ItemDustPouch extends WizardryItem {
	private final String name="dustpouch";
	private static final String DUST_TYPE_TAG="dustType",
								DUST_AMOUNT_TAG="dustAmount";
	public ItemDustPouch(){
		super();
		this.setMaxDamage(63);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemUse(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.util.EnumFacing, float, float, float)
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn,World worldIn, BlockPos pos, EnumFacing side, float hitX,float hitY, float hitZ) {
		ItemStack dustStack = getDustStack(stack, 1);//get a stack of the dust
		//use that stack
		return dustStack!=null? dustStack.getItem().onItemUse(dustStack, playerIn, worldIn, pos, side, hitX, hitY, hitZ) : false;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		ItemStack dustType = getDustStack(stack, 0);
		if(dustType!=null){
			tooltip.add(StatCollector.translateToLocal(dustType.getUnlocalizedName())+" x"+getDustAmount(stack));
		}
	}
	
	/** returns a stack of the dust contained in this pouch with a stacksize of min{dustAmount, amount of dust in the pouch, dust's max stacksize}**/
	@Nullable
	public ItemStack getDustStack(ItemStack pouch, int dustAmount){
		NBTTagCompound tag = pouch.getSubCompound(References.modid, true);
		ItemStack type =ItemStack.loadItemStackFromNBT(tag.getCompoundTag(DUST_TYPE_TAG));
		int amount = tag.getInteger(DUST_AMOUNT_TAG);
		if(type!=null){
			int toGive =Math.min(Math.min(dustAmount, amount),type.getMaxStackSize());
			type = ItemStack.copyItemStack(type);
			type.stackSize=toGive;
			amount-=toGive;
			tag.setInteger(DUST_AMOUNT_TAG, amount);
		}
		return type;
	}
	/** returns the amount of dust in the pouch**/
	public int getDustAmount(ItemStack pouch){
		NBTTagCompound tag = pouch.getSubCompound(References.modid, true);
		return tag.getInteger(DUST_AMOUNT_TAG);
	}
	/** checks if dust may be added to the pouch**/
	public boolean canAddDust(ItemStack pouch, ItemStack dust){
		if(dust==null) return false;
		NBTTagCompound compound = pouch.getSubCompound(References.modid, true);
		ItemStack pouchType = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(DUST_TYPE_TAG));
		int amount = compound.getInteger(DUST_AMOUNT_TAG);
		return pouchType==null || ItemStack.areItemsEqual(dust, pouchType) && ItemStack.areItemStackTagsEqual(dust, pouchType) && amount<Integer.MAX_VALUE-dust.stackSize;
	}
	/** adds dust to the pouch. returns false if the dust could not be added**/
	public boolean addDust(ItemStack pouch, ItemStack dust){
		if(dust==null) return false;
		NBTTagCompound compound = pouch.getSubCompound(References.modid, true);
		ItemStack pouchType = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(DUST_TYPE_TAG));
		int amount = compound.getInteger(DUST_AMOUNT_TAG);
		boolean ok = pouchType==null || ItemStack.areItemsEqual(dust, pouchType) && ItemStack.areItemStackTagsEqual(dust, pouchType) && amount<Integer.MAX_VALUE-dust.stackSize;
		if(ok){
			if(pouchType==null){
				pouchType = ItemStack.copyItemStack(dust);
				pouchType.stackSize=0;
				pouchType.writeToNBT(compound.getCompoundTag(DUST_TYPE_TAG));
			}
			amount+=dust.stackSize;
			compound.setInteger(DUST_AMOUNT_TAG, amount);
		}
		return ok;
	}
	/** empties a pouch. returns true if items were deleted**/
	@Nullable
	public boolean clear(ItemStack pouch){
		NBTTagCompound tag = pouch.getSubCompound(References.modid, true);
		int amount = tag.getInteger(DUST_AMOUNT_TAG);
		ItemStack contents = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(DUST_TYPE_TAG));
		tag.removeTag(DUST_TYPE_TAG);
		tag.removeTag(DUST_AMOUNT_TAG);
		return contents!=null && amount>0;
	}
}
