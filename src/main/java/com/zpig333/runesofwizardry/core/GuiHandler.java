package com.zpig333.runesofwizardry.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.client.gui.GuiDustDye;
import com.zpig333.runesofwizardry.inventory.ContainerDustDye;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if(id == 0){
			//open gui container here
		}else if (id==GuiDustDye.GUI_ID){
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileEntityDustDye){
				return new ContainerDustDye(player.inventory, (TileEntityDustDye)te);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if(id == 0){
			//open gui screen here
		}else if (id==GuiDustDye.GUI_ID){
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileEntityDustDye){
				return new GuiDustDye(player.inventory, (TileEntityDustDye)te);
			}
		}
		return null;
	}
}
