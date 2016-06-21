package com.zpig333.runesofwizardry.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.References;

public class ItemDustPouch extends WizardryItem {
	private final String name="dust_pouch";
	private static final String DUST_TYPE_TAG="dustType",
			DUST_AMOUNT_TAG="dustAmount";
	public ItemDustPouch(){
		super();
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
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote)return EnumActionResult.SUCCESS;
		ItemStack dustStack = getDustStack(stack, 1);//get a stack of the dust
		//use that stack
		EnumActionResult placed =  dustStack!=null && dustStack.stackSize>0? dustStack.getItem().onItemUse(dustStack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ) : EnumActionResult.PASS;
		if(placed!=EnumActionResult.SUCCESS)addDust(stack, dustStack);//re-add the dust if it wasn't placed
		return placed;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		ItemStack dustType = getDustStack(stack, 0);
		if(dustType!=null){
			tooltip.add(dustType.getDisplayName()+" x"+getDustAmount(stack));
			//tooltip.add(I18n.translateToLocal(dustType.getUnlocalizedName()+".name")+" x"+getDustAmount(stack));
			if(dustType.getItem()!=null)dustType.getItem().addInformation(dustType, playerIn, tooltip, advanced);
		}
	}



	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getItemStackDisplayName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		ItemStack dust = getDustStack(stack, 0);
		String name = super.getItemStackDisplayName(stack);
		if(dust!=null)name+=" ("+dust.getDisplayName()+")";
		return name;
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
				pouchType.stackSize=1;
				NBTTagCompound type = new NBTTagCompound();
				pouchType.writeToNBT(type);
				compound.setTag(DUST_TYPE_TAG, type);
				pouch.setItemDamage(1);
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
		pouch.setItemDamage(0);
		return contents!=null && amount>0;
	}

}
