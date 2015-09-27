/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-06
 */
package com.zpig333.runesofwizardry.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

/**
 * @author Xilef11
 *
 */
public class ModelDustStorage implements IBakedModel {
	private static Map<String,ModelResourceLocation> resourceMap = new HashMap<String, ModelResourceLocation>();
	private IDustStorageBlock block;
	private int meta;
	private TextureAtlasSprite texture;
	// create a tag (ModelResourceLocation) for our model.
	  public final ModelResourceLocation modelResourceLocation;

	  public ModelDustStorage(IDustStorageBlock block, int meta) {
		WizardryLogger.logInfo("Creating model for block: "+block.getName()+" "+meta);
		this.block=block;
		this.meta=meta;
		this.modelResourceLocation = new ModelResourceLocation(getModelResourceLocationPath(block,meta));
		this.texture=generateTexture();
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
	private class TextureTemp extends TextureAtlasSprite{

		public TextureTemp(String spriteName) {
			super(spriteName);
			// TODO Auto-generated constructor stub
		}
		
	}
	private TextureAtlasSprite generateTexture() {
		// TODO Auto-generated method stub
		//FIXME apparently, the BlockRendererDispatcher is null when this is called
		try{
			return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getTexture();
		}catch (NullPointerException e){
			e.printStackTrace();
			return null;
		}
	}
	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getFaceQuads(net.minecraft.util.EnumFacing)
	 */
	@Override
	public List getFaceQuads(EnumFacing face) {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getFaceQuads(face);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getGeneralQuads()
	 */
	@Override
	public List getGeneralQuads() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getGeneralQuads();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isAmbientOcclusion()
	 */
	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).isAmbientOcclusion();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isGui3d()
	 */
	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).isGui3d();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#isBuiltInRenderer()
	 */
	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).isBuiltInRenderer();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getTexture()
	 */
	@Override
	public TextureAtlasSprite getTexture() {
		WizardryLogger.logInfo("getTexture was called for "+modelResourceLocation+" of "+block+" "+meta);
		if(texture==null) texture=generateTexture();
		return this.texture;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.resources.model.IBakedModel#getItemCameraTransforms()
	 */
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getItemCameraTransforms();
	}

}
