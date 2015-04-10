package com.zpig333.runesofwizardry.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.zpig333.runesofwizardry.client.container.ContainerDustDye;
import com.zpig333.runesofwizardry.client.gui.GuiDustDye;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == 0){
            //open gui container here
        }else if (id==GuiDustDye.GUI_ID){
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
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
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileEntityDustDye){
                return new GuiDustDye(player.inventory, (TileEntityDustDye)te);
            }
        }
        return null;
    }
}
