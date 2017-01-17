/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-03-05
 */
package com.zpig333.runesofwizardry.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.item.ItemDustPouch;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
import com.zpig333.runesofwizardry.util.RayTracer;
import com.zpig333.runesofwizardry.util.json.JsonUtils;

/**
 * @author Xilef11
 *
 */
public class CommandImportPattern implements ICommand {
	private static CommandImportPattern instance;
	private final List<String> aliases;
	private static final String locKey="runesofwizardry.command.import";
	public CommandImportPattern() {
		//define aliases here
		aliases = new LinkedList<String>();
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
		return "rw_import";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getUsage(net.minecraft.command.ICommandSender)
	 */
	@Override
	public String getUsage(ICommandSender sender) {
		//it gets partially translated - OK
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
	//Note: server is null when called from runic dictionary
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)throws CommandException {
		World world = sender.getEntityWorld();
		//server-side only... for now
		if(!world.isRemote){
			if(sender instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) sender;
				if(args.length<1 || args.length>2)throw new WrongUsageException(getUsage(sender));
				//get the pattern from args
				ItemStack[][] pattern = null;
				IRune rune=null;
				if(args[0].contains(":")){//if its a rune
					rune = DustRegistry.getRuneByID(args[0]);
					//translation happens automagically
					if(rune==null)throw new CommandException(locKey+".nosuchrune",args[0]);
					ItemStack[][] runepattern = rune.getPattern();
					//COPY the pattern to avoid changing it by mistake (everything is mutable...)
					pattern = new ItemStack[runepattern.length][runepattern[0].length];
					for(int r=0;r<pattern.length;r++){
						for(int c=0;c<pattern[r].length;c++){
							pattern[r][c]=runepattern[r][c].copy();
						}
					}
				}else{
					//Check for JSON on the server
					try {
						pattern = PatternUtils.importFromJson(args[0]);
						JsonUtils.clearItemStackJson();
					} catch (FileNotFoundException e) {
						sender.sendMessage(new TextComponentTranslation(locKey+".serverfilenotfound", args[0], args[0], args[0]));
						return;
					} catch (IOException e) {
						WizardryLogger.logException(Level.ERROR, e, "Error while importing pattern from JSON");
					}
				}

				//Find the block looked at + facing
				RayTraceResult look = RayTracer.retrace(player);
				BlockPos lookPos = look.getBlockPos();
				Block block = world.getBlockState(lookPos).getBlock();
				EnumFacing playerFacing = player.getHorizontalFacing();
				WizardryLogger.logInfo("Import Pattern: Looking at block: "+block.getUnlocalizedName()+" at "+lookPos+" facing: "+playerFacing);
				//allows to replace placed dust
				if(world.getBlockState(lookPos).getBlock()==WizardryRegistry.dust_placed)lookPos=lookPos.down();
				//place the pattern depending on arg
				if(args.length==2){
					String centering = args[1];
					if(centering.equalsIgnoreCase("natural")){
						if(rune!=null){//if we're placing a rune
							placeRuneAtEntity(rune,pattern, world, lookPos, playerFacing, player);
						}else{
							placePatternCentered(pattern, world, lookPos, playerFacing, player);
						}
					}else if(centering.equalsIgnoreCase("topleft")){
						PlacePatternTopLeft(pattern, world, lookPos, playerFacing, player);
					}else if(centering.equalsIgnoreCase("center")){
						placePatternCentered(pattern,world,lookPos,playerFacing,player);
					}
				}else{
					if(rune!=null){//if we're placing a rune
						placeRuneAtEntity(rune,pattern, world, lookPos, playerFacing, player);
					}else{
						placePatternCentered(pattern, world, lookPos, playerFacing, player);
					}
				}
				//PlacePatternTopLeft(pattern, world, lookPos, playerFacing, player);
			}
		}else{
			//TODO JSON on the client. we will need a packet of some sort.
		}
	}
	private void placeRuneAtEntity(IRune rune, ItemStack[][] pattern,World world, BlockPos lookPos, EnumFacing playerFacing,EntityPlayer player) {
		Vec3i pos = rune.getEntityPosition();
		int dX = pos.getX(),dY = pos.getY();
		EnumFacing left = playerFacing.rotateYCCW();
		BlockPos newTL = lookPos.offset(left, dX).offset(playerFacing, dY);
		PlacePatternTopLeft(pattern, world, newTL, playerFacing, player);
	}
	private void placePatternCentered(ItemStack[][] pattern, World world,BlockPos lookPos, EnumFacing playerFacing, EntityPlayer player) {
		int height = pattern.length/TileEntityDustPlaced.ROWS;
		int width = pattern[0].length/TileEntityDustPlaced.COLS;
		int centerX = width/2, centerY = height/2;
		EnumFacing left = playerFacing.rotateYCCW();
		BlockPos newTL = lookPos.offset(left, centerX).offset(playerFacing, centerY);
		PlacePatternTopLeft(pattern, world, newTL, playerFacing, player);
	}
	private void PlacePatternTopLeft(ItemStack[][] pattern, World world, BlockPos topLeft, EnumFacing playerFacing,EntityPlayer player){
		//get the pattern in the right direction
		ItemStack[][] rotatedPattern = PatternUtils.rotateAgainstFacing(pattern, playerFacing);
		//convert to contents array
		ItemStack[][][][] contents = PatternUtils.toContentsArray(rotatedPattern);
		//get the pos for the NW block (an offset from the look/"top-left" block)
		BlockPos nw=topLeft.up();
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
		boolean missing=false;//did we miss some dusts or blocks?
		//contents[0][0] is always NW most block
		for(int r=0;r<contents.length;r++){
			for(int c=0;c<contents[r].length;c++){
				BlockPos current = nw.offset(EnumFacing.EAST, c).offset(EnumFacing.SOUTH, r);
				IBlockState state=world.getBlockState(current);
				Block block = state.getBlock();
				boolean emptyBlock = PatternUtils.isEmpty(contents[r][c]);
				if((block==Blocks.AIR||block==WizardryRegistry.dust_placed) && world.isSideSolid(current.down(), EnumFacing.UP) && !emptyBlock){
					if(block==WizardryRegistry.dust_placed && !player.capabilities.isCreativeMode)block.breakBlock(world, current, state);
					world.setBlockState(current, WizardryRegistry.dust_placed.getDefaultState());
					TileEntity ent = world.getTileEntity(current);
					if(ent instanceof TileEntityDustPlaced){//no reason why it isn't
						TileEntityDustPlaced ted = (TileEntityDustPlaced)ent;
						//ItemStack[][] pat = PatternUtils.rotateAgainstFacing(contents[r][c], playerFacing);
						//ted.setContents(pat);
						//remove from player's inventory if not creative
						if(!player.capabilities.isCreativeMode){
							ItemStack[][] itemStacks = contents[r][c];
							//check one item at a time...
							for (int row = 0; row < itemStacks.length; row++) {
								ItemStack[] stacks = itemStacks[row];
								for (int col = 0; col < stacks.length; col++) {
									ItemStack s = stacks[col];
									int n=0;
									if(s!=null){
										n=s.getCount();
										//n is the number of wanted items
										//n=player.inventory.clearMatchingItems(s.getItem(), s.getMetadata(), s.getCount(), s.getTagCompound());
										for(int i=0;i<player.inventory.getSizeInventory()&&n>0;i++){
											ItemStack playerStack = player.inventory.getStackInSlot(i);
											if(playerStack==null)continue;
											//if the item matches
											if(ItemStack.areItemsEqual(s, playerStack)&&ItemStack.areItemStackTagsEqual(s, playerStack)){
												int originalSize = playerStack.getCount();
												int remainder = originalSize-n;
												playerStack.setCount(remainder>0? remainder : 0);
												if(playerStack.getCount()==0)player.inventory.removeStackFromSlot(i);
												n-= remainder>0? n : originalSize;
											}else if(playerStack.getItem() instanceof ItemDustPouch){												ItemDustPouch pouch = (ItemDustPouch)playerStack.getItem();
											ItemStack dust = pouch.getDustStack(playerStack, n);
											if(ItemStack.areItemsEqual(s,dust)&&ItemStack.areItemStackTagsEqual(s, dust)){
												int originalSize = dust.getCount();
												n-= originalSize;
											}else{
												//re-add the dust if it didn't match
												pouch.addDust(playerStack, dust);
											}
											}

										}
									}
									if(n>0)missing=true;
									//XXX this will update rendering all the time so it might be slow
									if(n==0||s==null||s.getItem() instanceof DustPlaceholder)ted.setInventorySlotContents(TileEntityDustPlaced.getSlotIDfromPosition(row, col), s.copy());
								}
							}
							if(ted.isEmpty()){//remove the TE if we couldn't place any dust in it
								world.removeTileEntity(current);
								world.setBlockToAir(current);
							}
						}else{//in creative mode, just set the contents.
							ted.setContents(contents[r][c]);
						}
						//world.markBlockForUpdate(current);
						world.notifyBlockUpdate(current, state, state, 3);
					}else{
						throw new IllegalStateException("import command: TE was not placed dust");
					}
				}else if(!emptyBlock){
					//we missed a block
					missing=true;
				}
			}
		}
		if(missing){
			player.sendMessage(new TextComponentTranslation(locKey+".incomplete"));
		}
		world.playSound(null, topLeft, SoundEvents.BLOCK_SAND_PLACE, SoundCategory.BLOCKS, 0.7f, 1.5f);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#canCommandSenderUseCommand(net.minecraft.command.ICommandSender)
	 */
	@Override
	public boolean checkPermission(MinecraftServer server,ICommandSender sender) {
		if(!(sender instanceof EntityPlayer))return false;
		if(ConfigHandler.commandImportPermission.equals(ConfigHandler.PERMISSIONS_NONE))return false;
		if(ConfigHandler.commandImportPermission.equals(ConfigHandler.PERMISSIONS_ALL))return true;
		if(ConfigHandler.commandImportPermission.equals(ConfigHandler.PERMISSIONS_OP)){
			String[] ops = server.getPlayerList().getOppedPlayerNames();
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
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,String[] args, BlockPos pos) {
		LinkedList<String> options = new LinkedList<String>();
		for(String id:DustRegistry.getRuneIDs()){
			if(StringUtils.containsIgnoreCase(id, args[0]))options.add(id);
		}
		return options;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#isUsernameIndex(java.lang.String[], int)
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
	public static CommandImportPattern instance() {
		if(instance==null){
			instance = new CommandImportPattern();
		}
		return instance;
	}

}
