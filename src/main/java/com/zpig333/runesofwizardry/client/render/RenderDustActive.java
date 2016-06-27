package com.zpig333.runesofwizardry.client.render;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamType;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

public class RenderDustActive extends RenderDustPlaced {

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.client.render.RenderDustPlaced#renderTileEntityAt(com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced, double, double, double, float, int)
	 */
	@Override
	public void renderTileEntityAt(TileEntityDustPlaced tileEntity,	double relativeX, double relativeY, double relativeZ,float partialTicks, int blockDamageProgress) {
		super.renderTileEntityAt(tileEntity, relativeX, relativeY, relativeZ,partialTicks, blockDamageProgress);
		if(!(tileEntity instanceof TileEntityDustActive)){
			WizardryLogger.logError("TileEntity was not active dust for rendering by RenderDustActive");
			return;
		}
		//save GL state
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

		//move the reference point from the player to the position of the block
		GlStateManager.translate(relativeX, relativeY, relativeZ);
		
		//TODO render star and beam if necessary
		TileEntityDustActive te = (TileEntityDustActive)tileEntity;
		//XXX if(te.stardata!=null && te.stardata.doRender)renderStar(te);
		if(te.beamdata!=null){
			if(te.beamdata.type == BeamType.BEACON){
				drawBeaconBeam(te,partialTicks);
			}else if(te.beamdata.type==BeamType.SPIRAL){
				drawSpiralBeam(te);
			}
		}
		
		
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	private static final int birthLength = 20*5;
	private void renderStar(TileEntityDustActive te)
    {
		
        Tessellator tes = Tessellator.getInstance();
        VertexBuffer vb = tes.getBuffer();
        Vec3i off = te.stardata.offset;
        //GlStateManager.translate(off.getX(), off.getY(), off.getZ());
        //FIXME the star is not above the rune
        RenderHelper.disableStandardItemLighting();
        int ticks = (int) (te.ticksExisted() % 200);

        if (ticks >= 100)
        {
            ticks = 200 - ticks - 1;
        }

        float f1 = ((float)ticks) / 200F;
        float f2 = 0.0F;

        if (f1 > 0.7F)
        {
            f2 = (f1 - 0.7F) / 0.2F;
        }
        
        float yOffset = 0;
        float scale = te.stardata.scale;
        if(te.ticksExisted() < birthLength){
        	double offset = 0.5; 
        	double offsetPerc = offset/(1-0.1875);
        	double perc = ((double)ticks / (double) birthLength);
        	scale *= Math.min(perc+0.2,1);
//        	System.out.println(offset + " " + perc);
        	yOffset = (float)perc*(float)offsetPerc-(float)offset;
        }        

        Random random = new Random(432L);
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -1F + yOffset, -2F);
        GL11.glScalef(0.02F, 0.02F, 0.02F);
        GL11.glScalef(scale,scale,scale);
        GL11.glScalef(1F, te.stardata.yscale, 1F);
        for (int i = 0; (float)i < ((f1 + f1 * f1) / 2F) * 90F + 30F; i++)
        {
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
            //tes.startDrawing(6);
            vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
            //tessellator.setColorRGBA_I(0xffffFF, (int)(255F * (1.0F - f2)));
           // tes.setColorRGBA_F(ri, gi, bi, (int)(255F * (1.0F - f2)));
           //tes.addVertex(0.0D, 0.0D, 0.0D);
            Color col = new Color(te.stardata.innercolor);
            vb.pos(0, 0, 0).color(col.getRed(), col.getGreen(), col.getBlue(), (int)(255F * (1.0F - f2))).endVertex();
//            tessellator.setColorRGBA_I(0x0000FF, 0);
            //tes.setColorRGBA_F(ro, go, bo, 0);
            col = new Color(te.stardata.outercolor);
            //tes.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
            //tes.addVertex(0.86599999999999999D * (double)f4, f3, -0.5F * f4);
            //tes.addVertex(0.0D, f3, 1.0F * f4);
            //tes.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
            vb.pos(-0.86599999999999999D * (double)f4, f3, -0.5F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
            vb.pos(0.86599999999999999D * (double)f4, f3, -0.5F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
            vb.pos(0, f3, 1.0F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
            vb.pos(-0.86599999999999999D * (double)f4, f3, -0.5F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 255).endVertex();
            tes.draw();
        }

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
        RenderHelper.enableStandardItemLighting();
    }

//
//	//TODO this beam stuff was probably copied from the beacon. should be updated with the new stuff
//	//public void renderBeam1(TileEntityDustActive te, double x, double y, double z, double x2, double j2, double z2, float f)
//	public void renderBeam1(TileEntityDustActive te)
//    {
//		double j=256;
//		double x = te.beamdata.offset.getX();
//		double y = te.beamdata.offset.getY();
//		double z = te.beamdata.offset.getZ();
////        float f2 = (float)entitydragon.field_41013_bH.field_41032_a + f1;
////        float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
////        f3 = (f3 * f3 + f3) * 0.2F;
////            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapEnabled);
////            GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
//        int ticks = (int) te.ticksExisted();
//        float f4 = (float)(x - x);
//        float f5 = (float)((/*(double)f3 + */j) - 1.0D - y);
//        float f6 = (float)(z - z);
//        float f7 = MathHelper.sqrt_float(f4 * f4 + f6 * f6);
//        float f8 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6);
//        GL11.glPushMatrix();
//
//        if(te.ticksExisted() < birthLength){
//        	double perc = ((double)ticks / (double) birthLength);
//        	y += 64 - perc*64D;
//        }  
//        
//        GL11.glTranslatef((float)x, (float)y + 2.0F, (float)z);
//        GL11.glRotatef(((float)(-Math.atan2(f6, f4)) * 180F) / (float)Math.PI - 90F, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(((float)(-Math.atan2(f7, f5)) * 180F) / (float)Math.PI - 90F, 1.0F, 0.0F, 0.0F);
//        
//        Tessellator tes = Tessellator.getInstance();
//        VertexBuffer vb = tes.getBuffer();
//        
//        RenderHelper.disableStandardItemLighting();
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        //XXX find a texture loadTexture(DustMod.path + "/beam.png");
//        GL11.glShadeModel(GL11.GL_SMOOTH);
//        float f9 = 0.0F - ((float)(ticks)) * 0.01F;
//        float f10 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6) / 32F - ((float)(ticks)) * 0.01F;
//        //tes.startDrawing(5);
//        vb.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
//        //tessellator.setBrightness(15);
//        int it = 8;
//        Color col = new Color(te.beamdata.color);
//        //GL11.glColor4i(col.getRed(), col.getGreen(), col.getBlue(), 1F);
//
//        for (int jt = 0; jt <= it; jt++)
//        {
//            float f11 = MathHelper.sin(((float)(jt % it) * (float)Math.PI * 2.0F) / (float)it) * 0.75F;
//            float f12 = MathHelper.cos(((float)(jt % it) * (float)Math.PI * 2.0F) / (float)it) * 0.75F;
//            float f13 = ((float)(jt % it) * 1.0F) / (float)it;
//            //tessellator.setColorOpaque_I(0xFFFFFF);
//            //tes.setColorRGBA(col.getRed(), col.getGreen(), col.getBlue(), 128);
//            //tes.addVertexWithUV(f11 * 0.2F, f12 * 0.2F, 0.0D, f13, f10);
//            vb.pos(f11 * 0.2F, f12 * 0.2F, 0.0D).tex(f13, f10).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            //tessellator.setColorOpaque_I(0x000000);
//            //tes.addVertexWithUV(f11, f12, f8, f13, f9);
//            vb.pos(f11, f12, f8).tex(f13, f9).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            
//        }
//
//        tes.draw();
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        GL11.glShadeModel(GL11.GL_FLAT);
//        GL11.glEnable(GL11.GL_LIGHTING);
//        RenderHelper.enableStandardItemLighting();
////            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapDisabled);
////                GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glPopMatrix();
//        GL11.glPopAttrib();
//    }
//
//    public void renderBeam2(TileEntityDustActive te, float f)
//    {
//    	
//    	double j=256;
//		double x = te.beamdata.offset.getX();
//		double y = te.beamdata.offset.getY();
//		double z = te.beamdata.offset.getZ();
//		
//        float var9 = 1f;
//
//        x-=0.5;
//        y+=2;
//        z-=0.5;
//        if (var9 > 0.0F)
//        {
//        	GL11.glPushMatrix();
////            GL11.glTranslatef(0, -1.0F, 0);
//            Tessellator tes = Tessellator.getInstance();
//            VertexBuffer vb = tes.getBuffer();
//            
//            RenderHelper.disableStandardItemLighting();
//            //tes.setBrightness(Integer.MAX_VALUE);
//            
//            RenderHelper.disableStandardItemLighting();
//            //XXX find the texture bindTexture("/misc/beam.png");
//            RenderHelper.disableStandardItemLighting();
//            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glDisable(GL11.GL_LIGHTING);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glDisable(GL11.GL_CULL_FACE);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glDisable(GL11.GL_BLEND);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glDepthMask(true);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//            RenderHelper.disableStandardItemLighting();
//            float var11 = (float)te.getWorld().getTotalWorldTime() + f;
//            float var12 = -var11 * 0.2F - (float)MathHelper.floor_float(-var11 * 0.1F);
//            byte var13 = 1;
//            double var14 = (double)var11 * 0.025D * (1.0D - (double)(var13 & 1) * 2.5D);
//            //tes.startDrawingQuads();
//            vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//            
//            GL11.glEnable(GL11.GL_BLEND);
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            GL11.glDepthMask(false);
//            RenderHelper.disableStandardItemLighting();
//            
//            //tes.setColorRGBA(191 + te.rb/4, 191 + te.gb/4, 191 + te.bb/4, 128);
//            Color col = new Color(te.beamdata.color);
////          var10.setColorRGBA(255, 255, 255, 32);
//            double var16 = (double)var13 * 0.2D;
//            double var18 = 0.5D + Math.cos(var14 + 2.356194490192345D) * var16;
//            double var20 = 0.5D + Math.sin(var14 + 2.356194490192345D) * var16;
//            double var22 = 0.5D + Math.cos(var14 + (Math.PI / 4D)) * var16;
//            double var24 = 0.5D + Math.sin(var14 + (Math.PI / 4D)) * var16;
//            double var26 = 0.5D + Math.cos(var14 + 3.9269908169872414D) * var16;
//            double var28 = 0.5D + Math.sin(var14 + 3.9269908169872414D) * var16;
//            double var30 = 0.5D + Math.cos(var14 + 5.497787143782138D) * var16;
//            double var32 = 0.5D + Math.sin(var14 + 5.497787143782138D) * var16;
//            double var34 = (double)(256.0F * var9);
//            double var36 = 0.0D;
//            double var38 = 1.0D;
//            double var40 = (double)(-1.0F + var12);
//            double var42 = (double)(256.0F * var9) * (0.5D / var16) + var40;
//            vb.pos(x + var18, y + var34, z + var20).tex( var38, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var18, y, z + var20).tex( var38, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var22, y, z + var24).tex( var36, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var22, y + var34, z + var24).tex( var36, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var30, y + var34, z + var32).tex( var38, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var30, y, z + var32).tex( var38, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var26, y, z + var28).tex( var36, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var26, y + var34, z + var28).tex( var36, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var22, y + var34, z + var24).tex( var38, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var22, y, z + var24).tex( var38, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var30, y, z + var32).tex( var36, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var30, y + var34, z + var32).tex( var36, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var26, y + var34, z + var28).tex( var38, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var26, y, z + var28).tex( var38, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var18, y, z + var20).tex( var36, var40).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            vb.pos(x + var18, y + var34, z + var20).tex( var36, var42).color(col.getRed(), col.getGreen(), col.getBlue(), 128).endVertex();
//            tes.draw();
//            //tes.startDrawingQuads();
//            //tes.setColorRGBA(te.rb, te.gb, te.bb, 48);
//            vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
////            var10.setColorRGBA(255, 255, 255, 32);
//            double var44 = 0.2D;
//            double var15 = 0.2D;
//            double var17 = 0.8D;
//            double var19 = 0.2D;
//            double var21 = 0.2D;
//            double var23 = 0.8D;
//            double var25 = 0.8D;
//            double var27 = 0.8D;
//            double var29 = (double)(256.0F * var9);
//            double var31 = 0.0D;
//            double var33 = 1.0D;
//            double var35 = (double)(-1.0F + var12);
//            double var37 = (double)(256.0F * var9) + var35;
//            vb.pos(x + var44, y + var29, z + var15).tex( var33, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var44, y, z + var15).tex( var33, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var17, y, z + var19).tex( var31, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var17, y + var29, z + var19).tex( var31, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var25, y + var29, z + var27).tex( var33, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var25, y, z + var27).tex( var33, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var21, y, z + var23).tex( var31, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var21, y + var29, z + var23).tex( var31, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var17, y + var29, z + var19).tex( var33, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var17, y, z + var19).tex( var33, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var25, y, z + var27).tex( var31, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var25, y + var29, z + var27).tex( var31, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var21, y + var29, z + var23).tex( var33, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var21, y, z + var23).tex( var33, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var44, y, z + var15).tex( var31, var35).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            vb.pos(x + var44, y + var29, z + var15).tex( var31, var37).color(col.getRed(), col.getGreen(), col.getBlue(), 48).endVertex();
//            tes.draw();
//            RenderHelper.enableStandardItemLighting();
//            GL11.glEnable(GL11.GL_LIGHTING);
//            GL11.glEnable(GL11.GL_TEXTURE_2D);
//            GL11.glDepthMask(true);
//            
//            GL11.glPopMatrix();
//        }
//    }
	
	private void drawSpiralBeam(TileEntityDustActive te) {
		// TODO Auto-generated method stub
		
	}
	private void drawBeaconBeam(TileEntityDustActive te,float partialTicks) {
		GlStateManager.alphaFunc(516, 0.1F);
        this.bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);
        GlStateManager.disableFog();
		Color beamColor = new Color(te.beamdata.color);
		//renderBeacon(x, y, z, (double)partialTicks, (double)te.shouldBeamRender(), te.getBeamSegments(), (double)te.getWorld().getTotalWorldTime());
		Vec3d off = te.beamdata.offset;
		float[] colors = new float[]{beamColor.getRed()/255F,beamColor.getGreen()/255F,beamColor.getBlue()/255F};
		//renderBeamSegment(double x, double y, double z, double partialTicks, double shouldBeamrender, double totalWorldTime, int verticalOffset, int height, float[] colors, double beamRadius, double glowRadius)
		//looks like shouldBeamRender affects the scale/speed of the texture animation
		TileEntityBeaconRenderer.renderBeamSegment(off.xCoord, off.yCoord, off.zCoord, partialTicks, 1, te.beamdata.doesRotate?(double)te.getWorld().getTotalWorldTime() : partialTicks, 0, te.beamdata.height, colors, 0.2,0.5);
		GlStateManager.enableFog();
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#isGlobalRenderer(net.minecraft.tileentity.TileEntity)
	 */
	//FIXME this should make it draw even when the TE is out of view. doesn't quite work.
	@Override
	public boolean isGlobalRenderer(TileEntityDustPlaced te) {
		if(te instanceof TileEntityDustActive){
			TileEntityDustActive tea = (TileEntityDustActive)te;
			return (tea.stardata!=null && tea.stardata.doRender)||(tea.beamdata!=null && tea.beamdata.doRender);
		}
		return false;
	}
	
}
