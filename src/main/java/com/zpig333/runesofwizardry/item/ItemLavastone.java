package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.core.References;

import net.minecraft.client.renderer.texture.IIconRegister;


public class ItemLavastone extends WizardryItem {
	private final String name="lavastone";
	public ItemLavastone(){
		super();
	}
	@Override
	public String getName(){
		return name;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	public void registerIcons(IIconRegister register) {
		this.itemIcon=register.registerIcon(References.texture_path+"lavastone");
	}
	
}
