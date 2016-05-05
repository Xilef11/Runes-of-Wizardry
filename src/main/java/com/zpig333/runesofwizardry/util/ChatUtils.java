package com.zpig333.runesofwizardry.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.zpig333.runesofwizardry.RunesOfWizardry;
/*Utilities to mess with the chat. adapted from Blood Magic: https://github.com/WayofTime/BloodMagic/blob/698358df237d2c894afb20a3312203c8c2bf8e65/src/main/java/WayofTime/bloodmagic/util/ChatUtil.java
 * by WayOfTime, licensed under Creative Commons Attribution 4.0 International Public License
 */
public class ChatUtils
{
	private static final int DELETION_ID = 2525277;
	private static int lastAdded;

	private static void sendNoSpamMessages(ITextComponent[] messages)
	{
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		for (int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++)
		{
			chat.deleteChatLine(i);
		}
		for (int i = 0; i < messages.length; i++)
		{
			chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		}
		lastAdded = DELETION_ID + messages.length - 1;
	}

	/**
	 * Returns a standard {@link TextComponentString} for the given {@link String}
	 * .
	 * 
	 * @param s
	 *        The string to wrap.
	 * 
	 * @return An {@link ITextComponent} containing the string.
	 */
	public static ITextComponent wrap(String s)
	{
		return new TextComponentString(s);
	}

	/**
	 * @see #wrap(String)
	 */
	public static ITextComponent[] wrap(String... s)
	{
		ITextComponent[] ret = new ITextComponent[s.length];
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = wrap(s[i]);
		}
		return ret;
	}

	/**
	 * Returns a translatable chat component for the given string and format
	 * args.
	 * 
	 * @param s
	 *        The string to format
	 * @param args
	 *        The args to apply to the format
	 */
	public static ITextComponent wrapFormatted(String s, Object... args)
	{
		return new TextComponentTranslation(s, args);
	}

	/**
	 * Simply sends the passed lines to the player in a chat message.
	 * 
	 * @param player
	 *        The player to send the chat to
	 * @param lines
	 *        The lines to send
	 */
	public static void sendChat(EntityPlayer player, String... lines)
	{
		sendChat(player, wrap(lines));
	}

	/**
	 * Sends all passed chat components to the player.
	 * 
	 * @param player
	 *        The player to send the chat lines to.
	 * @param lines
	 *        The {@link ITextComponent chat components} to send.yes
	 */
	public static void sendChat(EntityPlayer player, ITextComponent... lines)
	{
		for (ITextComponent c : lines)
		{
			player.addChatComponentMessage(c);
		}
	}

	/**
	 * Same as {@link #sendNoSpamClient(ITextComponent...)}, but wraps the
	 * Strings automatically.
	 * 
	 * @param lines
	 *        The chat lines to send
	 * 
	 * @see #wrap(String)
	 */
	public static void sendNoSpamClient(String... lines)
	{
		sendNoSpamClient(wrap(lines));
	}

	/**
	 * Skips the packet sending, unsafe to call on servers.
	 * 
	 * @see #sendNoSpam(EntityPlayerMP, ITextComponent...)
	 */
	public static void sendNoSpamClient(ITextComponent... lines)
	{
		sendNoSpamMessages(lines);
	}

	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(EntityPlayer, ITextComponent...)
	 */
	public static void sendNoSpam(EntityPlayer player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}

	/**
	 * First checks if the player is instanceof {@link EntityPlayerMP} before
	 * casting.
	 * 
	 * @see #sendNoSpam(EntityPlayerMP, ITextComponent...)
	 */
	public static void sendNoSpam(EntityPlayer player, ITextComponent... lines)
	{
		if (player instanceof EntityPlayerMP)
		{
			sendNoSpam((EntityPlayerMP) player, lines);
		}
	}

	/**
	 * @see #wrap(String)
	 * @see #sendNoSpam(EntityPlayerMP, ITextComponent...)
	 */
	public static void sendNoSpam(EntityPlayerMP player, String... lines)
	{
		sendNoSpam(player, wrap(lines));
	}

	/**
	 * Sends a chat message to the client, deleting past messages also sent via
	 * this method.
	 * 
	 * Credit to RWTema for the idea
	 * 
	 * @param player
	 *        The player to send the chat message to
	 * @param lines
	 *        The chat lines to send.
	 */
	public static void sendNoSpam(EntityPlayerMP player, ITextComponent... lines)
	{
		if (lines.length > 0)
			RunesOfWizardry.networkWrapper.sendTo(new PacketNoSpamChat(lines), player);
	}

	/**
	 * @author tterrag1098
	 * 
	 *         Ripped from EnderCore (and slightly altered)
	 */
	public static class PacketNoSpamChat implements IMessage
	{

		private ITextComponent[] chatLines;

		public PacketNoSpamChat()
		{
			chatLines = new ITextComponent[0];
		}

		private PacketNoSpamChat(ITextComponent... lines)
		{
			// this is guaranteed to be >1 length by accessing methods
			this.chatLines = lines;
		}

		@Override
		public void toBytes(ByteBuf buf)
		{
			buf.writeInt(chatLines.length);
			for (ITextComponent c : chatLines)
			{
				ByteBufUtils.writeUTF8String(buf, ITextComponent.Serializer.componentToJson(c));
			}
		}

		@Override
		public void fromBytes(ByteBuf buf)
		{
			chatLines = new ITextComponent[buf.readInt()];
			for (int i = 0; i < chatLines.length; i++)
			{
				chatLines[i] = ITextComponent.Serializer.jsonToComponent(ByteBufUtils.readUTF8String(buf));
			}
		}

		public static class Handler implements IMessageHandler<PacketNoSpamChat, IMessage>
		{

			@Override
			public IMessage onMessage(PacketNoSpamChat message, MessageContext ctx)
			{
				sendNoSpamMessages(message.chatLines);
				return null;
			}
		}
	}
}
