package com.zpig333.runesofwizardry.client.render;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.core.References;
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
		if(te.stardata!=null && te.stardata.doRender)renderStar(te);
		if(te.beamdata!=null && te.beamdata.doRender)drawBeam(te, partialTicks);
		
		
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

	private static final ResourceLocation TEXTURE_SPIRAL_BEAM = new ResourceLocation(References.modid, "textures/renderer/beam_spiral.png");
	private static final ResourceLocation TEXTURE_RING_BEAM = new ResourceLocation(References.modid, "textures/renderer/beam_rings.png");
	private static final ResourceLocation MISSING_TEXTURE=new ResourceLocation("minecraft:missing_block_texture");
	
	private void drawBeam(TileEntityDustActive te,float partialTicks) {
		GlStateManager.alphaFunc(GL11.GL_GEQUAL, 0.1F);
		//looks like the GL flags get reset by the renderBeamSegment thing, so we are stuck with the solid beam if we want to just call instead of copy
		this.bindTexture(getBeamTexture(te.beamdata.type));
        GlStateManager.disableFog();
		Color beamColor = new Color(te.beamdata.color);
		Vec3d off = te.beamdata.offset;
		float[] colors = new float[]{beamColor.getRed()/255F,beamColor.getGreen()/255F,beamColor.getBlue()/255F};
		//renderBeamSegment(double x, double y, double z, double partialTicks, double shouldBeamrender, double totalWorldTime, int verticalOffset, int height, float[] colors, double beamRadius, double glowRadius)
		//looks like shouldBeamRender affects the scale/speed of the texture animation
		TileEntityBeaconRenderer.renderBeamSegment(off.xCoord, off.yCoord, off.zCoord, partialTicks, getTextureScale(te.beamdata.type), te.beamdata.doesRotate?(double)te.getWorld().getTotalWorldTime() : partialTicks, 0, te.beamdata.height, colors, te.beamdata.beamRadius,te.beamdata.glowRadius);
		GlStateManager.enableFog();
	}
	
	private static ResourceLocation getBeamTexture(BeamType type){
		switch(type){
		case BEACON:return TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM;
		case SPIRAL:return TEXTURE_SPIRAL_BEAM;
		case RINGS: return TEXTURE_RING_BEAM;
		default:return MISSING_TEXTURE;
		}
	}
	private static double getTextureScale(BeamType type){
		switch(type){
		case BEACON:return 1.0;
		case SPIRAL: return 0.5;
		case RINGS: return 1.0;
		default: return 1.0;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#isGlobalRenderer(net.minecraft.tileentity.TileEntity)
	 */
	//FIXME this should make it draw even when the TE is out of view. doesn't quite work.
	@Override
	public boolean isGlobalRenderer(TileEntityDustPlaced te) {
//		if(te instanceof TileEntityDustActive){
//			TileEntityDustActive tea = (TileEntityDustActive)te;
//			return (tea.stardata!=null && tea.stardata.doRender)||(tea.beamdata!=null && tea.beamdata.doRender);
//		}
//		return false;
		return true;
	}
	
}
