/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-04
 */
package com.zpig333.runesofwizardry.client.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;

/**
 * @author Xilef11
 * 
 * {@link https://github.com/rwtema/DenseOres/blob/master/src/main/java/com/rwtema/denseores/DenseModelGenerator.java}
 */
public class ModelGeneratorDustStorage {
	public static ModelGeneratorDustStorage INSTANCE = new ModelGeneratorDustStorage();
	public static HashMap<IDustStorageBlock,TextureAtlasSprite> icons;

	public static void register() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event){
		icons = new HashMap<IDustStorageBlock,TextureAtlasSprite>();//not sure what the number should be
		TextureMap textureMap = event.map;

		for(IDustStorageBlock block:DustRegistry.getAllBlocks()){
			// Note: Normally you would simply use map.registerSprite(), this method
			// is only required for custom texture classes.

			// name of custom icon ( must equal getIconName() )
			String name = TextureDustStorage.getName(block);
			// see if there's already an icon of that name
			TextureAtlasSprite texture = textureMap.getTextureExtry(name);
			if (texture == null) {
				// if not create one and put it in the register
				texture = new TextureDustStorage(block);
				textureMap.setTextureEntry(name, texture);
			}

			icons.put(block,textureMap.getTextureExtry(name));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	// This allows us to create and add Baked Models to the registry
	public void onModelBake(ModelBakeEvent event) {
		Map<IDustStorageBlock,IBakedModel> models = new HashMap<IDustStorageBlock,IBakedModel>();
		Map<IDustStorageBlock,IBakedModel> invmodels = new HashMap<IDustStorageBlock,IBakedModel>();

		ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (IDustStorageBlock block : DustRegistry.getAllBlocks()) {

			/// * Block model *
			// get the model registries entry for the current Dense Ore block state
			for(int meta:block.getIDust().getMetaValues()){
				ModelResourceLocation modelResourceLocation = ModelBuilder.getModelResourceLocation(block.getStateFromMeta(meta));

				// add to the registry
				event.modelRegistry.putObject(modelResourceLocation, ModelBuilder.newBlankModel(icons.get(block)));

				/// * Item model *
				// get the item model for the base blocks itemstack
				IBakedModel itemModel = itemModelMesher.getItemModel(new ItemStack(block,1,meta));

				// generate the item model for the Dense ore block
				invmodels.put(block, ModelBuilder.newBlankModel(icons.get(block)));

				// this creates the entry for the inventory block
				ModelResourceLocation inventory = new ModelResourceLocation(modelResourceLocation, "inventory");

				// add to registry
				event.modelRegistry.putObject(inventory, invmodels.get(block));

				// register with the itemModelMesher
				itemModelMesher.register(Item.getItemFromBlock(block), meta, inventory);
			}
		}

	}
}
