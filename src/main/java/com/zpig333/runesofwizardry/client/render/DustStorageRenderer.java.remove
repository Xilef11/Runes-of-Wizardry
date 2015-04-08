package com.zpig333.runesofwizardry.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;


public class DustStorageRenderer implements ISimpleBlockRenderingHandler{
    private static int dustStorageRenderID;
    //following a "singleton" pattern since I don't like having a public constructor changing a static variable (could mess stuff up)
    private static DustStorageRenderer instance = null;
    private DustStorageRenderer(){
        dustStorageRenderID = RenderingRegistry.getNextAvailableRenderId();
    }
    public static int getRenderID(){
        return dustStorageRenderID;
    }
    public static DustStorageRenderer getInstance(){
        if(instance==null){
            instance=new DustStorageRenderer();
        }
        return instance;
    }
    @Override
    public void renderInventoryBlock(Block ablock, int metadata, int modelId, RenderBlocks renderer) {
        if (ablock instanceof IDustStorageBlock) {
            IDustStorageBlock block = (IDustStorageBlock) ablock;

            //thanks to TheGreyGhost for this https://github.com/TheGreyGhost/ItemRendering/blob/master/src/TestItemRendering/blocks/BlockPyramidRenderer.java
            Tessellator tes = Tessellator.instance;
            // if you don't perform this translation, the item won't sit in the player's hand properly in 3rd person view
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            // for "inventory" blocks (actually for items which are equipped, dropped, or in inventory), should render in [0,0,0] to [1,1,1]
            tes.startDrawingQuads();
            renderDustBlock(tes, 0.0, 0.0, 0.0, metadata, block,null);
            tes.draw();
            // don't forget to undo the translation you made at the start
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block ablock, int modelId, RenderBlocks renderer) {
        if (ablock instanceof IDustStorageBlock) {
            IDustStorageBlock block = (IDustStorageBlock) ablock;
            Tessellator tessellator = Tessellator.instance;
            // world blocks should render in [x,y,z] to [x+1, y+1, z+1]
            // tessellator.startDrawingQuads() has already been called by the caller
            //int lightValue = block.getMixedBrightnessForBlock(world, x, y, z);
            //tessellator.setBrightness(lightValue);
            //tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            renderDustBlock(tessellator, (double)x, (double)y, (double) z, world.getBlockMetadata(x, y, z), block, world);
            // tessellator.draw() will be called by the caller after return
            return true;
        }
        return false;
    }
    

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return dustStorageRenderID;
    }

    private void renderDustBlock(Tessellator tessellator, double x, double y, double z, int meta, IDustStorageBlock block, IBlockAccess world) {
        //TODO most of the work: renderDustBlock
        IDust dust = block.getIDust();
        ItemStack dustStack = new ItemStack(dust,1,meta);
        IIcon bgIcon = block.getBackgroundIcon();
        IIcon fgIcon = block.getForegroundIcon();
        int primaryColor = dust.getPrimaryColor(dustStack);
        int secondaryColor = dust.getSecondaryColor(dustStack);
        //bgIcon = Blocks.sand.getIcon(0, 0);
        //fgIcon = Blocks.bedrock.getIcon(0, 0);
        double minUBG = (double) bgIcon.getMinU();
        double minVBG = (double) bgIcon.getMinV();
        double maxUBG = (double) bgIcon.getMaxU();
        double maxVBG = (double) bgIcon.getMaxV();
        //foreground texture
        double minUFG = (double) fgIcon.getMinU();
        double minVFG = (double) fgIcon.getMinV();
        double maxUFG = (double) fgIcon.getMaxU();
        double maxVFG = (double) fgIcon.getMaxV();
        //NOrmals: X+ = east, Y+ =up, z+ = south 
        // east face
        //background color
        tessellator.draw();//flush
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x+1), (int)Math.rint(y), (int)Math.rint(z)));
        tessellator.setNormal(1F, 0F, 0.0F);
        tessellator.setColorRGBA_I(primaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 1.0, y + 0.0, z + 0, maxUBG, maxVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 1.0, z + 0, maxUBG, minVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 1.0, z + 1, minUBG, minVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 0, z + 1, minUBG, maxVBG);
        tessellator.draw();
        
        //fg color
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x+1), (int)Math.rint(y), (int)Math.rint(z)));
        tessellator.setNormal(1F, 0F, 0.0F);
        tessellator.setColorRGBA_I(secondaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 1.001, y + 0.0, z + 0, maxUFG, maxVFG);
        tessellator.addVertexWithUV(x + 1.001, y + 1.0, z + 0, maxUFG, minVFG);
        tessellator.addVertexWithUV(x + 1.001, y + 1.0, z + 1, minUFG, minVFG);
        tessellator.addVertexWithUV(x + 1.001, y + 0, z + 1, minUFG, maxVFG);
        tessellator.draw();
        
        // west face
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x-1), (int)Math.rint(y), (int)Math.rint(z)));
        tessellator.setNormal(-1, 0, 0.0F);
        tessellator.setColorRGBA_I(primaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 0.0, y + 0.0, z + 1.0, maxUBG, maxVBG);
        tessellator.addVertexWithUV(x + 0.0, y + 1.0, z + 1.0, maxUBG, minVBG);
        tessellator.addVertexWithUV(x + 0.0, y + 1.0, z + 0, minUBG, minVBG);
        tessellator.addVertexWithUV(x + 0.0, y + 0.0, z + 0.0, minUBG, maxVBG);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x-1), (int)Math.rint(y), (int)Math.rint(z)));
        tessellator.setNormal(-1, 0, 0.0F);
        tessellator.setColorRGBA_I(secondaryColor, 0xFF);
        tessellator.addVertexWithUV(x - 0.001, y + 0.0, z + 1.0, maxUFG, maxVFG);
        tessellator.addVertexWithUV(x - 0.001, y + 1.0, z + 1.0, maxUFG, minVFG);
        tessellator.addVertexWithUV(x - 0.001, y + 1.0, z + 0, minUFG, minVFG);
        tessellator.addVertexWithUV(x - 0.001, y + 0.0, z + 0.0, minUFG, maxVFG);
        
        tessellator.draw();
        // north face
        //NOrmals: X+ = east, Y+ =up, z+ = south 
        //background color
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y), (int)Math.rint(z-1)));
        tessellator.setNormal(0, 0F, -1);
        tessellator.setColorRGBA_I(primaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 0, y + 0.0, z + 0.0, maxUBG, maxVBG);
        tessellator.addVertexWithUV(x + 0, y + 1, z + 0.0, maxUBG, minVBG);
        tessellator.addVertexWithUV(x + 1, y + 1.0, z + 0, minUBG, minVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 0.0, z + 0, minUBG, maxVBG);
        tessellator.draw();
        
        //fg color
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y), (int)Math.rint(z-1)));
        tessellator.setNormal(0, 0F, -1);
        tessellator.setColorRGBA_I(secondaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 0, y + 0.0, z - 0.001, maxUFG, maxVFG);
        tessellator.addVertexWithUV(x + 0, y + 1, z - 0.001, maxUFG, minVFG);
        tessellator.addVertexWithUV(x + 1, y + 1.0, z - 0.001, minUFG, minVFG);
        tessellator.addVertexWithUV(x + 1.0, y + 0.0, z - 0.001, minUFG, maxVFG);
        
        tessellator.draw();
        // south face
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y), (int)Math.rint(z+1)));
        //NOrmals: X+ = east, Y+ =up, z+ = south 
        //background color
        tessellator.setNormal(0, 0F, 1);
        tessellator.setColorRGBA_I(dust.getPrimaryColor(dustStack), 0xFF);
        tessellator.addVertexWithUV(x + 1.0, y + 0.0, z + 1, maxUBG, maxVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 1, z + 1, maxUBG, minVBG);
        tessellator.addVertexWithUV(x + 0, y + 1.0, z + 1, minUBG, minVBG);
        tessellator.addVertexWithUV(x + 0, y + 0.0, z + 1.0, minUBG, maxVBG);
        tessellator.draw();
        //fg color
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y), (int)Math.rint(z+1)));
        tessellator.setNormal(0, 0F, 1);
        tessellator.setColorRGBA_I(secondaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 1.0, y + 0.0, z + 1.001, maxUFG, maxVFG);
        tessellator.addVertexWithUV(x + 1.0, y + 1, z + 1.001, maxUFG, minVFG);
        tessellator.addVertexWithUV(x + 0, y + 1.0, z + 1.001, minUFG, minVFG);
        tessellator.addVertexWithUV(x + 0, y + 0.0, z + 1.001, minUFG, maxVFG);
        
        tessellator.draw();
        // bottom face
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y-1), (int)Math.rint(z)));
        //NOrmals: X+ = east, Y+ =up, z+ = south 
        //background color
        tessellator.setNormal(0, -1F, 0.0F);
        tessellator.setColorRGBA_I(primaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 1, y + 0.0, z + 0, maxUBG, maxVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 0.0, z + 1, maxUBG, minVBG);
        tessellator.addVertexWithUV(x + 0, y + 0, z + 1, minUBG, minVBG);
        tessellator.addVertexWithUV(x + 0, y + 0.0, z + 0, minUBG, maxVBG);
        tessellator.draw();
        //fg color
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y-1), (int)Math.rint(z)));
        tessellator.setNormal(0, -1F, 0.0F);
        tessellator.setColorRGBA_I(dust.getSecondaryColor(dustStack), 0xFF);
        tessellator.addVertexWithUV(x + 1, y - 0.001, z + 0, maxUFG, maxVFG);
        tessellator.addVertexWithUV(x + 1.0, y - 0.001, z + 1, maxUFG, minVFG);
        tessellator.addVertexWithUV(x + 0, y - 0.001, z + 1, minUFG, minVFG);
        tessellator.addVertexWithUV(x + 0, y - 0.001, z + 0, minUFG, maxVFG);
        
        tessellator.draw();
        // UP face
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y+1), (int)Math.rint(z)));
        //NOrmals: X+ = east, Y+ =up, z+ = south 
        //background color
        tessellator.setNormal(0, 1F, 0.0F);
        tessellator.setColorRGBA_I(primaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 1.0, y + 1, z + 1, maxUBG, maxVBG);
        tessellator.addVertexWithUV(x + 1.0, y + 1, z + 0.0, maxUBG, minVBG);
        tessellator.addVertexWithUV(x + 0, y + 1.0, z + 0, minUBG, minVBG);
        tessellator.addVertexWithUV(x + 0, y +1, z + 1.0, minUBG, maxVBG);
        tessellator.draw();
        //fg color
        tessellator.startDrawingQuads();
        if(world!=null)tessellator.setBrightness(block.getMixedBrightnessForBlock(world, (int)Math.rint(x), (int)Math.rint(y+1), (int)Math.rint(z)));
        tessellator.setNormal(0, 1F, 0.0F);
        tessellator.setColorRGBA_I(secondaryColor, 0xFF);
        tessellator.addVertexWithUV(x + 1.0, y + 1.001, z + 1, maxUFG, maxVFG);
        tessellator.addVertexWithUV(x + 1.0, y + 1.001, z + 0.0, maxUFG, minVFG);
        tessellator.addVertexWithUV(x + 0, y + 1.001, z + 0, minUFG, minVFG);
        tessellator.addVertexWithUV(x + 0, y +1.001, z + 1.0, minUFG, maxVFG);
        tessellator.draw();
        tessellator.startDrawingQuads();//restart the tessellator since it's what the callers expect
    }
}
