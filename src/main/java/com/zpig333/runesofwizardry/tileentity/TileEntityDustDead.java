package com.zpig333.runesofwizardry.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zpig333.runesofwizardry.core.WizardryRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;

public class TileEntityDustDead extends TileEntityDustPlaced implements
ITickable {
	//we go with a ticking TE because the random ticks are not frequent enough (by default, on average once every 65 seconds according to math, once every ~40 sec. experimentally)
	private int nextTick=-1;
	private static final int MAX_DELAY=5*20,
			BASE_DELAY=2*20;
	
	private List<Integer> searchorder = new ArrayList<Integer>(getSizeInventory());
	public TileEntityDustDead() {
		super();
		for(int i=0;i<getSizeInventory();i++) searchorder.add(i);
		Collections.shuffle(searchorder);
	}
	@Override
	public void update() {
		if(nextTick<0&&!world.isRemote){
			nextTick = BASE_DELAY + world.rand.nextInt(MAX_DELAY);
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
		nextTick--;
		if(nextTick==0){
			if(world.isRemote){
				for (int i = 0; i<world.rand.nextInt(4)+1; i++){
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+Math.random(), pos.getY()+Math.random()/2D, pos.getZ()+Math.random(), 0.07, 0.01D, 0.07D);
				}
			}else{
				// remove a dead dust stack
				for(int i:searchorder) {
					ItemStack stack = getStackInSlot(i);
					if(!stack.isEmpty() && stack.getItem()==WizardryRegistry.dust_dead) {
						setInventorySlotContents(i, ItemStack.EMPTY);
						nextTick = BASE_DELAY + world.rand.nextInt(MAX_DELAY);
						if(isEmpty()){//if there is no more dust, break the block
							world.setBlockToAir(pos);
						}
						IBlockState state = world.getBlockState(pos);
						world.notifyBlockUpdate(pos, state, state, 3);
						break;
					}
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.nextTick=tagCompound.getInteger("ticksExisted");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound = super.writeToNBT(tagCompound);
		tagCompound.setInteger("ticksExisted", nextTick);
		return tagCompound;
	}


}
