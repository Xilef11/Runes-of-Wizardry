package com.zpig333.runesofwizardry.network.guipackets;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class DustDyeTextPacket implements IMessage{
	private String text;//the text
	private int x,y,z;//position of the tileentity

	public DustDyeTextPacket(){}
	public DustDyeTextPacket(String text, int x, int y, int z){
		this.text=text;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		text=ByteBufUtils.readUTF8String(buf);
		x=ByteBufUtils.readVarInt(buf,5);
		y=ByteBufUtils.readVarInt(buf,5);
		z=ByteBufUtils.readVarInt(buf,5);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
	}

	public static class Handler implements IMessageHandler<DustDyeTextPacket, IMessage>{

		@Override
		public IMessage onMessage(DustDyeTextPacket message, MessageContext ctx) {
			TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileEntityDustDye){
				TileEntityDustDye ted = (TileEntityDustDye)te;
				ted.setColor(message.text);
				return null;
			}else{
				throw new IllegalArgumentException("DustDyeTextPacket.onMessage: tileEntity is not a Dust Dye");
			}
		}

	}

}
