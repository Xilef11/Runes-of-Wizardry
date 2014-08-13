package com.zpig333.runesofwizardry.core;

import com.zpig333.runesofwizardry.client.container.ContainerDustDye;
import com.zpig333.runesofwizardry.gui.GuiDustDye;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by zombiepig333 on 24-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
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
