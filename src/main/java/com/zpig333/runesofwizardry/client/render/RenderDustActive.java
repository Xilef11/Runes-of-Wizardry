package com.zpig333.runesofwizardry.client.render;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive.BeamData;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderDustActive extends RenderDustPlaced {

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.client.render.RenderDustPlaced#renderTileEntityAt(com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced, double, double, double, float, int)
	 */
	@Override
	public void render(TileEntityDustPlaced tileEntity,	double relativeX, double relativeY, double relativeZ,float partialTicks, int blockDamageProgress, float alpha) {
		super.render(tileEntity, relativeX, relativeY, relativeZ,partialTicks, blockDamageProgress, alpha);
		if(!(tileEntity instanceof TileEntityDustActive)){
			WizardryLogger.logError("TileEntity was not active dust for rendering by RenderDustActive");
			return;
		}
		//save GL state
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

		//move the reference point from the player to the position of the block
		GlStateManager.translate(relativeX, relativeY, relativeZ);

		TileEntityDustActive te = (TileEntityDustActive)tileEntity;
		if(te.stardata!=null && te.stardata.doRender)renderStar(te);
		if(te.beamdata!=null && te.beamdata.doRender)drawBeam(te, partialTicks);
		
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	private static final int BIRTHLENGTH = 20*5;
	/**
	 * Draws a bicolor star as defined per the TE
	 * @author billythegoat101, tweaks by Xilef11
	 */
	private void renderStar(TileEntityDustActive te)
    {
		//the star disappears sometimes (especially if it's *far* from the dust) - this is because the TE is not in the view. can't do much about it.
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder vb = tes.getBuffer();
        Vec3d off = te.stardata.offset;

        RenderHelper.disableStandardItemLighting();
        int ticks = (int) (te.ticksExisted() % 200);

        if (ticks >= 100)
        {
            ticks = 200 - ticks - 1;
        }

        float f1 = (ticks) / 200F;
        float f2 = 0.0F;

        if (f1 > 0.7F)
        {
            f2 = (f1 - 0.7F) / 0.2F;
        }

        float yOffset = 0;
        float scale = te.stardata.scale;
        if(te.ticksExisted() < BIRTHLENGTH){
        	double offset = off.y +0.5;
        	double offsetPerc = offset/(1-0.1875);
        	double perc = ((double)ticks / (double) BIRTHLENGTH);

        	scale *= Math.min(perc+0.2,1);
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

        //center 1 block above TE by default
        GlStateManager.translate(0.5+off.x, 1+off.y+yOffset, 0.5+off.z);

        GL11.glScalef(0.02F, 0.02F, 0.02F);
        GL11.glScalef(scale,scale,scale);
        GL11.glScalef(1F, te.stardata.yscale, 1F);
        for (int i = 0; i < ((f1 + f1 * f1) / 2F) * 90F + 30F; i++)
        {
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
            
            vb.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
            float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
            
            Color col = new Color(te.stardata.innercolor);
            vb.pos(0, 0, 0).color(col.getRed(), col.getGreen(), col.getBlue(), (int)(255F * (1.0F - f2))).endVertex();

            col = new Color(te.stardata.outercolor);

            vb.pos(-0.86599999999999999D * f4, f3, -0.5F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 0).endVertex();
            vb.pos(0.86599999999999999D * f4, f3, -0.5F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 0).endVertex();
            vb.pos(0, f3, 1.0F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 0).endVertex();
            vb.pos(-0.86599999999999999D * f4, f3, -0.5F * f4).color(col.getRed(), col.getGreen(), col.getBlue(), 0).endVertex();
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
		this.bindTexture(getBeamTexture(te.beamdata));
        GlStateManager.disableFog();
		Color beamColor = new Color(te.beamdata.color);
		Vec3d off = te.beamdata.offset;
		float[] colors = new float[]{beamColor.getRed()/255F,beamColor.getGreen()/255F,beamColor.getBlue()/255F};
		//renderBeamSegment(double x, double y, double z, double partialTicks, double shouldBeamrender, double totalWorldTime, int verticalOffset, int height, float[] colors, double beamRadius, double glowRadius)
		//looks like shouldBeamRender affects the scale/speed of the texture animation
		TileEntityBeaconRenderer.renderBeamSegment(off.x, off.y, off.z, partialTicks, getTextureScale(te.beamdata), te.beamdata.doesRotate?(double)te.getWorld().getTotalWorldTime() : partialTicks, 0, te.beamdata.height, colors, te.beamdata.beamRadius,te.beamdata.glowRadius);
		GlStateManager.enableFog();
	}
	
	private static ResourceLocation getBeamTexture(BeamData data){
		switch(data.type){
		case BEACON:return TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM;
		case SPIRAL:return TEXTURE_SPIRAL_BEAM;
		case RINGS: return TEXTURE_RING_BEAM;
		case CUSTOM: if(data.customTexture!=null)return data.customTexture;
			//$FALL-THROUGH$
		default:return MISSING_TEXTURE;
		}
	}
	private static double getTextureScale(BeamData data){
		switch(data.type){
		case BEACON:return 1.0;
		case SPIRAL: return 0.5;
		case RINGS: return 1.0;
		case CUSTOM: return data.customTexScale; 
		default: return 1.0;
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#isGlobalRenderer(net.minecraft.tileentity.TileEntity)
	 */
	@Override
	public boolean isGlobalRenderer(TileEntityDustPlaced te) {
		if(te instanceof TileEntityDustActive){
			TileEntityDustActive tea = (TileEntityDustActive)te;
			return (tea.stardata!=null && tea.stardata.doRender)||(tea.beamdata!=null && tea.beamdata.doRender);
		}
		return false;
	}
}
