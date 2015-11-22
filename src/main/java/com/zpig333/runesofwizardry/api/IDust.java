package com.zpig333.runesofwizardry.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import amerifrance.guideapi.api.abstraction.IPage;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
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
	/** Returns the description of this dust for the Runic Dictionary
	 * @param meta the metadata value for the dust with this description
	 * @return the unlocalized String for the description
	 */
	public abstract String getDescription(int meta);
	
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

	/** returns the items used to obtain this dust by infusing inert dust. /!\ MAX OF 8 Stacks for now
	 * @note If you want to use NBT to have different dust types, return {@code null} here, since the registering method does not handle it, 
	 * and manually call {@link DustRegistry#registerBlockInfusion(ItemStack[], ItemStack, ItemStack)}<br/>
	 * Also, Note that the block to be infused will always show up in the guide book as an inert dust block
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

	/**return the modid under which to register this dust. 
	 * @return (default) to runesofwizardry
	 **/
	public String getmodid(){
		return References.modid;
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
	/** returns pages (from Guide-API) for the additional crafting steps (other than the infusion) 
	 *  required for this dust. They will get added between the description of the dust and the infusion
	 * @param the metadata value (from {@link IDust#getMetaValues()}) for these pages
	 * @return a list of IPage to add between description and infusion. an empty ArrayList by default.
	 */
	public List<IPage> getAdditionalCraftingPages(int meta){
		return new ArrayList<IPage>();
	}
	/** what happens when the dust is used. places the dust by default,<br/>
	 *  override for custom behaviour, but don't forget to call super.onItemUse()
	 * 
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		//see ItemRedstone#onItemUse
		if(world.isRemote){
			return true;
		}
		else {
			//can't place dust on certain blocks...
			Block block = world.getBlockState(pos).getBlock();
			if (block == Blocks.vine || block == Blocks.tallgrass || block == Blocks.deadbush || block == WizardryRegistry.dust_placed || block == Blocks.snow_layer) {
				return false;
			}if(block == WizardryRegistry.dust_placed){
				return true;
			}else{
				world.setBlockState(pos.up(), WizardryRegistry.dust_placed.getDefaultState());
				IBlockState state =  world.getBlockState(pos.up());
				state.getBlock().onBlockActivated(world, pos.up(), state, player, side, hitX, hitY, hitZ);
				return true;
			}
		}
	}

	/** sets the item's color based on the itemstack
	 * 
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		//if there is a custom icon registered, return the same thing as Item
		if(hasCustomIcon())return 16777215;
		//otherwise, return the colors of the dust
		IDust dust = DustRegistry.getDustFromItemStack(stack);
		return pass == 0 ? dust.getPrimaryColor(stack) : dust.getSecondaryColor(stack);

	}
	/** DOes this dust appear in the "dusts" category of the guide?
	 * 
	 * @return true by default
	 */
	public boolean appearsInGuideBook() {
		return true;
	}

}
