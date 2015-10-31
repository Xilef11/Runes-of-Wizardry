package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.zpig333.runesofwizardry.core.References;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlantBalls extends WizardryItem {
	private final String name="plantball";
	private final String[] metaName={"small","large"};
	public ItemPlantBalls(){
		super();
		this.setHasSubtypes(true);
	}
	@Override
	public String getName(){
		return name;
	}
	public String getFullName(int meta){
		return name+"_"+metaName[meta];
	}
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		int meta = itemStack.getMetadata();
		return super.getUnlocalizedName() + "_" + metaName[meta];
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })//MC does not use generics... (List is a list of ItemStacks)
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list){
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

	private IIcon[] icons;
	@Override
	public IIcon getIconFromDamage(int meta){
		return icons[meta];
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ireg){
		icons = new IIcon[2];
		icons[0] = ireg.registerIcon(References.texture_path + "plantball_small");
		icons[1] = ireg.registerIcon(References.texture_path + "plantball_large");
	}

}
