/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-06
 */
package com.zpig333.runesofwizardry.client.model;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.References;

/**
 * @author Xilef11
 *
 */
public class ModelDustStorage implements IBakedModel {
	private IDustStorageBlock block;
	private int meta;
	private TextureAtlasSprite texture;
	// create a tag (ModelResourceLocation) for our model.
	  public final ModelResourceLocation modelResourceLocation;

	  public ModelDustStorage(IDustStorageBlock block, int meta) {
		this.block=block;
		this.meta=meta;
		this.modelResourceLocation = new ModelResourceLocation(getModelResourceLocationPath(block,meta));
		this.texture=generateTexture();
	}
	public static String getModelResourceLocationPath(IDustStorageBlock block, int meta){
		return References.texture_path+block.getName()+"_"+meta;
	}
	private TextureAtlasSprite generateTexture() {
		// TODO Auto-generated method stub
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.dirt.getDefaultState()).getTexture();
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
