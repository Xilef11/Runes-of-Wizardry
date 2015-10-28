package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.core.References;

import net.minecraft.client.renderer.texture.IIconRegister;


public class ItemNetherPaste extends WizardryItem {
	private final String name="nether_paste";
	public ItemNetherPaste(){
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
		this.itemIcon=register.registerIcon(References.texture_path+"nether_paste");
	}
		
}
