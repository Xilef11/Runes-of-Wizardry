package com.zpig333.runesofwizardry.network.guipackets;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
/**@see http://www.minecraftforge.net/forum/index.php/topic,20135.0.html
 * 
 */

public class DustDyeButtonPacket implements IMessage {

	private int color;
	//private TileEntityDustDye source;
	private int x,y,z; //position of the tileentity

	public DustDyeButtonPacket(){
	}


	public DustDyeButtonPacket(int color,int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
		this.color=color;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		//would probably be more efficient to use the BlockPos.toLong and fromLong, but ByteBufUtils dosen't have a readVarLong
		this.color = ByteBufUtils.readVarInt(buf, 5);
		this.x=ByteBufUtils.readVarInt(buf, 5);
		this.y=ByteBufUtils.readVarInt(buf, 5);
		this.z=ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, color, 5);
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
	}

	public static class Handler implements IMessageHandler<DustDyeButtonPacket, IMessage> {

		@Override
		public IMessage onMessage(DustDyeButtonPacket message, MessageContext ctx) {
			TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te instanceof TileEntityDustDye){
				TileEntityDustDye ted = (TileEntityDustDye)te;
				ted.dye(message.color);
				return null;//contents should be automatically synced
			}else{
				throw new IllegalArgumentException("DustDyeButtonPacket.Handler.onMessage: TileEntity is not a Dust Dye");
			}
		}
	}
}
