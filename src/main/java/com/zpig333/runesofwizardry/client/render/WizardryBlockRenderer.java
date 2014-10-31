package com.zpig333.runesofwizardry.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class WizardryBlockRenderer implements ISimpleBlockRenderingHandler {

    public static int modelid_dust;
    public int current_renderer;

    public WizardryBlockRenderer(int modelID){
        this.current_renderer = modelID;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        //Don't need inventory render...
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        //Which block should the blockrenderer be rendering?
        if(modelId == current_renderer){
            renderDust(world, x, y, z, block, renderer);
        }

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    public boolean renderDust(IBlockAccess iBlockAccess, int x, int y, int z, Block block, RenderBlocks renderBlocks){

        return true;
    }
}
