/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-25
 */
package com.zpig333.runesofwizardry.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/** Renders the placed dust
 * @author Xilef11
 *
 */
public class RenderDustPlaced extends TileEntitySpecialRenderer {
	
	private static final ResourceLocation dustTexture = new ResourceLocation(References.texture_path + "textures/blocks/dust_top.png");
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double, double, float, int)
	 * see http://github.com/TheGreyGhost/MinecraftByExample/ MBE21 TESR
	 */ 
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double relativeX,
			double relativeY, double relativeZ, float partialTicks, int blockDamageProgress) {
		
		if (!(tileEntity instanceof TileEntityDustPlaced)) return;//should not happen
		TileEntityDustPlaced teDust = (TileEntityDustPlaced)tileEntity;
		
		// TODO placed dust rendering
		try{
			//save GL state
			GL11.glPushMatrix();
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
			
			//move the reference point from the player to the position of the block
			GlStateManager.translate(relativeX, relativeY, relativeZ);
			//grab the tesselator
			Tessellator tesselator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tesselator.getWorldRenderer();
			
			//set texture (might not be needed?)
			this.bindTexture(dustTexture);
			
			//setup flags
			GL11.glDisable(GL11.GL_LIGHTING);//we want lighting (?)
			GL11.glDisable(GL11.GL_BLEND);//we want transparency blending(?)
			GL11.glDisable(GL11.GL_CULL_FACE);//visible from both faces (might solve being invisible)
			GL11.glDepthMask(false);//hidden behind other objects
			//FIXME STILL INVISIBLE (when scrolling through items)
			//GlStateManager.color(1, 0, 0);
			
			//WorldRenderer.startDrawing(GL11.GL_QUADS);//our thing is quads (? the example uses triangles)
			
			//drawing logic here
			//XXX testing
			drawCenterVertexNoUV(0, 0, 0xff0000, worldrenderer, tesselator);
			drawCenterVertexNoUV(0, 1, 0x0000ff, worldrenderer,tesselator);
			//FIXME following does not work FSR
//			int[][] colors = teDust.getCenterColors();
//			for(int i=0;i<colors.length;i++){
//				for(int j=0;j<colors[i].length;j++){
//					if(colors[i][j]>0){//negative colors indicate no rendering
//						drawCenterVertexNoUV(i, j, colors[i][j], worldrenderer, tesselator);
//					}
//				}
//			}
			
			//addCenterVertex(0, 1, 0x0000ff, worldrenderer);
			
//			worldrenderer.addVertex(0, 0.1, 0);
//			worldrenderer.addVertex(0, 0.1, 1);
//			worldrenderer.addVertex(1, 0.1, 1);
//			worldrenderer.addVertex(1, 0.1, 0);
			//actually draw the stuff
			//tesselator.draw();
			
		}finally{//restore GL stuff
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		}
		
	}
	
	private void addCenterVertex(int row, int col, int colorInt, WorldRenderer renderer){
		Color color = new Color(colorInt);
		GlStateManager.color(((float)color.getRed())/255F,((float)color.getGreen())/255F,((float)color.getBlue())/255F);
		final double offset=0.0;
		final double[][] vertexTable = {
				{0+offset,0.1,0-offset,0,0}, //numbers are X Y Z U V
				{0,0,0,1,0},
				{0,0,1,1,0},
				{1,1,1,1,0}
			};
		
		for(double[] vertex:vertexTable){
			renderer.addVertexWithUV(vertex[0], vertex[1], vertex[2], vertex[3], vertex[4]);
		}
		
	}
	
	private void drawCenterVertexNoUV(int row, int col, int colorInt, WorldRenderer renderer, Tessellator tes){
		Color color = new Color(colorInt);
		GlStateManager.color(((float)color.getRed())/255F,((float)color.getGreen())/255F,((float)color.getBlue())/255F);
		renderer.startDrawing(GL11.GL_QUADS);//our thing is quads (? the example uses triangles)
		final double offset=0.05;
		final double y = 0.01;//y coordinate at which to draw the things
		float rowBegin = row/4F;
		float rowEnd = (row+1)/4F;
		float colBegin = col/4F;
		float colEnd = (col+1)/4F;
		final double[][] vertexTable = {
				{colBegin+offset,y,rowBegin+offset}, //numbers are X Y Z
				{colBegin+offset,y,rowEnd-offset},
				{colEnd-offset,y,rowEnd-offset},
				{colEnd-offset,y,rowBegin + offset}
			};
		
		for(double[] vertex:vertexTable){
			renderer.addVertex(vertex[0], vertex[1], vertex[2]);
		}
		tes.draw();
	}

}
