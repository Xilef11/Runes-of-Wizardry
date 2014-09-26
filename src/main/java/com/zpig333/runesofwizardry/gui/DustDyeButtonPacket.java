package com.zpig333.runesofwizardry.gui;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
/**@see http://www.minecraftforge.net/forum/index.php/topic,20135.0.html
 * 
 */
public class DustDyeButtonPacket implements IMessage {

    private String text;
    private int color;
    //private TileEntityDustDye source;
    private ItemStack stack;
    private int type;
    private int x,y,z; //position of the tileentity
    public final static int TYPE_TEXT=0,
                            TYPE_DYE=1,
                            TYPE_REPLY=2;

    public DustDyeButtonPacket(){
        this.type=-1;
    }

    public DustDyeButtonPacket(int color, ItemStack stack){
        this.type=TYPE_REPLY;
        this.color=color;
        this.stack=stack;
        //DEBUG
        System.out.println("packet created");
    }
    public DustDyeButtonPacket(int x, int y, int z, int color){
        this.type=TYPE_DYE;
        this.x=x;
        this.y=y;
        this.z=z;
        this.color=color;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        type=ByteBufUtils.readVarShort(buf);
       /* try {
            ObjectInputStream stream =new ObjectInputStream(new ByteBufInputStream(buf));
            Object in=stream.readObject();
            if(in instanceof TileEntityDustDye){
                source=(TileEntityDustDye)in;
            }
        } catch (IOException ex) {
            Logger.getLogger(DustDyeButtonPacket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DustDyeButtonPacket.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if(type==TYPE_TEXT){
            text = ByteBufUtils.readUTF8String(buf); // this class is very useful in general for writing more complex objects
        }else if(type==TYPE_DYE){
            color=ByteBufUtils.readVarInt(buf, 5);
            //stack=ByteBufUtils.readItemStack(buf);
            x=ByteBufUtils.readVarInt(buf, 5);
            y=ByteBufUtils.readVarInt(buf, 5);
            z=ByteBufUtils.readVarInt(buf, 5);
        }else if(type==TYPE_REPLY){
            color=ByteBufUtils.readVarInt(buf, 5);
            stack=ByteBufUtils.readItemStack(buf);
        }
        
        //DEBUG
        System.out.println("packet decoded");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarShort(buf, type);
       /* ObjectOutputStream stream;
        try {
            stream = new ObjectOutputStream(new ByteBufOutputStream(buf));
            stream.writeObject(source);
        } catch (IOException ex) {
            Logger.getLogger(DustDyeButtonPacket.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        if(type==TYPE_TEXT){
            ByteBufUtils.writeUTF8String(buf, text);
        }else if(type==TYPE_DYE){
            ByteBufUtils.writeVarInt(buf, color, 5);
            //ByteBufUtils.writeItemStack(buf, stack);
            ByteBufUtils.writeVarInt(buf, x, 5);
            ByteBufUtils.writeVarInt(buf, y, 5);
            ByteBufUtils.writeVarInt(buf, z, 5);
        }else if(type==TYPE_REPLY){
            ByteBufUtils.writeVarInt(buf, color, 5);
            ByteBufUtils.writeItemStack(buf, stack);
        }
        //DEBUG
        System.out.println("packet encoded");
    }

    public static class Handler implements IMessageHandler<DustDyeButtonPacket, IMessage> {

        @Override
        public IMessage onMessage(DustDyeButtonPacket message, MessageContext ctx) {
            //TODO what to do when the button is clicked
            System.out.println(String.format("Received %s from %s", message, ctx.getServerHandler().playerEntity.getDisplayName()));
           // System.out.println("type: "+message.type+" stack: "+message.stack);
            //System.out.println("stack color: "+message.stack.getTagCompound().getInteger("color"));
            if(message.type==TYPE_DYE){
                System.out.println("color: "+message.color);
                //FIXME tis not do stuffz
                //message.stack.getTagCompound().setInteger("color", message.color);
                 //System.out.println("stack color: "+message.stack.getTagCompound().getInteger("color"));
                 //message.stack.writeToNBT(message.stack.getTagCompound());
                EntityPlayer player = ctx.getServerHandler().playerEntity;
                TileEntity te = player.worldObj.getTileEntity(message.x, message.y, message.z);
                //FIXME fsr this does not recognize the import?
                if(te instanceof com.zpig333.runesofwizardry.tileentity.TileEntityDustDye){
                    com.zpig333.runesofwizardry.tileentity.TileEntityDustDye ted;
                    ted = (com.zpig333.runesofwizardry.tileentity.TileEntityDustDye)te;
                    ted.dye(message.color);
                    player.worldObj.markBlockForUpdate(ted.xCoord, ted.yCoord, ted.zCoord);
                    ted.markDirty();
                    return new DustDyeButtonPacket(message.color, ted.getStackInSlot(0));
                }
                
            }else if(message.type==TYPE_TEXT){
                return null;
                //message.source.setColor(message.text);
            }else if(message.type==TYPE_REPLY){
                message.stack.getTagCompound().setInteger("color", message.color);
                return null;
            }
            throw new IllegalArgumentException("Could not process message, "+message.type);
        }
    }
}
