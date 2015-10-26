package com.zpig333.runesofwizardry.network.guipackets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class DustDyeRequestUpdatePacket implements IMessage{
	int posX,posY,posZ;//position of the DustDye TE
	public DustDyeRequestUpdatePacket(){}
	public DustDyeRequestUpdatePacket(int x,int y, int z){
		this.posX=x;
		this.posY=y;
		this.posZ=z;
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
				return new DustDyeUpdatePacket(message.posX, message.posY, message.posZ, ted.getColorString());
			}else{
				throw new IllegalArgumentException("DustDyeRequestUpdatePacket.onMessage: tileEntity is not a Dust Dye");
			}
		}

	}

}
