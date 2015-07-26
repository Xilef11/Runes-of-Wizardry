package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
//[refactor] seems good
public class ItemPlantBalls extends Item {
	private final String name="plantball";
	private final String[] metaName={"small","large"};
    public ItemPlantBalls(){
        super();
        GameRegistry.registerItem(this, name);
        setUnlocalizedName(References.modid+"_"+name);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
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



    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list){
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }


}
