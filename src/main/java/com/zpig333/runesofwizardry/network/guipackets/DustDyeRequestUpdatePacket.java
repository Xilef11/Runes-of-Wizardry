package com.zpig333.runesofwizardry.network.guipackets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

//[refactor] should be ok
public class DustDyeRequestUpdatePacket implements IMessage{
    int posX,posY,posZ;//position of the DustDye TE
    public DustDyeRequestUpdatePacket(){}
    public DustDyeRequestUpdatePacket(BlockPos source){
        this.posX=source.getX();
        this.posY=source.getY();
        this.posZ=source.getZ();
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
            BlockPos position=new BlockPos(message.posX, message.posY, message.posZ);
            TileEntity te = player.worldObj.getTileEntity(position);
            if(te instanceof TileEntityDustDye){
                TileEntityDustDye ted = (TileEntityDustDye)te;
                return new DustDyeUpdatePacket(position, ted.getColorString());
            }else{
                throw new IllegalArgumentException("DustDyeRequestUpdatePacket.onMessage: tileEntity is not a Dust Dye");
            }
        }
    
    }

}
