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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

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
		WizardryLogger.logInfo("Creating model for block: "+block.getName()+" "+meta);
		this.block=block;
		this.meta=meta;
		this.modelResourceLocation = new ModelResourceLocation(getModelResourceLocationPath(block,meta));
		this.bgColor = block.getIDust().getPrimaryColor(new ItemStack(block.getIDust(),1,meta));
		this.fgColor = block.getIDust().getSecondaryColor(new ItemStack(block.getIDust(),1,meta));
	}
	public static String getModelResourceLocationPath(IDustStorageBlock block, int meta){
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
		List<BakedQuad> result = new LinkedList<BakedQuad>();
		List<int[]> vertex = new LinkedList<int[]>();
		TextureAtlasSprite bgTex = TextureStitchEventHandler.getDustStorageBG();
		TextureAtlasSprite fgTex = TextureStitchEventHandler.getDustStorageFG();
		if(face==EnumFacing.EAST){
			//BG color
			vertex.add(vertexToInts(1, 0, 0, bgColor, bgTex, 16, 16));
			vertex.add(vertexToInts(1, 1, 0, bgColor, bgTex, 16, 0));
			vertex.add(vertexToInts(1, 1, 1, bgColor, bgTex, 0, 0));
			vertex.add(vertexToInts(1, 0, 1, bgColor, bgTex, 0, 16));
			//fg
			vertex.add(vertexToInts(1.001F, 0, 0, fgColor, fgTex, 16, 16));
			vertex.add(vertexToInts(1.001F, 1, 0, fgColor, fgTex, 16, 0));
			vertex.add(vertexToInts(1.001F, 1, 1, fgColor, fgTex, 0, 0));
			vertex.add(vertexToInts(1.001F, 0, 1, fgColor, fgTex, 0, 16));
		}else if(face==EnumFacing.WEST){
			//BG color
			vertex.add(vertexToInts(0, 0, 1, bgColor, bgTex, 16, 16));
			vertex.add(vertexToInts(0, 1, 1, bgColor, bgTex, 16, 0));
			vertex.add(vertexToInts(0, 1, 0, bgColor, bgTex, 0, 0));
			vertex.add(vertexToInts(0, 0, 0, bgColor, bgTex, 0, 16));
			//fg
			vertex.add(vertexToInts(-0.001F, 0, 1, fgColor, fgTex, 16, 16));
			vertex.add(vertexToInts(-0.001F, 1, 1, fgColor, fgTex, 16, 0));
			vertex.add(vertexToInts(-0.001F, 1, 0, fgColor, fgTex, 0, 0));
			vertex.add(vertexToInts(-0.001F, 0, 0, fgColor, fgTex, 0, 16));
		}else if(face==EnumFacing.NORTH){
			//BG color
			vertex.add(vertexToInts(0, 0, 0, bgColor, bgTex, 16, 16));
			vertex.add(vertexToInts(0, 1, 0, bgColor, bgTex, 16, 0));
			vertex.add(vertexToInts(1, 1, 0, bgColor, bgTex, 0, 0));
			vertex.add(vertexToInts(1, 0, 0, bgColor, bgTex, 0, 16));
			//fg
			vertex.add(vertexToInts(0, 0, -0.001F, fgColor, fgTex, 16, 16));
			vertex.add(vertexToInts(0, 1, -0.001F, fgColor, fgTex, 16, 0));
			vertex.add(vertexToInts(1, 1, -0.001F, fgColor, fgTex, 0, 0));
			vertex.add(vertexToInts(1, 0, -0.001F, fgColor, fgTex, 0, 16));
		}else if(face==EnumFacing.SOUTH){
			//BG color
			vertex.add(vertexToInts(1, 0, 1, bgColor, bgTex, 16, 16));
			vertex.add(vertexToInts(1, 1, 1, bgColor, bgTex, 16, 0));
			vertex.add(vertexToInts(0, 1, 1, bgColor, bgTex, 0, 0));
			vertex.add(vertexToInts(0, 0, 1, bgColor, bgTex, 0, 16));
			//fg
			vertex.add(vertexToInts(1, 0, 1.001F, fgColor, fgTex, 16, 16));
			vertex.add(vertexToInts(1, 1, 1.001F, fgColor, fgTex, 16, 0));
			vertex.add(vertexToInts(0, 1, 1.001F, fgColor, fgTex, 0, 0));
			vertex.add(vertexToInts(0, 0, 1.001F, fgColor, fgTex, 0, 16));
		}else if(face==EnumFacing.DOWN){
			//BG color
			vertex.add(vertexToInts(1, 0, 0, bgColor, bgTex, 16, 16));
			vertex.add(vertexToInts(1, 0, 1, bgColor, bgTex, 16, 0));
			vertex.add(vertexToInts(0, 0, 1, bgColor, bgTex, 0, 0));
			vertex.add(vertexToInts(0, 0, 0, bgColor, bgTex, 0, 16));
			//fg
			vertex.add(vertexToInts(1,-0.001F,0, fgColor, fgTex, 16, 16));
			vertex.add(vertexToInts(1,-0.001F,1, fgColor, fgTex, 16, 0));
			vertex.add(vertexToInts(0, -0.001F,1, fgColor, fgTex, 0, 0));
			vertex.add(vertexToInts(0, -0.001F,0, fgColor, fgTex, 0, 16));
		}else if(face==EnumFacing.UP){
			//BG color
			vertex.add(vertexToInts(1, 1, 1, bgColor, bgTex, 16, 16));
			vertex.add(vertexToInts(1, 1, 0, bgColor, bgTex, 16, 0));
			vertex.add(vertexToInts(0, 1, 0, bgColor, bgTex, 0, 0));
			vertex.add(vertexToInts(0, 1, 1, bgColor, bgTex, 0, 16));
			//fg
			vertex.add(vertexToInts(1,1.001F,1, fgColor, fgTex, 16, 16));
			vertex.add(vertexToInts(1,1.001F,0, fgColor, fgTex, 16, 0));
			vertex.add(vertexToInts(0, 1.001F,0, fgColor, fgTex, 0, 0));
			vertex.add(vertexToInts(0, 1.001F,1, fgColor, fgTex, 0, 16));
		}else{
			throw new IllegalArgumentException("Wrong EnumFacing: "+face);//is that even possible...
		}
		for(int[] i:vertex){
			result.add(new BakedQuad(i, 0, face));
		}
		return result;
		//SBakedQuad quad1 = new BakedQuad(p_i46232_1_, p_i46232_2_, p_i46232_3_);
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
		return res;
		// TODO Auto-generated method stub
		//return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getGeneralQuads();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isAmbientOcclusion()
	 */
	@Override
	public boolean isAmbientOcclusion() {
		return true;
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
		return TextureStitchEventHandler.getDustStorageBG();
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
	            color,
	            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
	            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
	            0
	    };
	  }


}
