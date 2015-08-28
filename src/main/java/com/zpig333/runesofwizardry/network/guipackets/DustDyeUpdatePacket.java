
package com.zpig333.runesofwizardry.network.guipackets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zpig333.runesofwizardry.client.gui.GuiDustDye;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class DustDyeUpdatePacket implements IMessage {
	String colorString;
	int posX,posY,posZ;
	public DustDyeUpdatePacket(){}
	public DustDyeUpdatePacket(BlockPos pos, String colorS){
		posX=pos.getX();
		posY=pos.getY();
		posX=pos.getZ();
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
