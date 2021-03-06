/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-05
 */
package com.zpig333.runesofwizardry.command;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonIOException;
import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternFinder;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.util.RayTracer;
import com.zpig333.runesofwizardry.util.json.JsonUtils;

import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

/**
 * @author Xilef11
 *
 */
public class CommandExportPattern implements ICommand {
	private final List<String> aliases;
	private static final String locKey="runesofwizardry.command.export";
	public CommandExportPattern() {
		//define aliases here
		aliases = new LinkedList<>();
		//aliases.add(I18n.translateToLocal("runesofwizardry.command.export"));
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
	 * @see net.minecraft.command.ICommand#getName()
	 */
	@Override
	public String getName() {
		return "rw_export";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getUsage(net.minecraft.command.ICommandSender)
	 */
	@Override
	public String getUsage(ICommandSender sender) {
		//should return unlocalized string
		return getName()+" "+locKey+".usage";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		return aliases;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#processCommand(net.minecraft.command.ICommandSender, java.lang.String[])
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		//do work on the client side only
		if(world.isRemote && sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) sender;
			if(args.length!=1){
				throw new WrongUsageException(getUsage(sender));
			}
			//get the block the player is looking at
			RayTraceResult look = player.rayTrace(RayTracer.getBlockReachDistance(player), 1f);
			BlockPos lookPos = look.getBlockPos();
			Block block = world.getBlockState(lookPos).getBlock();
			EnumFacing playerFacing = player.getHorizontalFacing();
			RunesOfWizardry.log().info("Export Pattern: Looking at block: "+block.getUnlocalizedName()+" at "+lookPos+" facing: "+playerFacing);
			if(block!=WizardryRegistry.dust_placed){
				//gets localised.
				throw new CommandException(locKey+".nodust");
			}
			//find the pattern
			PatternFinder finder = new PatternFinder(world, lookPos);
			finder.search();
			ItemStack[][] pattern = finder.toArray();
			//Rotate the array so the direction the player is facing is top
			pattern = PatternUtils.rotateToFacing(pattern, playerFacing);

			//save pattern to JSON
			File output;
			try {
				output = PatternUtils.exportPatternJson(pattern, args[0]);
				JsonUtils.clearItemStackJson();
			} catch (JsonIOException e) {
				RunesOfWizardry.log().error("Unable to save pattern",e);
				throw new CommandException(locKey+".message.error");
			} catch (IOException e) {
				RunesOfWizardry.log().error("Unable to save pattern",e);
				throw new CommandException(locKey+".message.error");
			}
			//info message with link
			TextComponentString filename = new TextComponentString(output.getName());
			filename.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, output.getAbsolutePath()));
			filename.getStyle().setUnderlined(true);
			player.sendMessage(new TextComponentTranslation(locKey+".message", finder.getNumBlocks(), filename));
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#canCommandSenderUseCommand(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		//only players can use this command
		return sender instanceof EntityPlayer;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#addTabCompletionOptions(net.minecraft.command.ICommandSender, java.lang.String[], net.minecraft.util.BlockPos)
	 */
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,BlockPos pos) {
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
