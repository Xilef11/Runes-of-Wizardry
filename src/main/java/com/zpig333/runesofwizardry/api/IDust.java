package com.zpig333.runesofwizardry.api;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
    

    /**
     * XXX probably useless
     * @return the (unlocalized) name of the dust
     */
    public String getDustName(){
        return getUnlocalizedName();
    }
    /**returns the primary color of the dust
     * 
     * @return the primary color of the dust
     */
    public abstract int getPrimaryColor();
    /**returns the secondary color of the dust
     * 
     * @return the secondary color of the dust
     */
    public abstract int getSecondaryColor();
    /** returns the placed color of the dust
     * 
     * @return (default) the primary color of the dust
     */
    public int getPlacedColor(){
        return getPrimaryColor();
    }
    /** returns the item used to obtain this dust by infusing inert dust.
     * @return - the item used to infuse this dust. (has to be an ItemStack for metadata)
     * <br/>- <code>null</code> for custom crafting mechanics
     */
    public abstract ItemStack getInfusionItem();
    
    /** sets the icon of the dust.
     * default is based on the primary and secondary colors. 
     * override for custom icon
     * 
     * @param ireg 
     */
    @Override
    public void registerIcons(IIconRegister ireg){
        //TODO stuff with the colors
    }
    /** what happens when the dust is used. places the dust by default, override for custom behaviour
     * 
     */
    public boolean onItemUse(ItemStack stack){
        //TODO place the dust
        return true;
    }
}
