/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-06
 */
package com.zpig333.runesofwizardry.client.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.scene.shape.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.primitives.UnsignedInteger;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.client.TextureStitchEventHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;

/**
 * @author Xilef11
 *
 */
public class ModelDustStorage implements IBakedModel {
	private static Map<String,ModelResourceLocation> resourceMap = new HashMap<String, ModelResourceLocation>();
	private IDustStorageBlock block;
	private int meta;
	private int bgColor,fgColor;
	// create a tag (ModelResourceLocation) for our model.
	  public final ModelResourceLocation modelResourceLocation;

	  public ModelDustStorage(IDustStorageBlock block, int meta) {
		this.block=block;
		this.meta=meta;
		this.modelResourceLocation = getModelResourceLocation(block,meta);
		this.bgColor = block.getIDust().getPrimaryColor(new ItemStack(block.getIDust(),1,meta));
		this.fgColor = block.getIDust().getSecondaryColor(new ItemStack(block.getIDust(),1,meta));
		WizardryLogger.logInfo("Created model for block: "+block.getName()+" "+meta+" bg: "+Integer.toHexString(bgColor)+" fg: "+Integer.toHexString(fgColor));
		bgColor+=0xFF000000;//multiplies opacity by 100% when block is not in the solid layer
		fgColor+=0xFF000000;
		//debug - looks like the color must be big endian (rightmost msb)
		//bgColor = 0xff0000;
		//fgColor = 0x00ff00;
	  }
	public static String getModelResourceLocationPath(IDustStorageBlock block, int meta){
		//maybe block.getIdust().getmodid instead of refs.texturepath?
		return References.texture_path+block.getName()+"_"+meta;
	}
	public static ModelResourceLocation getModelResourceLocation(String path){
		ModelResourceLocation current = resourceMap.get(path);
	    if(current==null){
	    	current=new ModelResourceLocation(path);
	    	resourceMap.put(path, current);
	    }
	    return current;
	}
	public static ModelResourceLocation getModelResourceLocation(IDustStorageBlock block, int meta){
		return getModelResourceLocation(getModelResourceLocationPath(block, meta));
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getFaceQuads(net.minecraft.util.EnumFacing)
	 */
	@Override
	public List getFaceQuads(EnumFacing face) {
		List r = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getFaceQuads(face);
		
		//XXX testing
//		for(int i=0;i<4;i++){
//			int [] quad0=((BakedQuad)r.get(0)).getVertexData();
//			float x = Float.intBitsToFloat(quad0[i*7]);
//			float y = Float.intBitsToFloat(quad0[1+i*7]);
//			float z = Float.intBitsToFloat(quad0[2+i*7]);
//			int color = quad0[3+i*7];
//			int lastvalue = quad0[6+i*7];
//			System.out.println("Dirt has vertex "+i+" on face "+face+" being "+x+" "+y+" "+z+" color: "+color+" last: "+lastvalue);
//		}
		List<BakedQuad> result = new LinkedList<BakedQuad>();
		int[] bg =null;
		int[] fg = null;
		TextureAtlasSprite bgTex = TextureStitchEventHandler.getDustStorageBG();
		//following line works, so problem is with texture... (also color is wrong and dosen't render in inventory)
		//bgTex = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getTexture();
		TextureAtlasSprite fgTex = TextureStitchEventHandler.getDustStorageFG();
		//tall grass shows up as black too, so problem is not with registering texture
		//also, tall grass is shown on block in inventory, but the bg is still gray
		//fgTex=Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.tallgrass.getDefaultState()).getTexture();
		//looks like a bakedquad is a full square, and we have to pass it all its vertices in the int array...
		//also, tintindex should be -1
		if(face==EnumFacing.EAST){
			//BG color
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 1, 1, bgColor, bgTex, 0, 0));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 0, 1, bgColor, bgTex, 0, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 0, 0, bgColor, bgTex, 16, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 1, 0, bgColor, bgTex, 16, 0));
			//fg
			fg = ArrayUtils.addAll(fg, vertexToInts(1.001F, 1, 1, fgColor, fgTex, 0, 0));
			fg = ArrayUtils.addAll(fg, vertexToInts(1.001F, 0, 1, fgColor, fgTex, 0, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1.001F, 0, 0, fgColor, fgTex, 16, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1.001F, 1, 0, fgColor, fgTex, 16, 0));
		}else if(face==EnumFacing.WEST){
			//BG color
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 1, 0, bgColor, bgTex, 0, 0));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 0, 0, bgColor, bgTex, 0, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 0, 1, bgColor, bgTex, 16, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 1, 1, bgColor, bgTex, 16, 0));
			//fg
			fg = ArrayUtils.addAll(fg, vertexToInts(-0.001F, 1, 0, fgColor, fgTex, 0, 0));
			fg = ArrayUtils.addAll(fg, vertexToInts(-0.001F, 0, 0, fgColor, fgTex, 0, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(-0.001F, 0, 1, fgColor, fgTex, 16, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(-0.001F, 1, 1, fgColor, fgTex, 16, 0));
		}else if(face==EnumFacing.NORTH){
			//BG color
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 1, 0, bgColor, bgTex, 0, 0));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 0, 0, bgColor, bgTex, 0, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 0, 0, bgColor, bgTex, 16, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 1, 0, bgColor, bgTex, 16, 0));
			//fg
			fg = ArrayUtils.addAll(fg, vertexToInts(1, 1, -0.001F, fgColor, fgTex, 0, 0));
			fg = ArrayUtils.addAll(fg, vertexToInts(1, 0, -0.001F, fgColor, fgTex, 0, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(0, 0, -0.001F, fgColor, fgTex, 16, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(0, 1, -0.001F, fgColor, fgTex, 16, 0));
		}else if(face==EnumFacing.SOUTH){
			//BG color
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 1, 1, bgColor, bgTex, 0, 0));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 0, 1, bgColor, bgTex, 0, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 0, 1, bgColor, bgTex, 16, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 1, 1, bgColor, bgTex, 16, 0));
			//fg
			fg = ArrayUtils.addAll(fg, vertexToInts(0, 1, 1.001F, fgColor, fgTex, 0, 0));
			fg = ArrayUtils.addAll(fg, vertexToInts(0, 0, 1.001F, fgColor, fgTex, 0, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1, 0, 1.001F, fgColor, fgTex, 16, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1, 1, 1.001F, fgColor, fgTex, 16, 0));
		}else if(face==EnumFacing.DOWN){
			//BG color
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 0, 1, bgColor, bgTex, 0, 0));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 0, 0, bgColor, bgTex, 0, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 0, 0, bgColor, bgTex, 16, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 0, 1, bgColor, bgTex, 16, 0));
			//fg
			fg = ArrayUtils.addAll(fg, vertexToInts(0, -0.001F,1, fgColor, fgTex, 0, 0));
			fg = ArrayUtils.addAll(fg, vertexToInts(0, -0.001F,0, fgColor, fgTex, 0, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1,-0.001F,0, fgColor, fgTex, 16, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1,-0.001F,1, fgColor, fgTex, 16, 0));
		}else if(face==EnumFacing.UP){
			//BG color
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 1, 0, bgColor, bgTex, 0, 0));
			bg = ArrayUtils.addAll(bg, vertexToInts(0, 1, 1, bgColor, bgTex, 0, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 1, 1, bgColor, bgTex, 16, 16));
			bg = ArrayUtils.addAll(bg, vertexToInts(1, 1, 0, bgColor, bgTex, 16, 0));
			//fg
			fg = ArrayUtils.addAll(fg, vertexToInts(0, 1.001F,0, fgColor, fgTex, 0, 0));
			fg = ArrayUtils.addAll(fg, vertexToInts(0, 1.001F,1, fgColor, fgTex, 0, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1,1.001F,1, fgColor, fgTex, 16, 16));
			fg = ArrayUtils.addAll(fg, vertexToInts(1,1.001F,0, fgColor, fgTex, 16, 0));
		}else{
			throw new IllegalArgumentException("Wrong EnumFacing: "+face);//is that even possible...
		}
		result.add(new BakedQuad(bg, -1, face));
		//FIXME no alpha blending...
		BakedQuad fore = new BakedQuad(fg, -1, face);
		result.add(fore);
		return result;
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getGeneralQuads()
	 */
	@Override
	public List getGeneralQuads() {
		List<BakedQuad> res = new LinkedList<BakedQuad>();
		for(EnumFacing face : EnumFacing.VALUES){
			res.addAll(getFaceQuads(face));
		}
		//return res;
		// TODO Auto-generated method stub
		List r = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getGeneralQuads();
		return r;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isAmbientOcclusion()
	 */
	@Override
	public boolean isAmbientOcclusion() {
		return true;
		//return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isGui3d()
	 */
	@Override
	public boolean isGui3d() {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isBuiltInRenderer()
	 */
	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getTexture()
	 */
	@Override
	public TextureAtlasSprite getTexture() {
		//TODO getTexture might need to get tweaked
		TextureAtlasSprite sprite =TextureStitchEventHandler.getDustStorageBG(); 
		//sprite.
		return sprite;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getItemCameraTransforms()
	 */
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}
	
	  /**
	   * Converts the vertex information to the int array format expected by BakedQuads.
	   * @param x x coordinate
	   * @param y y coordinate
	   * @param z z coordinate
	   * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
	   * @param texture the texture to use for the face
	   * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
	   * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
	   * @return
	   */
	  private static int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
	  {
	    return new int[] {
	            Float.floatToRawIntBits(x),
	            Float.floatToRawIntBits(y),
	            Float.floatToRawIntBits(z),
	            /* on a 0x00RRGGBB
	             * on veut 0x00bbggrr
	             * reverse donne 0xbbggrr00
	             */
	            Integer.rotateRight(Integer.reverseBytes(color),8),
	            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
	            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
	            0
	    };
	  }


}
