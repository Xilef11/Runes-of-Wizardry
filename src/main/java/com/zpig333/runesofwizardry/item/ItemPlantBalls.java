package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPlantBalls extends WizardryItem {
	private final String name="plantball";
	private final String[] metaName={"small","large"};
    public ItemPlantBalls(){
        super();
        this.setHasSubtypes(true);
    }
    public String getName(){
    	return name;
    }
    public String getFullName(int meta){
    	return name+"_"+metaName[meta];
    }
    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        int meta = itemStack.getItemDamage();
        return super.getUnlocalizedName() + "_" + metaName[meta];
    }



    @SuppressWarnings({ "rawtypes", "unchecked" })//MC does not use generics... (List is a list of ItemStacks)
	@Override
    public void getSubItems(Item item, CreativeTabs tabs, List list){
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }


}
