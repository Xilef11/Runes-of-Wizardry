package com.zpig333.runesofwizardry.api;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import com.zpig333.runesofwizardry.client.render.DustStorageRenderer;
import com.zpig333.runesofwizardry.core.References;

import net.minecraft.block.Block;

public abstract class IDustStorageBlock extends Block {
    //FIXME changed from BlockFalling to Block because of rendering bug
    //Icons for the background and foreground
    private static IIcon bgIcon=null,
    					 fgIcon=null;
    public IDustStorageBlock(Material mat){
        super(mat);
    }
    /** returns the dust that forms this block **/
    public abstract IDust getIDust();
    @Override
    public int damageDropped(int i) {
        return i;
    }
    public IIcon getBackgroundIcon(){
        return bgIcon;
    }
    public IIcon getForegroundIcon(){
        return fgIcon;
    }
    //TODO finish setting up the block
    //TODO Icons and stuff

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        //sand icon, used only for the particles when breaking block
        blockIcon = Blocks.sand.getIcon(0, 0);
        //if(bgIcon==null){
            bgIcon = reg.registerIcon(References.texture_path+"dustStorage_bg");
        //}
        //if(fgIcon==null){
        	fgIcon = reg.registerIcon(References.texture_path+"dustStorage_fg");
        //}
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return DustStorageRenderer.getRenderID();
    }

}
