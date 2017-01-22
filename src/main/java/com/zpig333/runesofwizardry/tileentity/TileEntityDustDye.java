package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.ItemDustPouch;


public class TileEntityDustDye extends TileEntity{
	//only 1 slot for now, might change if dyes are required as input
	private DyedDustItemStackHandler inventory = new DyedDustItemStackHandler(this);
	//the currently selected color
	private String colorString;
	//was this block powered last time we checked
	private boolean pastRedstoneState=false;
	
	public TileEntityDustDye(){
		super();
		colorString="Color";
		//colorString=I18n.translateToLocal(References.Lang.COLOR);
	}
	public void dye(int color){
		//technically, this stack should not be modified
		ItemStack dust = inventory.getStackInSlot(0);
		if(dust==ItemStack.EMPTY)return;
		ItemStack pouch = null;
		ItemDustPouch itemPouch = null;
		if(dust.getItem() instanceof ItemDustPouch){
			pouch = dust;
			itemPouch = (ItemDustPouch)pouch.getItem();
			dust = itemPouch.getDustStack(pouch, 0);
			if(dust==ItemStack.EMPTY || dust.getItem()!=WizardryRegistry.dust_dyed){
				WizardryLogger.logError("the TEDustDye at "+getPos()+" had a pouch with null/non dyed dust");
				return;
			}
		}
		//NPE was because tagCompound is null...
		NBTTagCompound compound = dust.getTagCompound();
		if(compound == null){
			compound = new NBTTagCompound();
			dust.setTagCompound(compound);
		}
		compound.setInteger("color", color);
		if(pouch!=ItemStack.EMPTY){
			dust.setCount(itemPouch.getDustAmount(pouch));
			itemPouch.clear(pouch);
			itemPouch.addDust(pouch, dust);
		}
		setColor(Integer.toHexString(color));
		this.markDirty();
	}
	
	public void handleBlockUpdate(boolean newRedstone){
		if(pastRedstoneState && !newRedstone){//falling edge
			try{
				this.dye(Integer.parseInt(colorString,16));
			}catch(NumberFormatException e){
				WizardryLogger.logInfo("Dust Dye: unable to parse color "+colorString+" on redstone toggle");
			}
		}
		pastRedstoneState=newRedstone;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getUpdateTag()
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(super.getUpdateTag());
	}
	/**
	 * 
	 * @return the currently selected Color of this block as a String
	 */
	public String getColorString(){
		return colorString;
	}
	/**
	 * 
	 * @param color the selected color
	 */
	public void setColor(String color){
		colorString=color;
	}
	

	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#hasCapability(net.minecraftforge.common.capabilities.Capability, net.minecraft.util.EnumFacing)
	 */
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)return true;
		return super.hasCapability(capability, facing);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getCapability(net.minecraftforge.common.capabilities.Capability, net.minecraft.util.EnumFacing)
	 */
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory); 
		return super.getCapability(capability, facing);
	}
	//might want to change the tag names to be variables for "safety" (nah, "safety" is overrated)
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagCompound contents = tagCompound.getCompoundTag("contents");
		inventory = new DyedDustItemStackHandler(this);
		inventory.deserializeNBT(contents);
		//not syncing to the client on world load
		this.colorString=tagCompound.getString("Color");
		this.pastRedstoneState=tagCompound.getBoolean("pastRedstone");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setTag("contents", inventory.serializeNBT());
		tagCompound.setString("Color", colorString);
		tagCompound.setBoolean("pastRedstone", pastRedstoneState);
		return tagCompound;
	}
	
	private static class DyedDustItemStackHandler extends ItemStackHandler{
		private final TileEntityDustDye te;
		public DyedDustItemStackHandler(TileEntityDustDye tile) {
			te = tile;
		}
		/* (non-Javadoc)
		 * @see net.minecraftforge.items.ItemStackHandler#insertItem(int, net.minecraft.item.ItemStack, boolean)
		 */
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(stack==ItemStack.EMPTY || stack.getItem()==WizardryRegistry.dust_dyed){
				return super.insertItem(slot, stack, simulate);
			}else if(stack.getItem() instanceof ItemDustPouch){
				ItemDustPouch pouch = (ItemDustPouch)stack.getItem();
				ItemStack dust = pouch.getDustStack(stack, 0);
				if(dust!=ItemStack.EMPTY && dust.getItem()==WizardryRegistry.dust_dyed){
					return super.insertItem(slot, stack, simulate);
				}
			}
			return stack;
		}
		/* (non-Javadoc)
		 * @see net.minecraftforge.items.ItemStackHandler#onContentsChanged(int)
		 */
		@Override
		protected void onContentsChanged(int slot) {
			te.markDirty();
		}
		
	}
}
