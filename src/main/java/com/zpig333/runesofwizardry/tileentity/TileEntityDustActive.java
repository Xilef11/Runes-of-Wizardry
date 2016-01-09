/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-09
 */
package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;

import com.zpig333.runesofwizardry.api.RuneEntity;

/** This TileEntity replaces a TileEntityDustPlaced when a rune is formed.
 * @author Xilef11
 *
 */
public class TileEntityDustActive extends TileEntityDustPlaced implements IUpdatePlayerListBox {

	public TileEntityDustActive(TileEntityDustPlaced oldTE){
		//copy all fields from old TE
		this.contents=oldTE.contents;
		this.rune=oldTE.rune;
		//get the rendering
		this.updateCenterColors();
		this.updateExternalConnectors();
		this.updateInternalConnectors();
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.server.gui.IUpdatePlayerListBox#update()
	 */
	@Override
	public void update() {
		rune.update();
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		rune.readFromNBT(tagCompound);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		rune.writeToNBT(tagCompound);
	}
	//
	
}
