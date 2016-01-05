/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-05
 */
package com.zpig333.runesofwizardry.command;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

/**
 * @author Xilef11
 *
 */
public class CommandExportPattern implements ICommand {
	//private final List<String> aliases;
	
	public CommandExportPattern() {
		//define aliases here
		//aliases = new LinkedList<String>();
		//aliases.add(StatCollector.translateToLocal("runesofwizardry.command.export"));
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object arg0) {
		//for sorting commands I guess?
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return "rw_export";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandUsage(net.minecraft.command.ICommandSender)
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return getCommandName()+" "+StatCollector.translateToLocal("runesofwizardry.command.export.usage");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandAliases()
	 */
	@Override
	public List getCommandAliases() {
		//return aliases;
		return null;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#processCommand(net.minecraft.command.ICommandSender, java.lang.String[])
	 */
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		//do work on the client side only
		//hmmm... looks like this happens server-side only :(
		if(sender.getEntityWorld().isRemote && sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) sender;
			if(args.length!=1){
				throw new WrongUsageException(getCommandUsage(sender));
			}
			//TODO actual exporting
			
			//TODO localization
			player.addChatMessage(new ChatComponentText("Exported pattern as "+args[0]+".json"));
			
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#canCommandSenderUseCommand(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		//only players can use this command
		return sender instanceof EntityPlayer;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#addTabCompletionOptions(net.minecraft.command.ICommandSender, java.lang.String[], net.minecraft.util.BlockPos)
	 */
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,BlockPos pos) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#isUsernameIndex(java.lang.String[], int)
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
