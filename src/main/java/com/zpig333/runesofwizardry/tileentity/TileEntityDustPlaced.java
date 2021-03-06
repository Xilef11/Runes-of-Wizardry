/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-18
 */
package com.zpig333.runesofwizardry.tileentity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**The TileEntity that holds placed dust
 * @author Xilef11
 *
 */
public class TileEntityDustPlaced extends TileEntity implements IInventory{
	public static final int ROWS=4, COLS=4;

	//the dusts placed in this block
	protected ItemStack[][] contents = new ItemStack[ROWS][COLS];
	protected RuneEntity rune=null;
	//the colors for rendering the center of the dusts
	private int[][] centralColors;
	//the internal connector data
	private Set<int[]> internalConnectors;
	//external connector data
	private List<int[]> externalConnectors;
	/* return the coordinates of a slot based on its id
	 * NORTH (Z-)
	 * [0][1][2][3]
	 * [4][5][6][7]		EAST (X+)
	 * [8][9][10][11]
	 * [12][13][14][15]
	 */
	public static int[] getPositionFromSlotID(int id){
		int row = id / ROWS;
		int col = id % COLS;
		return new int[]{row,col};
	}
	//the other way around
	public static int getSlotIDfromPosition(int row, int col){
		return row * ROWS + col;
	}

	public TileEntityDustPlaced() {
		super();
		for(int i=0;i<contents.length;i++){
			Arrays.fill(contents[i],ItemStack.EMPTY);
		}
	}
	/**
	 * Returns the Contents of this TE (not a copy)
	 * @return
	 */
	public ItemStack[][] getContents(){
		return contents;
	}
	/**sets the contents of this TE
	 * 
	 * @throws IllegalArgumentException if the given ItemStack[][] is not of the right size
	 */
	public void setContents(ItemStack[][] stacks){
		if(stacks.length!=ROWS || stacks[0].length!=COLS)throw new IllegalArgumentException("Contents must be "+ROWS+" by "+COLS );
		this.contents=stacks;
	}
	/** returns the rune associated with this dust**/
	public RuneEntity getRune(){
		return rune;
	}
	public void setRune(RuneEntity rune){
		this.rune=rune;
	}
	/**returns the color of all center points**/
	public int[][] getCenterColors(){
		if(centralColors==null)updateCenterColors();
		return centralColors;
	}
	//updates the array of central colors
	protected void updateCenterColors(){
		int[][]result = new int[ROWS][COLS];
		for(int i=0;i<result.length;i++){
			for(int j=0;j<result[i].length;j++){
				//FIXME must not have properly converted the stack somewhere...
				//if(contents[i][j]!.isEmpty()){
				if(!contents[i][j].isEmpty()){
					result[i][j]=DustRegistry.getDustFromItemStack(contents[i][j]).getPlacedColor(contents[i][j]);
				}else{
					result[i][j]=-1;
				}
			}
		}
		centralColors=result;
	}
	/**returns the data on the internal connectors to draw
	 * 
	 * @return a set of arrays with the following structure: [row1, col1, row2, col2, color1, color2]
	 */
	public Set<int[]> getInternalConnectors(){
		if (internalConnectors==null) updateInternalConnectors();
		return internalConnectors;
	}
	//update the data for the internal connectors
	protected void updateInternalConnectors(){
		HashSet<int[]> result = new HashSet<>();
		for(int i=0;i<contents.length;i++){
			for(int j=0;j<contents[i].length;j++){
				if(i+1<contents.length && dustsMatch(contents[i][j],contents[i+1][j])){
					int color1 = DustRegistry.getDustFromItemStack(contents[i][j]).getPlacedColor(contents[i][j]);
					int color2 = DustRegistry.getDustFromItemStack(contents[i+1][j]).getPlacedColor(contents[i+1][j]);
					result.add(new int[]{i,j,i+1,j, color1,color2});
				}if(j+1<contents[i].length && dustsMatch(contents[i][j],contents[i][j+1])){
					int color1 = DustRegistry.getDustFromItemStack(contents[i][j]).getPlacedColor(contents[i][j]);
					int color2 = DustRegistry.getDustFromItemStack(contents[i][j+1]).getPlacedColor(contents[i][j+1]);
					result.add(new int[]{i,j,i,j+1, color1,color2});
				}
			}
		}

		internalConnectors = result;
	}
	public void updateRendering(){
		this.updateCenterColors();
		this.updateExternalConnectors();
		this.updateInternalConnectors();
	}
	/**returns the data on the external connectors
	 * @return a Linked List of int[] in the following format: [row,col,color, index of the facing]
	 * @see EnumFacing#getIndex()
	 * @see EnumFacing#getFront(int)
	 */
	public List<int[]>getExternalConnectors(){
		if(externalConnectors==null)updateExternalConnectors();
		//updateExternalConnectors();
		return externalConnectors;
	}
	//update the external connectors
	public void updateExternalConnectors(){
		List<int[]> result = new LinkedList<>();
		//world was null when destroyed by explosion
		if(world==null||pos==null){
			RunesOfWizardry.log().error("world or pos was null for TED at "+world+" "+pos);
			return;
		}
		if(world.getBlockState(pos.north()).getBlock() == WizardryRegistry.dust_placed){
			TileEntityDustPlaced ted = (TileEntityDustPlaced)world.getTileEntity(pos.north());
			if(ted!=null){
				for(int i=0;i<COLS;i++){
					int id=getSlotIDfromPosition(0, i);
					int otherSlot=id+((ROWS-1)*COLS);
					if(!contents[0][i].isEmpty()){
						if(dustsMatch(contents[0][i], ted.getStackInSlot(otherSlot))){
							result.add(new int[]{0,i,DustRegistry.getDustFromItemStack(contents[0][i]).getPlacedColor(contents[0][i]),EnumFacing.NORTH.getIndex()});
						}
					}
				}
			}
		}
		if(world.getBlockState(pos.south()).getBlock() == WizardryRegistry.dust_placed){
			TileEntityDustPlaced ted = (TileEntityDustPlaced)world.getTileEntity(pos.south());
			if(ted!=null){
				for(int i=0;i<COLS;i++){
					int id=getSlotIDfromPosition(ROWS-1, i);
					int otherSlot=id-((ROWS-1)*COLS);
					if(!contents[ROWS-1][i].isEmpty()){
						if(dustsMatch(contents[ROWS-1][i], ted.getStackInSlot(otherSlot))){
							result.add(new int[]{ROWS-1,i,DustRegistry.getDustFromItemStack(contents[ROWS-1][i]).getPlacedColor(contents[ROWS-1][i]),EnumFacing.SOUTH.getIndex()});
						}
					}
				}
			}
		}
		if(world.getBlockState(pos.west()).getBlock() == WizardryRegistry.dust_placed){
			TileEntityDustPlaced ted = (TileEntityDustPlaced)world.getTileEntity(pos.west());
			if(ted!=null){
				for(int i=0;i<ROWS;i++){
					int id=getSlotIDfromPosition(i,0);
					int otherSlot=id+(COLS-1);
					if(!contents[i][0].isEmpty()){
						if(dustsMatch(contents[i][0], ted.getStackInSlot(otherSlot))){
							result.add(new int[]{i,0,DustRegistry.getDustFromItemStack(contents[i][0]).getPlacedColor(contents[i][0]),EnumFacing.WEST.getIndex()});
						}
					}
				}
			}
		}
		if(world.getBlockState(pos.east()).getBlock() == WizardryRegistry.dust_placed){
			TileEntityDustPlaced ted = (TileEntityDustPlaced)world.getTileEntity(pos.east());
			if(ted!=null){
				for(int i=0;i<ROWS;i++){
					int id=getSlotIDfromPosition(i,COLS-1);
					int otherSlot=id-(COLS-1);
					if(!contents[i][COLS-1].isEmpty()){
						if(dustsMatch(contents[i][COLS-1], ted.getStackInSlot(otherSlot))){//should be true OK
							result.add(new int[]{i,COLS-1,DustRegistry.getDustFromItemStack(contents[i][COLS-1]).getPlacedColor(contents[i][COLS-1]),EnumFacing.EAST.getIndex()});
						}
					}
				}
			}
		}
		externalConnectors=result;
	}
	private boolean dustsMatch(ItemStack stack1, ItemStack stack2){
		if(!stack1.isEmpty() && stack1.getItem()instanceof IDust){
			IDust dust1 = DustRegistry.getDustFromItemStack(stack1);
			return dust1.shouldConnect(stack1, stack2);
		}
		if(!stack2.isEmpty() && stack2.getItem() instanceof IDust){
			IDust dust2 = DustRegistry.getDustFromItemStack(stack2);
			return dust2.shouldConnect(stack2, stack1);
		}
		return false;//if not at least one is a non-null IDust, should not connect.

	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass==1;
	}
	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		return 32*32;
	};
	//	@SideOnly(Side.CLIENT)
	//	@Override
	//	public net.minecraft.util.AxisAlignedBB getRenderBoundingBox() {
	//		return INFINITE_EXTENT_AABB;
	//	};

	@Override
	public String getName() {
		return References.modid+".DustPlaced";
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public ITextComponent getDisplayName() {
		//see TileEntityDustDye
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
	}

	@Override
	public int getSizeInventory() {
		return ROWS*COLS;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		int[] coords=getPositionFromSlotID(index);
		return contents[coords[0]][coords[1]];
	}
	/** returns true if there are no more itemStacks in**/
	public boolean isEmpty(){
		for(int i=0;i<contents.length;i++){
			for(int j=0;j<contents[i].length;j++){
				if(!contents[i][j].isEmpty())return false;
			}
		}
		return true;
	}
	/** returns true if this block is part of a rune**/
	public boolean isInRune(){
		return rune!=null;
	}
	@Override
	public ItemStack decrStackSize(int index, int count) {
		int[] co=getPositionFromSlotID(index);
		if (!this.contents[co[0]][co[1]].isEmpty())
		{
			ItemStack itemstack;

			if (this.contents[co[0]][co[1]].getCount() <= count)
			{
				itemstack = this.contents[co[0]][co[1]];
				this.contents[co[0]][co[1]] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.contents[co[0]][co[1]].splitStack(count);

				if (this.contents[co[0]][co[1]].getCount() == 0)
				{
					this.contents[co[0]][co[1]] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = getStackInSlot(index);
		if (!stack.isEmpty()) {
			setInventorySlotContents(index, ItemStack.EMPTY);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		int[] co=getPositionFromSlotID(index);
		contents[co[0]][co[1]]=stack;
		if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		} 
		//update the rendering stuff
		updateCenterColors();
		updateInternalConnectors();
		updateExternalConnectors();
		//update neighbors
		//world.notifyNeighborsOfStateChange(getPos(), getBlockType()); NOT working FSR
		updateNeighborConnectors();

	}
	public void updateNeighborConnectors(){
		updateNeighborConnectors(getWorld(), getPos());
	}
	public static void updateNeighborConnectors(World worldIn, BlockPos pos){
		for(EnumFacing dir : EnumFacing.HORIZONTALS){
			TileEntity te =worldIn.getTileEntity(pos.offset(dir));
			if(te instanceof TileEntityDustPlaced){
				((TileEntityDustPlaced)te).updateExternalConnectors();
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		// only 1 item per slot (does this even do anything?)
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// Handled with onBlockActivated?
		return false;
	}

	//Next 2 methods are an attempt to sync stuff
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getDescriptionPacket()
	 */
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		//this didn't seem to sync properly - getUpdateTag seems to do it, but it would be wise to ask
		//what botania does
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new SPacketUpdateTileEntity(pos, -999, tagCompound);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#onDataPacket(net.minecraft.network.NetworkManager, net.minecraft.network.play.server.S35PacketUpdateTileEntity)
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
		//		//also update the rendering
		updateCenterColors();
		updateInternalConnectors();
		updateExternalConnectors();
		updateNeighborConnectors();
		//world.notifyBlockOfStateChange(getPos(), getBlockType());
		//world.notifyNeighborsOfStateChange(getPos(), getBlockType());
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getUpdateTag()
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(super.getUpdateTag());
	}
	@Override
	public void openInventory(EntityPlayer player) {
		// not using this

	}

	@Override
	public void closeInventory(EntityPlayer player) {
		//not using this
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// only let dust in
		return stack.getItem() instanceof IDust;
	}
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		this.clear();//Necessary because null stacks don't get saved to NBT, causing removed items to stay on the client until reload
		NBTTagList tagList = tagCompound.getTagList("Inventory",10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				int[] coords = getPositionFromSlotID(slot);
				contents[coords[0]][coords[1]] = new ItemStack(tag);
			}
		}
		if(!tagCompound.getBoolean("inRune"))this.rune=null;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setBoolean("inRune", isInRune());
		return tagCompound;
	}
	//NOT using the following field methods
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// let's just do the same thing as inventoryBasic
		for(int i=0;i<contents.length;i++){
			for(int j=0;j<contents[i].length;j++){
				contents[i][j]=ItemStack.EMPTY;
			}
		}
	}
}
