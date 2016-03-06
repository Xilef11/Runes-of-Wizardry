/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-03-05
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
import com.zpig333.runesofwizardry.util.RayTracer;

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
		World world = sender.getEntityWorld();
		//server-side only... for now
		if(!world.isRemote && sender instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) sender;
			if(args.length==0)throw new WrongUsageException(getCommandUsage(sender));
			//get the pattern from args
			ItemStack[][] pattern = null;
			if(args[0].contains(":")){//if its a rune
				IRune rune = DustRegistry.getRuneByID(args[0]);
				if(rune==null)throw new CommandException(StatCollector.translateToLocal(locKey+".nosuchrune"));
				pattern = rune.getPattern();
			}else{
				//TODO if its a JSON on the client, we'll need a packet to send the pattern...
				throw new WrongUsageException(StatCollector.translateToLocal(locKey+".runesonly"));
			}
			//Find the block looked at + facing
			MovingObjectPosition look = RayTracer.retrace(player);
			BlockPos lookPos = look.getBlockPos();
			Block block = world.getBlockState(lookPos).getBlock();
			EnumFacing playerFacing = player.getHorizontalFacing();
			WizardryLogger.logInfo("Import Pattern: Looking at block: "+block.getUnlocalizedName()+" at "+lookPos+" facing: "+playerFacing);
			//TODO move the following somewhere else as placePatternFromTopLeft(pattern,pos, facing)
			//get the pattern in the right direction
			ItemStack[][] rotatedPattern = PatternUtils.rotateAgainstFacing(pattern, playerFacing);
			//convert to contents array
			ItemStack[][][][] contents = PatternUtils.toContentsArray(rotatedPattern);
			//get the pos for the NW block (an offset from the look/"top-left" block)
			BlockPos nw=lookPos.up();
			switch(playerFacing){
			case EAST: nw = nw.offset(EnumFacing.WEST,contents[0].length-1);
				break;
			case NORTH: //no change needed
				break;
			case SOUTH: nw=nw.offset(EnumFacing.WEST,contents[0].length-1).offset(EnumFacing.NORTH,contents.length-1);
				break;
			case WEST:nw = nw.offset(EnumFacing.NORTH,contents.length-1);
				break;
			default: throw new IllegalStateException("Import command: Facing is not horizontal");
			}
			//contents[0][0] is always NW most block
			for(int r=0;r<contents.length;r++){
				for(int c=0;c<contents[r].length;c++){
					BlockPos current = nw.offset(EnumFacing.EAST, c).offset(EnumFacing.SOUTH, r);
					if(world.isAirBlock(current) && !PatternUtils.isEmpty(contents[r][c])){
						world.setBlockState(current, WizardryRegistry.dust_placed.getDefaultState());
						TileEntity ent = world.getTileEntity(current);
						if(ent instanceof TileEntityDustPlaced){//no reason why it isn't
							TileEntityDustPlaced ted = (TileEntityDustPlaced)ent;
							//ItemStack[][] pat = PatternUtils.rotateAgainstFacing(contents[r][c], playerFacing);
							//ted.setContents(pat);
							ted.setContents(contents[r][c]);
							world.markBlockForUpdate(current);
							//remove from player's inventory if not creative
							if(false && !player.capabilities.isCreativeMode){
								for(ItemStack[] i:contents[r][c]){
									for(ItemStack s:i){
										//FIXME NPE
										player.inventory.clearMatchingItems(s.getItem(), s.getMetadata(), s.stackSize, s.getTagCompound());
									}
								}
							}
						}else{
							throw new IllegalStateException("import command: TE was not placed dust");
						}
					}
				}
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#canCommandSenderUseCommand(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if(!(sender instanceof EntityPlayer))return false;
		if(ConfigHandler.CommandImportPermission.equals(ConfigHandler.PERMISSIONS_NONE))return false;
		if(ConfigHandler.CommandImportPermission.equals(ConfigHandler.PERMISSIONS_ALL))return true;
		if(ConfigHandler.CommandImportPermission.equals(ConfigHandler.PERMISSIONS_OP)){
			String[] ops = MinecraftServer.getServer().getConfigurationManager().getOppedPlayerNames();
			for(String name:ops){
				if(name.equals(sender.getName()))return true;
			}
			//TODO check if cheats enabled if single player
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#addTabCompletionOptions(net.minecraft.command.ICommandSender, java.lang.String[], net.minecraft.util.BlockPos)
	 */
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender,String[] args, BlockPos pos) {
		return new LinkedList<String>(DustRegistry.getRuneIDs());
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#isUsernameIndex(java.lang.String[], int)
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
