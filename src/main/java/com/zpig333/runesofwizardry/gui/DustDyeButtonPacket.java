package com.zpig333.runesofwizardry.gui;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
/**@see http://www.minecraftforge.net/forum/index.php/topic,20135.0.html
 * 
 */
public class DustDyeButtonPacket implements IMessage {

    private String text;

    public DustDyeButtonPacket() {
    }

    public DustDyeButtonPacket(String text) {
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf); // this class is very useful in general for writing more complex objects
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<DustDyeButtonPacket, IMessage> {

        @Override
        public IMessage onMessage(DustDyeButtonPacket message, MessageContext ctx) {
            //TODO what to do when the button is clicked
            System.out.println(String.format("Received %s from %s", message.text, ctx.getServerHandler().playerEntity.getDisplayName()));
            return null; // no response in this case
        }
    }
}
