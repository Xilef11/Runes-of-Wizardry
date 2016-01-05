/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-05
 */
package com.zpig333.runesofwizardry.command;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternFinder;
import com.zpig333.runesofwizardry.util.ArrayUtils;
import com.zpig333.runesofwizardry.util.RayTracer;

/**
 * @author Xilef11
 *
 */
public class CommandExportPattern implements ICommand {
	private final List<String> aliases;
	private static final String locKey="runesofwizardry.command.export";
	public CommandExportPattern() {
		//define aliases here
		aliases = new LinkedList<String>();
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
		return getCommandName()+" "+StatCollector.translateToLocal(locKey+".usage");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandAliases()
	 */
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#processCommand(net.minecraft.command.ICommandSender, java.lang.String[])
	 */
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		//do work on the client side only
		if(world.isRemote && sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) sender;
			if(args.length!=1){
				throw new WrongUsageException(getCommandUsage(sender));
			}
			//get the block the player is looking at
			MovingObjectPosition look = player.rayTrace(RayTracer.getBlockReachDistance(player), 1f);
			BlockPos lookPos = look.getBlockPos();
			Block block = world.getBlockState(lookPos).getBlock();
			EnumFacing playerFacing = player.getHorizontalFacing();
			WizardryLogger.logInfo("Export Pattern: Looking at block: "+block.getUnlocalizedName()+" at "+lookPos+" facing: "+playerFacing);
			if(block!=WizardryRegistry.dust_placed){
				throw new CommandException(StatCollector.translateToLocal(locKey+".nodust"));
			}
			//find the pattern
			PatternFinder finder = new PatternFinder(world, lookPos);
			finder.search();
			ItemStack[][] pattern = finder.toArray();
			//Rotate the array so the direction the player is facing is top
			pattern = rotateToFacing(pattern, playerFacing);
			
			//WizardryLogger.logInfo(ArrayUtils.printMatrix(pattern));
			//TODO export to JSON
			
			//TODO localization
			player.addChatMessage(new ChatComponentText("Exported "+finder.getNumBlocks()+" blocks  as "+args[0]+".json"));
			
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
	/*Rotates the pattern so the given facing is at top
	 * (TODO move to some utility class...)
	 */
	private ItemStack[][] rotateToFacing(ItemStack[][] patternIn, EnumFacing facing){
		ItemStack[][] result;
		switch(facing){
		case NORTH: result = patternIn;
					break;//no need to do anything
		case WEST: result = ArrayUtils.rotateCW(patternIn);
				   break;
		case SOUTH: result = ArrayUtils.rotate180(patternIn);
					break;
		case EAST: result = ArrayUtils.rotateCCW(patternIn);
					break;
		default: throw new IllegalArgumentException("Facing: "+facing+" is not horizontal!");
		}
		return result;
	}
}
