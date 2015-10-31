package com.zpig333.runesofwizardry.api;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


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

	/** returns the items used to obtain this dust by infusing inert dust. /!\ MAX OF 9 Stacks for now
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
	public final boolean hasCustomBlock(){
		return true;
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
	/** what happens when the dust is used. places the dust by default,<br/>
	 *  override for custom behaviour, but don't forget to call super.onItemUse()
	 * 
	 */
	@Override

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int posX, int posY, int posZ, int side, float hitX, float hitY, float hitZ){
		//see ItemRedstone#onItemUse
		if(world.isRemote){
			return true;
		}
		else {
			//can't place dust on certain blocks...
			Block block = world.getBlock(posX, posY, posZ);
			if (block == Blocks.vine || block == Blocks.tallgrass || block == Blocks.deadbush || block == WizardryRegistry.dust_placed || block == Blocks.snow_layer) {
				return false;
			}if(block == WizardryRegistry.dust_placed){
				return true;
			}else{
				world.setBlock(posX, posY+1, posZ, WizardryRegistry.dust_placed);
				Block myBlock =  world.getBlock(posX, posY+1, posZ);
				myBlock.onBlockActivated(world, posX,posY+1,posZ, player, side, hitX, hitY, hitZ);
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

	//ICONS
	//Stuff from ItemDustPieces
	private IIcon icon_foreground;
	private IIcon icon_background;

	/**Gets an icon index based on an item's damage value and the given render pass. 
	 * <br/>Override this if your custom dust uses metadata/render pass to change its icon
	 * 
	 */
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass){
		if(this.hasCustomIcon())return this.itemIcon;
		if(pass == 0){
			return icon_background;
		}else {
			return icon_foreground;
		}
	}
	/** does the item require multiple render passes?
	 * @return (default) true
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	/** sets the icon of the dust.
	 * default is based on the primary and secondary colors. 
	 * override for custom icon
	 * 
	 * @param ireg 
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ireg){
		//just the plain one for now
		icon_foreground = ireg.registerIcon(References.texture_path + "dust_item_fore");
		icon_background = ireg.registerIcon(References.texture_path + "dust_item_back");
	}

}
