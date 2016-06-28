/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-09
 */
package com.zpig333.runesofwizardry.tileentity;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryLogger;

/** This TileEntity replaces a TileEntityDustPlaced when a rune is formed.
 * @author Xilef11
 *
 */
public class TileEntityDustActive extends TileEntityDustPlaced implements ITickable {
	private long ticksExisted=0;
	public StarData stardata = null;
	public BeamData beamdata=null;
	public TileEntityDustActive() {
		super();
	}
	/**
	 * Returns the number of ticks since this was created
	 * @return the number of times the update() method was called on this.
	 */
	public long ticksExisted(){
		return ticksExisted;
	}
	
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#getMaxRenderDistanceSquared()
	 */
	@Override
	public double getMaxRenderDistanceSquared() {
		if((this.beamdata!=null && this.beamdata.doRender)||(this.stardata!=null && this.stardata.doRender)){
			return 65536.0D;
		}else{
			return super.getMaxRenderDistanceSquared();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getRenderBoundingBox()
	 */
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if((this.stardata!=null && this.stardata.doRender)||(this.beamdata!=null && this.beamdata.doRender)){
			return INFINITE_EXTENT_AABB;
		}else{
			return super.getRenderBoundingBox();
		}
	}
	/* (non-Javadoc)
	 * @see net.minecraft.server.gui.IUpdatePlayerListBox#update()
	 */
	@Override
	public void update() {
		if(!initialised)init();
		ticksExisted++;
		if(rune!=null)rune.update();
	}
	
	public void setupStar(int inner, int outer, float size, float sizeY,Vec3d offset){
		stardata = new StarData(inner, outer, size, sizeY, offset);
	}
	public void setupStar(int inner, int outer, float size, float sizeY){
		stardata = new StarData(inner, outer, size, sizeY);
	}
	public void setupBeam(int color, BeamType type,Vec3d offset){
		beamdata = new BeamData(color,type,offset);
	}
	public void setupBeam(int color, BeamType type){
		beamdata = new BeamData(color,type);
	}
	public void setDrawStar(boolean value){
		if(stardata!=null)stardata.doRender=value;
	}
	public void setDrawBeam(boolean value){
		if(beamdata!=null)beamdata.doRender=value;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		ticksExisted = tagCompound.getLong("ticksExisted");
		String runeID = tagCompound.getString("runeID");
		//read the blockpos
		Set<BlockPos> posSet = new LinkedHashSet<BlockPos>();
		NBTTagList positions = tagCompound.getTagList("dustPositions", 11);//11 is int array
		for(int i=0;i<positions.tagCount();i++){
			int[] p = positions.getIntArrayAt(i);
			posSet.add(new BlockPos(p[0], p[1], p[2]));
		}
		//read the Rune's ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Pattern",10);
		List<ArrayElement> items = new LinkedList<ArrayElement>();
		int maxRow=0,maxCol=0;
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			int r = tag.getInteger("Row");
			int c = tag.getInteger("Col");
			ItemStack s = ItemStack.loadItemStackFromNBT(tag);
			items.add(new ArrayElement(r, c, s));
			if(r>maxRow)maxRow=r;
			if(c>maxCol)maxCol=c;
		}
		//make sure its the right size
		int modR = maxRow%TileEntityDustPlaced.ROWS;
		if(modR!=0)maxRow+=(TileEntityDustPlaced.ROWS-modR);
		int modC = maxCol%TileEntityDustPlaced.COLS;
		if(modC!=0)maxCol+=(TileEntityDustPlaced.COLS-modC);
		ItemStack[][] stacks = new ItemStack[maxRow+1][maxCol+1];

		for(ArrayElement a:items){
			stacks[a.row][a.col]=a.stack;
		}
		EnumFacing facing = EnumFacing.byName(tagCompound.getString("Facing"));
		boolean renders = tagCompound.getBoolean("renderActive");
		//re-create the rune
		IRune rune = DustRegistry.getRuneByID(runeID);
		if(rune==null){
			WizardryLogger.logError("Active Dust at "+getPos()+" loaded invalid runeID "+runeID+" from NBT");
			this.rune=null;//this should make the rune do nothing.
		}else{
			RuneEntity entity = rune.createRune(stacks,facing, posSet, this);
			entity.renderActive=renders;
			this.rune=entity;
			//init should get called on the first tick to set up the other dust blocks
			initialised=false;
			this.rune.readFromNBT(tagCompound);
		}
		if(tagCompound.hasKey("star")){
			stardata = new StarData();
			stardata.readNBT(tagCompound.getCompoundTag("star"));
		}
		if(tagCompound.hasKey("beam")){
			beamdata = new BeamData();
			beamdata.readNBT(tagCompound.getCompoundTag("beam"));
		}
	}
	private boolean initialised=false;
	private void init(){
		if(initialised ||rune==null|| worldObj==null)return;
		for(BlockPos p : rune.dustPositions){
			TileEntity te = worldObj.getTileEntity(p);
			if(te instanceof TileEntityDustPlaced){
				((TileEntityDustPlaced)te).setRune(rune);
			}else{
				WizardryLogger.logError("TileEntity at pos: "+p+" wasn't placed dust! (TEDustActive#init)");
			}
		}
		rune.isAnyBlockPowered();
		initialised=true;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if(rune!=null){
			rune.writeToNBT(tagCompound);
			tagCompound.setString("runeID", DustRegistry.getRuneID(rune.creator));
			//write the rune's blockpos set
			NBTTagList positions = new NBTTagList();
			for(BlockPos p: rune.dustPositions){
				NBTTagIntArray current = new NBTTagIntArray(new int[]{p.getX(),p.getY(),p.getZ()});
				positions.appendTag(current);
			}
			tagCompound.setTag("dustPositions",positions);
			//write the rune's ItemStacks
			NBTTagList itemList = new NBTTagList();
			for (int r = 0; r < rune.placedPattern.length; r++) {
				for(int c=0;c<rune.placedPattern[r].length;c++){
					ItemStack stack = rune.placedPattern[r][c];
					NBTTagCompound tag = new NBTTagCompound();
					if (stack != null) {
						stack.writeToNBT(tag);
						tag.setInteger("Row", r);
						tag.setInteger("Col", c);
						itemList.appendTag(tag);
					}
				}
			}
			tagCompound.setTag("Pattern", itemList);
			tagCompound.setTag("Facing", new NBTTagString(rune.face.getName()));
			tagCompound.setBoolean("renderActive", rune.renderActive);
		}
		tagCompound.setLong("ticksExisted", ticksExisted);
		if(stardata!=null)tagCompound.setTag("star", stardata.writeNBT(new NBTTagCompound()));
		if(beamdata!=null)tagCompound.setTag("beam", beamdata.writeNBT(new NBTTagCompound()));
		return tagCompound;
	}
	//
	private class ArrayElement{
		private int row;
		private int col;
		private ItemStack stack;
		private ArrayElement(int r, int c, ItemStack s){
			row=r;
			col=c;
			stack=s;
		}
	}
	/** This class holds the data and some utility methods to configure rendering of the star**/
	public static class StarData{
		public int innercolor, outercolor;
		public boolean doRender;
		public float scale, yscale;
		//offset from the active TE
		public Vec3d offset;
		private StarData(){
			//empty constructor for when we want to read from NBT
			doRender=false;
		}
		public StarData(int inner, int outer, float size, float sizeY,Vec3d offset){
			innercolor=inner;
			outercolor = outer;
			scale=size;
			yscale=sizeY;
			doRender=false;
			this.offset=offset;
		}
		public StarData(int inner, int outer, float size, float sizeY){
			this(inner,outer,size,sizeY,new Vec3d(0,0,0));
		}
		public NBTTagCompound writeNBT(NBTTagCompound tag){
			tag.setInteger("starInner", innercolor);
			tag.setInteger("starOuter", outercolor);
			tag.setFloat("starScale", scale);
			tag.setFloat("starScaleY", yscale);
			tag.setBoolean("starRender", doRender);
			tag.setDouble("starOffsetX", offset.xCoord);
			tag.setDouble("starOffsetY", offset.yCoord);
			tag.setDouble("starOffsetZ", offset.zCoord);
			return tag;
		}
		public void readNBT(NBTTagCompound tag){
			innercolor = tag.getInteger("starInner");
			outercolor = tag.getInteger("starOuter");
			scale = tag.getFloat("starScale");
			yscale = tag.getFloat("starScaleY");
			doRender=tag.getBoolean("starRender");
			double dx = tag.getDouble("starOffsetX"),
				   dy = tag.getDouble("starOffsetY"),
				   dz = tag.getDouble("starOffsetZ");
			offset=new Vec3d(dx,dy,dz);
		}
	}
	public static enum BeamType {BEACON, SPIRAL, RINGS};
	/** This class holds the data and some utility methods to configure rendering of the beam**/
	public static class BeamData{
		public int color;
		public boolean doRender;
		public BeamType type;
		public Vec3d offset;
		/** if false, the beam will "vibrate" instead of rotating like the normal beacon**/
		public boolean doesRotate;
		public int height;
		public double beamRadius,glowRadius;
		private BeamData(){
			doRender=false;
		}
		public BeamData(int color, BeamType type,Vec3d offset){
			this.color=color;
			this.type=type;
			doRender=false;
			this.offset=offset;
			this.doesRotate=true;
			this.height=128;
			this.beamRadius=0.2;
			this.glowRadius=0.25;
		}
		public BeamData(int color, BeamType type){
			this(color,type,new Vec3d(0,0,0));
		}
		public NBTTagCompound writeNBT(NBTTagCompound tag){
			tag.setInteger("beamColor", color);
			tag.setString("beamType", type.name());
			tag.setBoolean("beamRender", doRender);
			tag.setBoolean("beamRotate", doesRotate);
			tag.setInteger("beamHeight", height);
			tag.setDouble("beamOffsetX", offset.xCoord);
			tag.setDouble("beamOffsetY", offset.yCoord);
			tag.setDouble("beamOffsetZ", offset.zCoord);
			tag.setDouble("beamRad", beamRadius);
			tag.setDouble("beamGlowRad", glowRadius);
			return tag;
		}
		public void readNBT(NBTTagCompound tag){
			color = tag.getInteger("beamColor");
			doRender=tag.getBoolean("beamRender");
			doesRotate = tag.getBoolean("beamRotate");
			height = tag.getInteger("beamHeight");
			type = BeamType.valueOf(tag.getString("beamType"));
			offset = new Vec3d(tag.getDouble("beamOffsetX"),tag.getDouble("beamOffsetY"),tag.getDouble("beamOffsetZ"));
			beamRadius=tag.getDouble("beamRad");
			glowRadius=tag.getDouble("beamGlowRad");
		}
	}

	
	
	
}
