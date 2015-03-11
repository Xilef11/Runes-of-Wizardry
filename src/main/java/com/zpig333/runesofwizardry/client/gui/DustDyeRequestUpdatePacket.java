package com.zpig333.runesofwizardry.client.gui;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;


public class DustDyeRequestUpdatePacket implements IMessage{
    int posX,posY,posZ;
    public DustDyeRequestUpdatePacket(){}
    public DustDyeRequestUpdatePacket(int posX, int posY, int posZ){
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        posX=ByteBufUtils.readVarInt(buf, 5);
        posY=ByteBufUtils.readVarInt(buf, 5);
        posZ=ByteBufUtils.readVarInt(buf, 5);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, posX, 5);
        ByteBufUtils.writeVarInt(buf, posY, 5);
        ByteBufUtils.writeVarInt(buf, posZ, 5);
    }
    
    public static class Handler implements IMessageHandler<DustDyeRequestUpdatePacket, DustDyeUpdatePacket>{

        @Override
        public DustDyeUpdatePacket onMessage(DustDyeRequestUpdatePacket message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            TileEntity te = player.worldObj.getTileEntity(message.posX, message.posY, message.posZ);
            if(te instanceof TileEntityDustDye){
                TileEntityDustDye ted = (TileEntityDustDye)te;
                return new DustDyeUpdatePacket(ted.xCoord, ted.yCoord, ted.zCoord, ted.getColor());
            }else{
                throw new IllegalArgumentException("DustDyeRequestUpdatePacket.onMessage: tileEntity is not a Dust Dye");
            }
        }
    
    }

}
