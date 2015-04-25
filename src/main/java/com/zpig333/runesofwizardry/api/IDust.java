package com.zpig333.runesofwizardry.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

/** Extend this to create a dust (you also need to register it)
 * 
 */
public abstract class IDust extends Item {
    /** the ID of this dust. for use by the dust registry. **/
    private int id;
    
    /** returns the ID of this dust. only the dust registry should use this 
      (note: visibility of this is package)
    **/
    int getId(){return id;}
    /** sets the ID of this dust. for use by the dust registry only. **/
    void setId(int newId){this.id=newId;}
    /**determines if this has a custom icon**/
    private boolean hasCustomIcon=true;
    public IDust(){
        setCreativeTab(RunesOfWizardry.wizardry_tab);
        setUnlocalizedName("dust_"+getDustName());
    }

    /** returns the name of the dust.
     * <br/> the (default) unlocalized name will be dust_[whatever this returns]
     * @return the dust name
     */
    public abstract String getDustName();
    /**returns the primary color of the dust (can be based on metadata/nbt)
     * 
     * @return the primary color of the dust
     */
    public abstract int getPrimaryColor(ItemStack stack);
    /**returns the secondary color of the dust
     * 
     * @return the secondary color of the dust
     */
    public abstract int getSecondaryColor(ItemStack stack);
    /** returns the placed color of the dust
     * 
     * @return (default) the primary color of the dust
     */
    public int getPlacedColor(ItemStack stack){
        return getPrimaryColor(stack);
    }
    /** returns the item used to obtain this dust by infusing inert dust.
     * @return - the item used to infuse this dust. (has to be an ItemStack for metadata)
     * <br/>- <code>null</code> for custom crafting mechanics
     */
    //XXX not sure how we would handle the ItemStack parameter for infusion...
    public abstract ItemStack[] getInfusionItems(ItemStack stack);
    /** returns whether or not this dust uses a custom block for storage. 
     * if false (default), a storage block will be generated when registering this dust with the DustRegistery
     * NOTE: the default block is not a TileEntity and will not handle NBT for color.
     * @return true to disable the automatic generation of a storage block.
     */
    public boolean usesCustomBlock(){
    	return false;
    }
    
   //TODO don't forget to do the textures
    
    /** what happens when the dust is used. places the dust by default, override for custom behaviour
     * 
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){

        if(world.isRemote){
            return true;
        }
        else {
            Block block = world.getBlockState(pos).getBlock();
            if (block == Blocks.vine || block == Blocks.tallgrass || block == Blocks.deadbush || block == WizardryRegistry.dust_placed || block == Blocks.snow_layer) {
                return false;
            }
            //TODO seems like we will need a BlockState implementation for this
            //world.setBlockState(pos.add(0, 1, 0), WizardryRegistry.dust_placed);
            world.playSoundEffect((double)(pos.getX() + 0.5F), (double)(pos.getY() + 0.5F), (double)(pos.getZ() + 0.5F), Block.soundTypeSand.getPlaceSound(), (Block.soundTypeSand.getVolume() + 1.0F) / 2.0F, Block.soundTypeGrass.getFrequency() * 0.8F);
            return true;
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
        if(hasCustomIcon)return 16777215;
        //otherwise, return the colors of the dust
        IDust dust = DustRegistry.getDustFromItemStack(stack);
        return pass == 0 ? dust.getPrimaryColor(stack) : dust.getSecondaryColor(stack);
       
    }

}
