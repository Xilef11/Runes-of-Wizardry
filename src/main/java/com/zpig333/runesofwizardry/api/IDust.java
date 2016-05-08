package com.zpig333.runesofwizardry.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.WizardryRegistry;


/** Extend this to create a dust (you also need to register it)
 * 
 */
public abstract class IDust extends Item {

	public IDust(){

	}

	/** returns the name of the dust.
	 * <br/> the (default) name will be dust_[whatever this returns]
	 * @return the dust name
	 */
	public abstract String getDustName();

	/**returns the primary color of the dust (can be based on metadata/nbt)
	 * 
	 * @return the primary color of the dust (0xRRGGBB is the suggested format to make your life easier)
	 */
	public abstract int getPrimaryColor(ItemStack stack);
	/**returns the secondary color of the dust
	 * 
	 * @return the secondary color of the dust (0xRRGGBB is the suggested format to make your life easier)
	 */
	public abstract int getSecondaryColor(ItemStack stack);
	/** returns the placed color of the dust
	 * 
	 * @return (default) the primary color of the dust
	 */
	public int getPlacedColor(ItemStack stack){
		return getPrimaryColor(stack);
	}
	/**
	 * Returns wether this dust should be matched with DustRegistry.MAGIC_DUST in runes
	 * @param stack the stack that contains this dust
	 * @return true by default
	 */
	public boolean isMagicDust(ItemStack stack){
		return true;
	}
	/** returns the items used to obtain this dust by infusing inert dust. /!\ MAX OF 8 Stacks for now
	 * @note If you want to use NBT to have different dust types, return {@code null} here, since the registering method does not handle it, 
	 * and manually call {@link DustRegistry#registerBlockInfusion(ItemStack[], ItemStack, ItemStack)}
	 * @return - the items used to infuse this dust. (has to be an ItemStack for metadata)
	 * <br/>- <code>null</code> for custom crafting mechanics
	 */
	public abstract ItemStack[] getInfusionItems(ItemStack stack);

	/** returns whether or not this dust uses a custom block for storage. 
	 * if false (default), a storage block will be generated when registering this dust with the DustRegistery
	 * NOTE: the default block is not a TileEntity and will not handle NBT for color.
	 * @return true to disable the automatic generation of a storage block.
	 */
	public boolean hasCustomBlock(){
		return false;
	}
	/** returns the custom block for this dust, if appropriate.
	 * <br/>Note that there will be a recipe added for all metadata values as follows:
	 * <br/> ItemStack(dust, meta) * 9 => ItemStack(block,meta)
	 * @return the custom block for this dust
	 */
	public IDustStorageBlock getCustomBlock(){
		return null;
	}
	/**determines if this has a custom icon
	 * if false (default), a texture will be generated when registering this dust with the DustRegistery
	 * @return false by default
	 **/
	public boolean hasCustomIcon(){
		return false;
	}
	/** returns all metadata values that are used for this dust
	 * 
	 * @return [0] by default
	 */
	public int[] getMetaValues(){
		return new int[]{0};
	}
	/**returns a name for this dust. 
	 * @return (default) dust_[getDustName]**/
	public String getName(){
		return "dust_"+getDustName();
	}

	/** return the creative tab for this dust. not to be confused with {@link Item#getCreativeTab()}
	 * @return (default) the Runes of Wizardry creative tab
	 **/
	public CreativeTabs creativeTab(){
		return RunesOfWizardry.wizardry_tab;
	}
	/** returns whether or not this dust should be rendered as connected to a second one when placed
	 * 
	 * @param thisDust the stack that contains this dust
	 * @param otherDust the stack that contains the other dust 
	 * @return true for the dusts to connect (checks if the itemstacks are equal by default)
	 */
	public boolean shouldConnect(ItemStack thisDust, ItemStack otherDust){
		if(thisDust==null || otherDust==null)return false;
		if(!(thisDust.getItem() instanceof IDust && otherDust.getItem() instanceof IDust))return false;
		return ItemStack.areItemStacksEqual(thisDust, otherDust);
	}
	/**
	 *  Returns wether the dusts in the stacks are equal for rune purposes
	 * @param thisDust the first dust to compare
	 * @param other the second dust to compare
	 * @return true if both stacks contain dusts and they match, false otherwise
	 */
	public boolean dustsMatch(ItemStack thisDust, ItemStack other){
		if(thisDust==other)return true;//efficiency
		if(thisDust==null || other == null)return false;//only one is null
		//make sure both are dusts
		if(!(thisDust.getItem() instanceof IDust && other.getItem() instanceof IDust))return false;
		return (ItemStack.areItemStacksEqual(thisDust, other));
	}
	/** what happens when the dust is used. places the dust by default,<br/>
	 *  override for custom behaviour, but don't forget to call super.onItemUse()
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		//see ItemRedstone#onItemUse
		if(world.isRemote){
			return EnumActionResult.SUCCESS;
		}
		else {
			//can't place dust on certain blocks...
			Block block = world.getBlockState(pos).getBlock();
			if (block == Blocks.VINE || block == Blocks.TALLGRASS || block == Blocks.DEADBUSH || block == WizardryRegistry.dust_placed || block == Blocks.SNOW_LAYER) {
				return EnumActionResult.PASS;
			}if(block == WizardryRegistry.dust_placed){
				return EnumActionResult.SUCCESS;
			}else{
				world.setBlockState(pos.up(), WizardryRegistry.dust_placed.getDefaultState());
				IBlockState state =  world.getBlockState(pos.up());
				return state.getBlock().onBlockActivated(world, pos.up(), state, player, hand, stack, facing, hitX, hitY, hitZ)? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
				//return true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getUnlocalizedName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String base = super.getUnlocalizedName(stack);
		if(getMetaValues().length>1){
			base+="."+stack.getMetadata();
		}
		return base;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getSubItems(net.minecraft.item.Item, net.minecraft.creativetab.CreativeTabs, java.util.List)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab,List<ItemStack> subItems) {
		for(int meta:getMetaValues()){
			subItems.add(new ItemStack(itemIn,1,meta));
		}
	}

}
