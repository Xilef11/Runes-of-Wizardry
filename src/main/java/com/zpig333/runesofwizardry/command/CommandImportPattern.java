/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-03-05
 */
package com.zpig333.runesofwizardry.command;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import scala.actors.threadpool.Arrays;

import com.zpig333.runesofwizardry.core.ConfigHandler;

/**
 * @author Xilef11
 *
 */
public class CommandImportPattern implements ICommand {
	private final List<String> aliases;
	private static final String locKey="runesofwizardry.command.import";
	public CommandImportPattern() {
		//define aliases here
		aliases = new LinkedList<String>();
		//aliases.add(StatCollector.translateToLocal("runesofwizardry.command.export"));
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ICommand arg0) {
		//for sorting commands I guess?
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return "rw_import";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandUsage(net.minecraft.command.ICommandSender)
	 */
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return getCommandName()+" "+StatCollector.translateToLocal(locKey+".usage");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandAliases()
	 */
	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#processCommand(net.minecraft.command.ICommandSender, java.lang.String[])
	 */
	@Override
	public void processCommand(ICommandSender sender, String[] args)throws CommandException {
		// TODO Auto-generated method stub
		sender.addChatMessage(new ChatComponentText(Arrays.toString(MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames())));
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#canCommandSenderUseCommand(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if(ConfigHandler.CommandImportPermission.equals(ConfigHandler.PERMISSIONS_NONE))return false;
		if(ConfigHandler.CommandImportPermission.equals(ConfigHandler.PERMISSIONS_ALL))return true;
		if(ConfigHandler.CommandImportPermission.equals(ConfigHandler.PERMISSIONS_OP)){
			String[] ops = MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames();
			for(String name:ops){
				if(name.equals(sender.getName()))return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#addTabCompletionOptions(net.minecraft.command.ICommandSender, java.lang.String[], net.minecraft.util.BlockPos)
	 */
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender,String[] args, BlockPos pos) {
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
