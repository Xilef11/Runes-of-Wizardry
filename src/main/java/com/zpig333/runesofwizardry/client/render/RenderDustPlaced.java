/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-25
 */
package com.zpig333.runesofwizardry.client.render;

import java.awt.Color;
import java.util.Set;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

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
			GL11.glEnable(GL11.GL_BLEND);//we want transparency blending(?)
			GL11.glDisable(GL11.GL_CULL_FACE);//visible from both faces (might solve being invisible)
			GL11.glDepthMask(false);//hidden behind other objects
			//GlStateManager.color(1, 0, 0);
			
			//WorldRenderer.startDrawing(GL11.GL_QUADS);//our thing is quads (? the example uses triangles)
			
			//drawing logic here
			//XXX testing
			//drawCenterVertexNoUV(0, 0, 0xff0000, worldrenderer, tesselator);
			//drawCenterVertexNoUV(0, 1, 0x0000ff, worldrenderer,tesselator);
			/* a square is
			 * 0 y 0 
			 * 0 y 1
			 * 1 y 1
			 * 1 y 0
			 */
//			GlStateManager.color(1,0,0);
//			worldrenderer.startDrawingQuads();
//			worldrenderer.addVertexWithUV(0, 0.1, 0, 0, 0);
//			worldrenderer.addVertexWithUV(0, 0.1, 1, 0, 1);
//			worldrenderer.addVertexWithUV(1, 0.1, 1, 1, 1);
//			worldrenderer.addVertexWithUV(1, 0.1, 0, 1, 0);
//			tesselator.draw();
//			drawCenterVertexWithUV(0, 0, 0xff0000, worldrenderer, tesselator);
			
			int[][] colors = teDust.getCenterColors();
			for(int i=0;i<colors.length;i++){
				for(int j=0;j<colors[i].length;j++){
					if(colors[i][j]>=0){//negative colors indicate no rendering
						drawCenterVertexWithUV(i, j, colors[i][j], worldrenderer, tesselator);
					}
				}
			}
			//drawInternalConnector(0, 0, 0, 1, 0xff0000, 0x0, worldrenderer, tesselator);
			//drawInternalConnector(0, 0, 1, 0, 0xff0000, 0x0, worldrenderer, tesselator);
			Set<int[]> connectors = teDust.getInternalConnectors();
			for(int[] i : connectors){
				drawInternalConnector(i[0], i[1], i[2], i[3], i[4], i[5], worldrenderer, tesselator);
			}
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
	private static final double offset = 0.058;
	final double y = 0.01;//y coordinate at which to draw the things
	private void drawCenterVertexWithUV(int row, int col, int colorInt, WorldRenderer renderer, Tessellator tes){
		Color color = new Color(colorInt);
		GlStateManager.color(((float)color.getRed())/255F,((float)color.getGreen())/255F,((float)color.getBlue())/255F);
		renderer.startDrawing(GL11.GL_QUADS);//our thing is quads (? the example uses triangles)
		double rowBegin = row/4F + offset;
		double rowEnd = (row+1)/4F - offset;
		double colBegin = col/4F + offset;
		double colEnd = (col+1)/4F - offset;
		final double[][] vertexTable = {
				{colBegin,y,rowBegin, colBegin, rowBegin}, //numbers are X Y Z U V
				{colBegin,y,rowEnd, colBegin, rowEnd},
				{colEnd,y,rowEnd, colEnd, rowEnd},
				{colEnd,y,rowBegin, colEnd, rowBegin}
			};
		
		for(double[] vertex:vertexTable){
			renderer.addVertexWithUV(vertex[0],vertex[1],vertex[2],vertex[3],vertex[4]);
		}
		tes.draw();
	}
	
	private void drawInternalConnector(int row1, int col1, int row2, int col2, int color1, int color2, WorldRenderer renderer, Tessellator tes){
		double thin = 0.02;
		double row1begin = row1/4F + offset,
			   row1end = (row1+1)/4F - offset,
			   row2begin = row2/4F + offset,
			   //row2end = (row2+1)/4F - offset,
			   col1begin = col1/4F + offset,
			   col1end = (col1+1)/4F - offset,
			   col2begin = col2/4F + offset;
			   //col2end = (col2+1)/4F - offset;
		double[][]vertexTable1=null,vertexTable2=null;
		double middle;
		if(row1==row2){//horizontal connector
			middle = col1end + ((col2begin - col1end)/2);
			vertexTable1 = new double[][]{
					{col1end,y,row1begin+thin,col1end,row1begin+thin},
					{middle,y,row1begin+thin, middle,row1begin+thin},
					{middle,y,row1end-thin,middle,row1end-thin},
					{col1end,y,row1end-thin,col1end,row1end-thin}
			};
			vertexTable2 = new double[][]{
					{middle,y,row1begin+thin,middle,row1begin+thin},
					{col2begin,y,row1begin+thin,col2begin,row1begin+thin},
					{col2begin,y,row1end-thin,col2begin,row1end-thin},
					{middle,y,row1end-thin,middle,row1end-thin}
			};
			
		}else if(col1==col2){
			middle = row1end + ((row2begin - row1end)/2);
			vertexTable1 = new double[][]{
					{col1end-thin,y,row1end,col1end-thin,row1end},
					{col1end-thin,y,middle, col1end-thin,middle},
					{col1begin+thin,y,middle,col1begin+thin,middle},
					{col1begin+thin,y,row1end,col1begin+thin,row1end}
			};
			vertexTable2 = new double[][]{
					{col1end-thin,y,middle,col1end-thin,middle},
					{col1end-thin,y,row2begin,col1end-thin,row2begin},
					{col1begin+thin,y,row2begin,col1begin+thin,row2begin},
					{col1begin+thin,y,middle,col1begin+thin,middle}
			};
		}else{
			throw new IllegalArgumentException("Invalid internal connector");
		}
		//safety
		if(vertexTable1==null ||vertexTable2==null)return;
		
		Color color = new Color(color1);
		GlStateManager.color(((float)color.getRed())/255F,((float)color.getGreen())/255F,((float)color.getBlue())/255F);
		renderer.startDrawingQuads();
		for(double vertex[]:vertexTable1){
			renderer.addVertexWithUV(vertex[0],vertex[1],vertex[2],vertex[3],vertex[4]);
		}
		tes.draw();
		
		color=new Color(color2);
		GlStateManager.color(((float)color.getRed())/255F,((float)color.getGreen())/255F,((float)color.getBlue())/255F);
		renderer.startDrawingQuads();
		for(double vertex[]:vertexTable2){
			renderer.addVertexWithUV(vertex[0],vertex[1],vertex[2],vertex[3],vertex[4]);
		}
		tes.draw();
	}

}
