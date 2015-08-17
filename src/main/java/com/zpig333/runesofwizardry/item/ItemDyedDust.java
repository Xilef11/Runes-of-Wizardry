package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;

public class ItemDyedDust extends IDust{
	
	private final String name="dust_dyed";
    public ItemDyedDust(){
        super();
        GameRegistry.registerItem(this, name);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setUnlocalizedName(References.modid+"_"+name);
    }
    //XXX this may be handled already via IDust
    public String getName(){
    	return name;
    }
    //add tooltip
    @SuppressWarnings({ "rawtypes", "unchecked" })//List is a list of Strings, but Item does not use generics
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List data, boolean bool){
        //if the stack has no tag compound, create one and set the color to white
        if(stack.getTagCompound()==null){
        	NBTTagCompound compound = new NBTTagCompound();
        	compound.setInteger("color", 0xffffff);
            stack.setTagCompound(compound);
        }
        String color = String.format("#%06X", stack.getTagCompound().getInteger("color"));
        data.add(color);
            
    }

    /* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getDustName()
	 */
	@Override
	public String getDustName() {
		return "dyed";
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getPrimaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getPrimaryColor(ItemStack stack) {
		NBTTagCompound tag=stack.getTagCompound();
        if(tag==null){
            return 0xffffff;
        }
        return tag.getInteger("color");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getSecondaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getSecondaryColor(ItemStack stack) {
		//Only 1 color
		return getPrimaryColor(stack);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getInfusionItems(net.minecraft.item.ItemStack)
	 */
	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//This dust is crafted via other mechanics
		return null;
	}
}
