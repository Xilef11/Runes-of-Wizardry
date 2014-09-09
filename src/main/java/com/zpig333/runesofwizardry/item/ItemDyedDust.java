package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

//Might extend ItemDustPieces instead...
public class ItemDyedDust extends Item{
    public ItemDyedDust(){
        super();
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setUnlocalizedName("dyed_dust");
    }
    
    public void addInformation(ItemStack stack, EntityPlayer player, List data, boolean bool){
        //if the stack has no tag compound, create one and set the color to white
        if(stack.getTagCompound()==null){
            stack.setTagCompound(new NBTTagCompound());
            stack.stackTagCompound.setInteger("color", 0xffffff);
        }
        //String color = Integer.toHexString(stack.stackTagCompound.getInteger("color"));
        String color = String.format("#%06X", stack.stackTagCompound.getInteger("color"));
        data.add(color);
            
    }
    

}
