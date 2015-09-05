/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-04
 */
package com.zpig333.runesofwizardry.client.model;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
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
	    public static TextureAtlasSprite[] icons;

	    public static void register() {
	        MinecraftForge.EVENT_BUS.register(INSTANCE);
	    }
	    
	    @SubscribeEvent
	    public void onTextureStitch(TextureStitchEvent.Pre event){
	    	 icons = new TextureAtlasSprite[15];//not sure what the number should be
	         TextureMap textureMap = event.map;
	    	
	         for(IDustStorageBlock block:DustRegistry.getAllBlocks()){
	        	 
	         }
	    }
	    
	    @SubscribeEvent(priority = EventPriority.LOWEST)
	    // This allows us to create and add Baked Models to the registry
	    public void onModelBake(ModelBakeEvent event) {
	    	
	    }
	    
}
