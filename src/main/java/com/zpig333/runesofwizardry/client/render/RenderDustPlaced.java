/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-25
 */
package com.zpig333.runesofwizardry.client.render;

import java.awt.Color;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

/** Renders the placed dust
 * @author Xilef11
 */
public class RenderDustPlaced extends TileEntitySpecialRenderer<TileEntityDustPlaced> {

	private static final ResourceLocation dustTexture = new ResourceLocation(References.texture_path + "textures/blocks/dust_top.png");
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double, double, float, int)
	 * see http://github.com/TheGreyGhost/MinecraftByExample/ MBE21 TESR
	 */ 
	@Override
	public void renderTileEntityAt(final TileEntityDustPlaced tileEntity, double relativeX,
			double relativeY, double relativeZ, float partialTicks, int blockDamageProgress) {

		try{
			//save GL state
			GL11.glPushMatrix();
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

			//move the reference point from the player to the position of the block
			GlStateManager.translate(relativeX, relativeY, relativeZ);
			//grab the tesselator
			final Tessellator tesselator = Tessellator.getInstance();
			final WorldRenderer worldrenderer = tesselator.getWorldRenderer();
			//set texture
			this.bindTexture(dustTexture);

			//setup flags
			GL11.glDisable(GL11.GL_LIGHTING);//we want lighting (?)
			GL11.glEnable(GL11.GL_BLEND);//we want transparency blending(?)
			GL11.glDisable(GL11.GL_CULL_FACE);//visible from both faces (might solve being invisible)
			GL11.glDepthMask(false);//hidden behind other objects
			//drawing logic wrapped in a runnable
			Runnable draw = new Runnable(){

				@Override
				public void run() {
					//drawing logic here
					//the central spots
					int[][] colors = tileEntity.getCenterColors();
					for(int i=0;i<colors.length;i++){
						for(int j=0;j<colors[i].length;j++){
							if(colors[i][j]>=0){//negative colors indicate no rendering
								drawCenterVertex(i, j, colors[i][j], worldrenderer, tesselator);
							}
						}
					}
					///the internal connectors
					Set<int[]> connectors = tileEntity.getInternalConnectors();
					for(int[] i : connectors){
						drawInternalConnector(i[0], i[1], i[2], i[3], i[4], i[5], worldrenderer, tesselator);
					}
					//external connectors
					for(int[]i:tileEntity.getExternalConnectors()){
						drawExternalConnector(i[0], i[1], i[2], EnumFacing.getFront(i[3]), worldrenderer, tesselator);
					}
				}

			};
			draw.run();
			//draw FX if its an active rune
			if(tileEntity.isInRune()&&tileEntity.getRune().renderActive){
				//Note: lowering the scale "slows down" the animation
				runRendererWithGlint(draw, 1.5F);
			}
		}finally{//restore GL stuff
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		}

	}
	private static final double offset = 0.058;
	final double y = 0.01;//y coordinate at which to draw the things
	private void drawCenterVertex(int row, int col, int colorInt, WorldRenderer renderer, Tessellator tes){
		Color color = new Color(colorInt);
		double rowBegin = row/(float)TileEntityDustPlaced.ROWS + offset;
		double rowEnd = (row+1)/(float)TileEntityDustPlaced.ROWS - offset;
		double colBegin = col/(float)TileEntityDustPlaced.COLS + offset;
		double colEnd = (col+1)/(float)TileEntityDustPlaced.COLS - offset;
		final double[][] vertexTable = {
				{colBegin,y,rowBegin}, //numbers are X Y Z
				{colBegin,y,rowEnd},
				{colEnd,y,rowEnd},
				{colEnd,y,rowBegin}
		};

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for(double[] vertex:vertexTable){
			renderer.pos(vertex[0], vertex[1], vertex[2]).tex(vertex[0], vertex[2]).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
		}
		tes.draw();
	}
	private final static double thin = 0.02;
	private void drawInternalConnector(int row1, int col1, int row2, int col2, int color1, int color2, WorldRenderer renderer, Tessellator tes){

		double row1begin = row1/(float)TileEntityDustPlaced.ROWS + offset,
				row1end = (row1+1)/(float)TileEntityDustPlaced.ROWS - offset,
				row2begin = row2/(float)TileEntityDustPlaced.ROWS + offset,
				//row2end = (row2+1)/(float)TileEntityDustPlaced.ROWS - offset,
				col1begin = col1/(float)TileEntityDustPlaced.COLS + offset,
				col1end = (col1+1)/(float)TileEntityDustPlaced.COLS - offset,
				col2begin = col2/(float)TileEntityDustPlaced.COLS + offset;
		//col2end = (col2+1)/(float)TileEntityDustPlaced.COLS - offset;
		double[][]vertexTable1=null,vertexTable2=null;
		double middle;
		if(row1==row2){//horizontal connector
			middle = col1end + ((col2begin - col1end)/2);
			vertexTable1 = new double[][]{
					{col1end,y,row1begin+thin},
					{middle,y,row1begin+thin},
					{middle,y,row1end-thin},
					{col1end,y,row1end-thin}
			};
			vertexTable2 = new double[][]{
					{middle,y,row1begin+thin},
					{col2begin,y,row1begin+thin},
					{col2begin,y,row1end-thin},
					{middle,y,row1end-thin}
			};

		}else if(col1==col2){
			middle = row1end + ((row2begin - row1end)/2);
			vertexTable1 = new double[][]{
					{col1end-thin,y,row1end},
					{col1end-thin,y,middle},
					{col1begin+thin,y,middle},
					{col1begin+thin,y,row1end}
			};
			vertexTable2 = new double[][]{
					{col1end-thin,y,middle},
					{col1end-thin,y,row2begin},
					{col1begin+thin,y,row2begin},
					{col1begin+thin,y,middle}
			};
		}else{
			throw new IllegalArgumentException("Invalid internal connector");
		}
		//safety
		if(vertexTable1==null ||vertexTable2==null)return;

		Color color = new Color(color1);
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for(double vertex[]:vertexTable1){
			renderer.pos(vertex[0], vertex[1], vertex[2]).tex(vertex[0], vertex[2]).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
		}
		tes.draw();

		color=new Color(color2);
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for(double vertex[]:vertexTable2){
			renderer.pos(vertex[0], vertex[1], vertex[2]).tex(vertex[0], vertex[2]).color(color.getRed(), color.getGreen(), color.getBlue(), 255).endVertex();
		}
		tes.draw();
	}
	private void drawExternalConnector(int row, int col, int colorIn, EnumFacing direction, WorldRenderer renderer, Tessellator tes){
		double rowBegin = row/(float)TileEntityDustPlaced.ROWS + offset;
		double rowEnd = (row+1)/(float)TileEntityDustPlaced.ROWS - offset;
		double colBegin = col/(float)TileEntityDustPlaced.COLS + offset;
		double colEnd = (col+1)/(float)TileEntityDustPlaced.COLS - offset;
		double[][] vertexTable = null;
		if(direction == EnumFacing.NORTH && row==0){//top row
			vertexTable = new double[][]{
					{colBegin+thin,y,rowBegin},
					{colBegin+thin,y,0},
					{colEnd-thin,y,0},
					{colEnd-thin,y,rowBegin}
			};
		}
		if(direction==EnumFacing.SOUTH && row==TileEntityDustPlaced.ROWS-1){//bottom row
			vertexTable = new double[][]{
					{colBegin+thin,y,rowEnd},
					{colBegin+thin,y,1},
					{colEnd-thin,y,1},
					{colEnd-thin,y,rowEnd}
			};
		}
		if(direction==EnumFacing.WEST && col==0){//left (west) column
			vertexTable = new double[][]{
					{colBegin,y,rowBegin+thin},
					{0,y,rowBegin+thin},
					{0,y,rowEnd-thin},
					{colBegin,y,rowEnd-thin}
			};
		}
		if(direction==EnumFacing.EAST && col==TileEntityDustPlaced.COLS-1){//right (EAST) column
			vertexTable = new double[][]{
					{colEnd,y,rowBegin+thin},
					{1,y,rowBegin+thin},
					{1,y,rowEnd-thin},
					{colEnd,y,rowEnd-thin}
			};
		}
		if(vertexTable==null)return;//should not happen
		Color colour = new Color(colorIn);
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for(double vertex[]:vertexTable){
			renderer.pos(vertex[0], vertex[1], vertex[2]).tex(vertex[0], vertex[2]).color(colour.getRed(), colour.getGreen(), colour.getBlue(), 255).endVertex();
		}
		tes.draw();
	}

	public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	/**
	 * Runs the given renderer wrapped in a Runnable as a renderer for the
	 * enchantment glint effect. This only works if the current texture is not
	 * changed during execution of the runnable. Also, the colour should not
	 * be changed during rendering, or it will lead to some weird results.
	 * Basically, the only things that should be done are Matrix transformations
	 * and rendering calls of vertices.
	 * <p>
	 * If some of the settings are changed during the rendering call or if the
	 * texture scale is wrong, this can lead to pretty weird results. If you use
	 * this method you need to play around with the scale until you find a good
	 * value.
	 * <p>
	 * Usage:
	 *
	 * <pre>
	 * anyRenderer(x, y, z);
	 * runRendererWithGlint(new Runnable() {
	 *     public void run() {
	 *         anyRenderer(x, y, z);
	 *     }
	 * });
	 * </pre>
	 * <p>
	 * <strong>
	 * <em>The WorldRenderer needs to be off when this method is called!</em>
	 * </strong><br>
	 * <em>This discards the current texture</em>
	 *
	 * @param renderer
	 * the renderer to run as a glint renderer.
	 * @param textureScale
	 * the scale of the texture. This sets the scale of the enchantment glint
	 * texture parts. Some renderers need a value here in order to
	 * display properly. You need to experiment a bit in order to find a good
	 * value. For vanilla models, the standard value is 8.
	 */
	//http://www.minecraftforge.net/forum/index.php/topic,29902.0.html
	public void runRendererWithGlint(Runnable renderer, float textureScale) {
		GlStateManager.depthMask(false);
		//GlStateManager.depthFunc(GL11.GL_EQUAL);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
		//enableBlend(true);
		GL11.glEnable(GL11.GL_BLEND);
		//bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
		this.bindTexture(RES_ITEM_GLINT);

		GL11.glPushMatrix(); // Push MODEL
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix(); // Push TEXTURE
		GL11.glScalef(textureScale, textureScale, textureScale);
		float f = Minecraft.getSystemTime() % 3000L / 3000.0F / textureScale;
		GL11.glTranslatef(f, 0.0F, 0.0F);
		GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		//setColor(new Color(0.5, 0.2, 0.8));
		GL11.glColor3d(0.5, 0.2, 0.8);
		renderer.run();
		GL11.glPopMatrix(); // Pop MODEL

		GL11.glPushMatrix(); // Push MODEL
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GL11.glPopMatrix(); // Pop TEXTURE
		GL11.glPushMatrix(); // Push TEXTURE
		GL11.glScalef(textureScale, textureScale, textureScale);
		float f1 = Minecraft.getSystemTime() % 4873L / 4873.0F / textureScale;
		GL11.glTranslatef(-f1, 0.0F, 0.0F);
		GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		//setColor(new Color(0.5, 0.2, 0.8));
		GL11.glColor3d(0.5, 0.2, 0.8);
		renderer.run();
		GL11.glPopMatrix(); // Pop MODEL
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GL11.glPopMatrix(); // Pop TEXTURE

		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.depthMask(true);
	}
}
