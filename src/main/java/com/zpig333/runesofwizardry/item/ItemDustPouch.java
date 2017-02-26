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
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote)return EnumActionResult.SUCCESS;
		ItemStack stack = playerIn.getHeldItem(hand);
			if(getDustAmount(stack)!=0){
			ItemStack dustStack = getDustStack(stack, 1);//get a stack of the dust
			//use that stack
			addDust(stack, dustStack);//re-add the dust if it wasn't placed
			return dustStack.getItem().onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
			return EnumActionResult.PASS;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		ItemStack dustType = getDustStack(stack, 0);
		if(dustType!=ItemStack.EMPTY){
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
		if(dust!=ItemStack.EMPTY)name+=" ("+dust.getDisplayName()+")";
		return name;
	}
	/** returns a stack of the dust contained in this pouch with a stacksize of min{dustAmount, amount of dust in the pouch, dust's max stacksize}**/
	@Nullable
	public ItemStack getDustStack(ItemStack pouch, int dustAmount){
		NBTTagCompound tag = pouch.getOrCreateSubCompound(References.modid);
		ItemStack type = tag.hasKey(DUST_TYPE_TAG)? 
				new ItemStack(tag.getCompoundTag(DUST_TYPE_TAG)) : ItemStack.EMPTY;
		int amount = tag.getInteger(DUST_AMOUNT_TAG);
		if(type!=ItemStack.EMPTY){
			int toGive =Math.min(Math.min(dustAmount, amount),type.getMaxStackSize());
			type = type.copy();//FIXME dafuq is this line doing?
			//looks like size 0 ItemStacks are automatically converted to Empty...
			type.setCount(toGive==0? 1 : toGive);
			amount-=toGive;
			tag.setInteger(DUST_AMOUNT_TAG, amount);
		}
		return type;
	}
	/** returns the amount of dust in the pouch**/
	public int getDustAmount(ItemStack pouch){
		NBTTagCompound tag = pouch.getOrCreateSubCompound(References.modid);
		return tag.getInteger(DUST_AMOUNT_TAG);
	}
	/** checks if dust may be added to the pouch**/
	public boolean canAddDust(ItemStack pouch, ItemStack dust){
		if(dust==ItemStack.EMPTY) return false;
		NBTTagCompound compound = pouch.getOrCreateSubCompound(References.modid);
		ItemStack pouchType = compound.hasKey(DUST_TYPE_TAG)? 
				new ItemStack(compound.getCompoundTag(DUST_TYPE_TAG)) : ItemStack.EMPTY;
		int amount = compound.getInteger(DUST_AMOUNT_TAG);
		return pouchType==ItemStack.EMPTY || ItemStack.areItemsEqual(dust, pouchType) && ItemStack.areItemStackTagsEqual(dust, pouchType) && amount<Integer.MAX_VALUE-dust.getCount();
	}
	/** adds dust to the pouch. returns false if the dust could not be added**/
	public boolean addDust(ItemStack pouch, ItemStack dust){
		if(dust==ItemStack.EMPTY) return false;
		NBTTagCompound compound = pouch.getOrCreateSubCompound(References.modid);
		ItemStack pouchType = compound.hasKey(DUST_TYPE_TAG)? 
				new ItemStack(compound.getCompoundTag(DUST_TYPE_TAG)) : ItemStack.EMPTY;
		int amount = compound.getInteger(DUST_AMOUNT_TAG);
		boolean ok = pouchType==ItemStack.EMPTY || ItemStack.areItemsEqual(dust, pouchType) && ItemStack.areItemStackTagsEqual(dust, pouchType) && amount<Integer.MAX_VALUE-dust.getCount();
		if(ok){
			if(pouchType==ItemStack.EMPTY){
				pouchType = dust.copy();
				pouchType.setCount(1);
				NBTTagCompound type = new NBTTagCompound();
				pouchType.writeToNBT(type);
				compound.setTag(DUST_TYPE_TAG, type);
				pouch.setItemDamage(1);
			}
			amount+=dust.getCount();
			compound.setInteger(DUST_AMOUNT_TAG, amount);
		}
		return ok;
	}

	/** empties a pouch. returns true if items were deleted**/
	@Nullable
	public boolean clear(ItemStack pouch){
		NBTTagCompound tag = pouch.getOrCreateSubCompound(References.modid);
		int amount = tag.getInteger(DUST_AMOUNT_TAG);
		ItemStack contents = tag.hasKey(DUST_TYPE_TAG)? 
				new ItemStack(tag.getCompoundTag(DUST_TYPE_TAG)) : ItemStack.EMPTY;
		tag.removeTag(DUST_TYPE_TAG);
		tag.removeTag(DUST_AMOUNT_TAG);
		pouch.setItemDamage(0);
		return contents!=ItemStack.EMPTY && amount>0;
	}

}
