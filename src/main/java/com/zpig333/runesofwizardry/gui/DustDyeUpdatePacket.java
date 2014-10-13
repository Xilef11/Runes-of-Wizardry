
package com.zpig333.runesofwizardry.gui;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;

public class DustDyeUpdatePacket implements IMessage {
    String colorString;
    int posX,posY,posZ;
    public DustDyeUpdatePacket(){}
    public DustDyeUpdatePacket(int x, int y, int z, String colorS){
        posX=x;
        posY=y;
        posX=z;
        colorString=colorS;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        posX= ByteBufUtils.readVarInt(buf, 5);
        posY= ByteBufUtils.readVarInt(buf, 5);
        posZ= ByteBufUtils.readVarInt(buf, 5);
        colorString=ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, posX, 5);
        ByteBufUtils.writeVarInt(buf, posY, 5);
        ByteBufUtils.writeVarInt(buf, posZ, 5);
        ByteBufUtils.writeUTF8String(buf, colorString);
        
    }
    
    public static class Handler implements IMessageHandler<DustDyeUpdatePacket, IMessage>{

        @Override
        public IMessage onMessage(DustDyeUpdatePacket message, MessageContext ctx) {
            /*
            TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.posX, message.posY, message.posZ);
            if(te instanceof TileEntityDustDye){
                TileEntityDustDye ted = (TileEntityDustDye)te;
                ted.setColor(message.colorString);
            }else{
                throw new IllegalArgumentException("DustDyeUpdatepacket.Handler.onMessage: tileEntity is not a Dust Dye");
            }
            */
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen instanceof GuiDustDye){
                TileEntityDustDye ted = ((GuiDustDye)screen).getParent();
                ted.setColor(message.colorString);
            }else{
                throw new IllegalArgumentException("DustDyeUpdatePacket.Handler.onMessage: current screen is not GuiDustDye");
            }
            return null;
        }
        
    }

}
